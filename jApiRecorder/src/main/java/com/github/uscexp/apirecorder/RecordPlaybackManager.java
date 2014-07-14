/*
 * Copyright (C) 2014 by haui - all rights reserved
 */
package com.github.uscexp.apirecorder;

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import com.github.uscexp.apirecorder.contenttypestrategy.ContentTypeStrategy;
import com.github.uscexp.apirecorder.contenttypestrategy.XStreamContentTypeStrategy;
import com.github.uscexp.apirecorder.exception.ContentTypeStrategyException;
import com.github.uscexp.apirecorder.exception.ReadWriteStrategyException;
import com.github.uscexp.apirecorder.exception.RecordPlaybackException;
import com.github.uscexp.apirecorder.readwritestrategy.H2ReadWriteStrategy;
import com.github.uscexp.apirecorder.readwritestrategy.ReadWriteStrategy;

/**
 * This class creates a dynamic proxy of a class to record/playback.
 * <p>
 * There are four modes to go:<br>
 * <dl>
 * <dt>FOREWARD</dt>
 * <dd>calls the original method directly and returns the value.</dd>
 * <dt>RECORD</dt>
 * <dd>calls the original method and records the returned value.</dd>
 * <dt>PB_ONLINE</dt>
 * <dd>tries first to get a return value from the recorded data. if there is one it will be returned,
 * else it calls the original method, records the return value and returns it.</dd>
 * <dt>PB_OFFLINE</dt>
 * <dd>tries to get a return value from the recorded data. if there is none it will return a null.</dd>
 * </p>
 * It is possible to implement your own {@link ReadWriteStrategy} and/or {@link ContentTypeStrategy}.
 * You only have to implement the given interfaces.
 * <p>
 * Already implemented {@link ContentTypeStrategy} is:<br>
 * {@link XStreamContentTypeStrategy} which serialize/deserialize the objects to be recorded/playbacked with XStream.<br>
 * </p>
 * <p>
 * Already implemented {@link ReadWriteStrategy} is:<br>
 * {@link H2ReadWriteStrategy} which reads/writes the objects to be recorded/playbacked within a H2 database (memory or file database).
 * </p>
 * @author haui
 *
 */
public class RecordPlaybackManager extends Enhancer implements MethodInterceptor {

	private static Logger logger = Logger.getLogger(RecordPlaybackManager.class.getSimpleName());

	private final RecordPlaybackMode recordPlaybackMode;
	private final ContentTypeStrategy contentTypeStrategy;
	private final ReadWriteStrategy readWriteStrategy;
	private final RecordPlaybackConfiguration recordPlaybackConfiguration;

	/**
	 * Creates a dynamic proxy of the given class using a constructor without parameters.
	 *
	 * @param classToProxy class to create a dynamic proxy from it.
	 * @param recordPlaybackMode the behavior of the proxy.
	 * @param contentTypeStrategy the strategy for serialzation/deserialization.
	 * @param readWriteStrategy the strategy for reading/writing the data.
	 * @param recordPlaybackConfiguration the configuration of the identification of the recorded objects and the attribute replacment.
	 * @return the dynamic proxy object.
	 */
	public static Object newInstance(Class<? extends Object> classToProxy, RecordPlaybackMode recordPlaybackMode,
			ContentTypeStrategy contentTypeStrategy, ReadWriteStrategy readWriteStrategy,
			RecordPlaybackConfiguration recordPlaybackConfiguration) {
		return
			newInstance(classToProxy, null, null, recordPlaybackMode, contentTypeStrategy, readWriteStrategy, recordPlaybackConfiguration);
	}

	/**
     * Creates a dynamic proxy of the given class using a constructor with parameters.
	 * 
     * @param classToProxy class to create a dynamic proxy from it.
	 * @param parameterTypes parameter types for the constructor call.
	 * @param args parameter args for the constructor call.
     * @param recordPlaybackMode the behavior of the proxy.
     * @param contentTypeStrategy the strategy for serialzation/deserialization.
     * @param readWriteStrategy the strategy for reading/writing the data.
     * @param recordPlaybackConfiguration the configuration of the identification of the recorded objects and the attribute replacment.
     * @return the dynamic proxy object.
	 */
	public static Object newInstance(Class<? extends Object> classToProxy, Class<? extends Object>[] parameterTypes, Object[] args,
			RecordPlaybackMode recordPlaybackMode, ContentTypeStrategy contentTypeStrategy, ReadWriteStrategy readWriteStrategy,
			RecordPlaybackConfiguration recordPlaybackConfiguration) {

		RecordPlaybackManager recordPlaybackManager = new RecordPlaybackManager(recordPlaybackMode, contentTypeStrategy, readWriteStrategy,
				recordPlaybackConfiguration);
		recordPlaybackManager.setSuperclass(classToProxy);
		recordPlaybackManager.setCallback(recordPlaybackManager);

		if ((parameterTypes == null) || (args == null))
			return recordPlaybackManager.create();
		else
			return recordPlaybackManager.create(parameterTypes, args);
	}

