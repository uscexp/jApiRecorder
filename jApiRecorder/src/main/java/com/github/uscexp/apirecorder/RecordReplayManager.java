/*
 * Copyright (C) 2014 by haui - all rights reserved
 */
package com.github.uscexp.apirecorder;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import com.github.uscexp.apirecorder.attributereplacement.AttributeValueReplacer;
import com.github.uscexp.apirecorder.attributereplacement.ReplacementConfiguration;
import com.github.uscexp.apirecorder.contenttypestrategy.ContentTypeStrategy;
import com.github.uscexp.apirecorder.contenttypestrategy.XStreamContentTypeStrategy;
import com.github.uscexp.apirecorder.exception.ContentTypeStrategyException;
import com.github.uscexp.apirecorder.exception.ReadWriteStrategyException;
import com.github.uscexp.apirecorder.exception.RecordReplayException;
import com.github.uscexp.apirecorder.exception.ReplacementValueException;
import com.github.uscexp.apirecorder.readwritestrategy.H2ReadWriteStrategy;
import com.github.uscexp.apirecorder.readwritestrategy.ReadWriteStrategy;
import com.github.uscexp.dotnotation.exception.AttributeAccessExeption;

/**
 * This class creates a dynamic proxy of a class to record/replay method calls.
 * <p>
 * There are four modes to go:<br>
 * <dl>
 * <dt>FOREWARD</dt>
 * <dd>calls the original method directly and returns the value.</dd>
 * <dt>RECORD</dt>
 * <dd>calls the original method and records the returned value.</dd>
 * <dt>RP_ONLINE</dt>
 * <dd>tries first to get a return value from the recorded data. if there is one it will be returned,
 * else it calls the original method, records the return value and returns it.</dd>
 * <dt>RP_OFFLINE</dt>
 * <dd>tries to get a return value from the recorded data. if there is none it will return null.</dd>
 * </p>
 * It is possible to implement your own {@link ReadWriteStrategy} and/or {@link ContentTypeStrategy}.
 * You only have to implement the given interfaces.
 * <p>
 * Already implemented {@link ContentTypeStrategy} is:<br>
 * {@link XStreamContentTypeStrategy} which serialize/deserialize the objects to be recorded/replayed with XStream.<br>
 * </p>
 * <p>
 * Already implemented {@link ReadWriteStrategy} is:<br>
 * {@link H2ReadWriteStrategy} which reads/writes the objects to be recorded/replayed within a H2 database (memory or file database).
 * </p>
 * @author haui
 *
 */
public class RecordReplayManager extends Enhancer implements MethodInterceptor {

	private static final Level LOG_LEVEL = Level.FINEST;
	private static final Level ERROR_LOG_LEVEL = Level.INFO;

	private static Logger logger = Logger.getLogger(RecordReplayManager.class.getName());

	private final RecordReplayMode recordReplayMode;
	private final ContentTypeStrategy contentTypeStrategy;
	private final ReadWriteStrategy readWriteStrategy;
	private final RecordReplayConfiguration recordReplayConfiguration;

	/**
	 * Creates a dynamic proxy of the given class using a constructor without parameters.
	 *
	 * @param classToProxy class to create a dynamic proxy from it.
	 * @param recordReplayMode the behavior of the proxy.
	 * @param contentTypeStrategy the strategy for serialzation/deserialization.
	 * @param readWriteStrategy the strategy for reading/writing the data.
	 * @param recordReplayConfiguration the configuration of the identification of the recorded objects and the attribute replacment.
	 * @return the dynamic proxy object.
	 */
	public static Object newInstance(Class<? extends Object> classToProxy, RecordReplayMode recordReplayMode,
			ContentTypeStrategy contentTypeStrategy, ReadWriteStrategy readWriteStrategy,
			RecordReplayConfiguration recordReplayConfiguration) {
		return
			newInstance(classToProxy, null, null, recordReplayMode, contentTypeStrategy, readWriteStrategy, recordReplayConfiguration);
	}

	/**
	* Creates a dynamic proxy of the given class using a constructor with parameters.
	 *
	* @param classToProxy class to create a dynamic proxy from it.
	 * @param parameterTypes parameter types for the constructor call.
	 * @param args parameter args for the constructor call.
	* @param recordReplayMode the behavior of the proxy.
	* @param contentTypeStrategy the strategy for serialzation/deserialization.
	* @param readWriteStrategy the strategy for reading/writing the data.
	* @param recordReplayConfiguration the configuration of the identification of the recorded objects and the attribute replacment.
	* @return the dynamic proxy object.
	 */
	public static Object newInstance(Class<? extends Object> classToProxy, Class<? extends Object>[] parameterTypes, Object[] args,
			RecordReplayMode recordReplayMode, ContentTypeStrategy contentTypeStrategy, ReadWriteStrategy readWriteStrategy,
			RecordReplayConfiguration recordReplayConfiguration) {

		RecordReplayManager recordReplayManager = new RecordReplayManager(recordReplayMode, contentTypeStrategy, readWriteStrategy,
				recordReplayConfiguration);
		recordReplayManager.setSuperclass(classToProxy);
		recordReplayManager.setCallback(recordReplayManager);

		if ((parameterTypes == null) || (args == null))
			return recordReplayManager.create();
		else
			return recordReplayManager.create(parameterTypes, args);
	}

