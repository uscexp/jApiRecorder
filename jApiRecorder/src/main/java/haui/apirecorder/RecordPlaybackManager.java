/*
 * (C) 2014 haui
 */
package haui.apirecorder;

import haui.apirecorder.contenttypestrategy.ContentTypeStrategy;
import haui.apirecorder.exception.ContentTypeStrategyException;
import haui.apirecorder.exception.ReadWriteStrategyException;
import haui.apirecorder.exception.RecordPlaybackException;
import haui.apirecorder.readwritestrategy.ReadWriteStrategy;

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * @author haui
 * 
 */
public class RecordPlaybackManager extends Enhancer implements MethodInterceptor {

    private static Logger logger = Logger.getLogger(RecordPlaybackManager.class.getSimpleName());

    private final RecordPlaybackMode recordPlaybackMode;
    private final ContentTypeStrategy contentTypeStrategy;
    private final ReadWriteStrategy readWriteStrategy;
    private final RecordPlaybackConfiguration recordPlaybackConfiguration;

    public static Object newInstance(Class<? extends Object> classToProxy, RecordPlaybackMode recordPlaybackMode,
            ContentTypeStrategy contentTypeStrategy, ReadWriteStrategy readWriteStrategy,
            RecordPlaybackConfiguration recordPlaybackConfiguration) {
        return newInstance(classToProxy, null, null, recordPlaybackMode, contentTypeStrategy, readWriteStrategy,
                recordPlaybackConfiguration);
    }

    public static Object newInstance(Class<? extends Object> classToProxy, Class<? extends Object>[] parameterTypes,
            Object[] args, RecordPlaybackMode recordPlaybackMode, ContentTypeStrategy contentTypeStrategy,
            ReadWriteStrategy readWriteStrategy, RecordPlaybackConfiguration recordPlaybackConfiguration) {

        RecordPlaybackManager recordPlaybackManager = new RecordPlaybackManager(recordPlaybackMode,
                contentTypeStrategy, readWriteStrategy, recordPlaybackConfiguration);
        recordPlaybackManager.setSuperclass(classToProxy);
        recordPlaybackManager.setCallback(recordPlaybackManager);

        if (parameterTypes == null || args == null)
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
        } finally {

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

    private void record(RecordInformation recordInformation) throws ContentTypeStrategyException,
            ReadWriteStrategyException {

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