	private RecordPlaybackManager(RecordPlaybackMode recordReplyMode, ContentTypeStrategy contentTypeStrategy,
			ReadWriteStrategy readWriteStrategy, RecordPlaybackConfiguration recordPlaybackConfiguration) {
		this.recordPlaybackMode = recordReplyMode;
		this.contentTypeStrategy = contentTypeStrategy;
		this.readWriteStrategy = readWriteStrategy;
		if (recordPlaybackConfiguration == null) {
			this.recordPlaybackConfiguration = new RecordPlaybackConfiguration();
		} else {
			this.recordPlaybackConfiguration = recordPlaybackConfiguration;
		}
	}

	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) {
		RecordInformation recordInformation = new RecordInformation(method.getName(), args,
				recordPlaybackConfiguration.getArgumentIndices4PrimaryKey(method.getName()));

		try {

			switch (recordPlaybackMode) {
				case FOREWARD:
					recordInformation = foreward(obj, method, args, proxy);
					break;

				case RECORD:
					recordInformation = foreward(obj, method, args, proxy);
					record(recordInformation);
					break;

				case PB_ONLINE:
					recordInformation = playback(obj, method, args, proxy);
					if (recordInformation.getReturnValue() == null) {
						recordInformation = foreward(obj, method, args, proxy);
						record(recordInformation);
					}
					break;

				case PB_OFFLINE:
					recordInformation = playback(obj, method, args, proxy);
					break;
			}
		} catch (ContentTypeStrategyException | ReadWriteStrategyException e) {
			throw new RecordPlaybackException("Unexpected exception in RecordPlaybackManager", e);
		}
		return recordInformation.getReturnValue();
	}

	private RecordInformation playback(Object obj, Method method, Object[] args, MethodProxy proxy)
		throws ReadWriteStrategyException, ContentTypeStrategyException {
		RecordInformation result = new RecordInformation(method.getName(), args,
				recordPlaybackConfiguration.getArgumentIndices4PrimaryKey(method.getName()));
		logStep(method, args, "playback...");
		String serializedObject = readWriteStrategy.read(result.getReturnValueId());
		if (serializedObject != null) {
			Object object = contentTypeStrategy.deserialize(serializedObject);
			result.setReturnValue(object);
		}
		return result;
	}

	private void record(RecordInformation recordInformation)
		throws ContentTypeStrategyException, ReadWriteStrategyException {

		logger.finest("recording...");
		logger.finest(String.format("ReturnValueId: %s", recordInformation.getReturnValueId()));
		if (recordInformation.getReturnValue() != null)
			logger.finest(String.format("ReturnValue type: %s", recordInformation.getReturnValue().getClass()));
		String serializedObject = contentTypeStrategy.serialize(recordInformation.getReturnValue());
		readWriteStrategy.write(recordInformation.getReturnValueId(), serializedObject);
	}

	private RecordInformation foreward(Object obj, Method method, Object[] args, MethodProxy proxy) {
		RecordInformation result = null;
		try {
			logStep(method, args, "forewarding...");
			Object methodResult = proxy.invokeSuper(obj, args);
			result = new RecordInformation(method.getName(), args,
					recordPlaybackConfiguration.getArgumentIndices4PrimaryKey(method.getName()));
			result.setReturnValue(methodResult);
		} catch (Throwable e) {
			logger.log(Level.FINEST, e.getMessage(), e);
		}
		return result;
	}

	private void logStep(Method method, Object[] args, String step) {
		logger.finest(step);
		logger.finest("method name: " + method.getName());
		logger.finest("method arguments:");
		for (Object arg : args) {
			logger.finest(" " + arg);
		}
		logger.finest("\n");
	}
}