	private RecordReplayManager(RecordReplayMode recordReplyMode, ContentTypeStrategy contentTypeStrategy,
			ReadWriteStrategy readWriteStrategy, RecordReplayConfiguration recordReplayConfiguration) {
		this.recordReplayMode = recordReplyMode;
		this.contentTypeStrategy = contentTypeStrategy;
		this.readWriteStrategy = readWriteStrategy;
		if (recordReplayConfiguration == null) {
			this.recordReplayConfiguration = new RecordReplayConfiguration();
		} else {
			this.recordReplayConfiguration = recordReplayConfiguration;
		}
	}

	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) {
		RecordInformation recordInformation = new RecordInformation(method.getName(), args,
				recordReplayConfiguration.getArgumentIndices4PrimaryKey(method.getName()));

		try {

			switch (recordReplayMode) {
				case FOREWARD:
					recordInformation = foreward(obj, method, args, proxy);
					break;

				case RECORD:
					recordInformation = foreward(obj, method, args, proxy);
					record(recordInformation);
					break;

				case RP_ONLINE:
					recordInformation = replay(obj, method, args, proxy);
					if (recordInformation.getReturnValue() == null) {
						recordInformation = foreward(obj, method, args, proxy);
						record(recordInformation);
					}
					break;

				case RP_OFFLINE:
					recordInformation = replay(obj, method, args, proxy);
					break;
			}
		} catch (ContentTypeStrategyException | ReadWriteStrategyException | AttributeAccessExeption | ReplacementValueException e) {
			throw new RecordReplayException("Unexpected exception in RecordReplayManager", e);
		}
		return recordInformation.getReturnValue();
	}

	private RecordInformation replay(Object obj, Method method, Object[] args, MethodProxy proxy)
		throws ReadWriteStrategyException, ContentTypeStrategyException, AttributeAccessExeption, ReplacementValueException {
		RecordInformation result = new RecordInformation(method.getName(), args,
				recordReplayConfiguration.getArgumentIndices4PrimaryKey(method.getName()));
		logStep(method, args, "replay...");
		String serializedObject = readWriteStrategy.read(result.getReturnValueId());
		if (serializedObject != null) {
			Object object = contentTypeStrategy.deserialize(serializedObject);
			if (object != null) {
				Collection<ReplacementConfiguration> replacementConfigurations = recordReplayConfiguration.getReplacementConfigurations(
						method.getName());
				if ((replacementConfigurations != null) && !replacementConfigurations.isEmpty()) {
					object = AttributeValueReplacer.replace(object, result, replacementConfigurations);
				}
			}
			result.setReturnValue(object);
		}
		return result;
	}

	private void record(RecordInformation recordInformation)
		throws ContentTypeStrategyException, ReadWriteStrategyException {

		String type = "?";
		if (recordInformation.getReturnValue() != null) {
			type = recordInformation.getReturnValue().getClass().getSimpleName();
		}
		String message = String.format("recording... ReturnValueId: %s; ReturnValue type: %s", recordInformation.getReturnValueId(), type);
		logger.log(LOG_LEVEL, message);
		String serializedObject = contentTypeStrategy.serialize(recordInformation.getReturnValue());
		readWriteStrategy.write(recordInformation.getReturnValueId(), serializedObject);
	}

	private RecordInformation foreward(Object obj, Method method, Object[] args, MethodProxy proxy) {
		RecordInformation result = null;
		try {
			logStep(method, args, "forewarding...");
			Object methodResult = proxy.invokeSuper(obj, args);
			result = new RecordInformation(method.getName(), args,
					recordReplayConfiguration.getArgumentIndices4PrimaryKey(method.getName()));
			result.setReturnValue(methodResult);
		} catch (Throwable e) {
			logger.log(ERROR_LOG_LEVEL, e.getMessage(), e);
		}
		return result;
	}

	private void logStep(Method method, Object[] args, String step) {
		String arguments = "";
		for (Object arg : args) {
			arguments += " '" + arg + "'";
		}
		String message = String.format("%s method name: %s; method arguments:%s", step, method.getName(), arguments);
		logger.log(LOG_LEVEL, message);
	}
}
