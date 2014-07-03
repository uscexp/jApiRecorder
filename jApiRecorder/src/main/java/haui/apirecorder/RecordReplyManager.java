/**
 * (C) 2014 haui
 */
package haui.apirecorder;

import java.lang.reflect.InvocationTargetException;
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
public class RecordReplyManager extends Enhancer implements MethodInterceptor {

	private static Logger logger = Logger.getLogger(RecordReplyManager.class
			.getSimpleName());

	private Object objOfApi;
	private RecordReplyMode recordReplyMode;

	public static Object newInstance(Object obj, RecordReplyMode recordReplyMode) {

		RecordReplyManager recordReplyManager = new RecordReplyManager(obj,
				recordReplyMode);
		recordReplyManager.setSuperclass(obj.getClass());

		return recordReplyManager;
	}

	private RecordReplyManager(Object obj, RecordReplyMode recordReplyMode) {
		this.objOfApi = obj;
		this.recordReplyMode = recordReplyMode;
	}

	public Object intercept(Object obj, Method method, Object[] args,
			MethodProxy proxy) {
		Object result;

		switch (recordReplyMode) {
		case FOREWARD:
			result = foreward(obj, method, args, proxy);
			break;

		case RECORD:
			result = record(obj, method, args, proxy);
			break;

		case PB_ONLINE:
			result = pbOnline(obj, method, args, proxy);
			break;

		case PB_OFFLINE:
			result = pbOffline(obj, method, args, proxy);
			break;
		}
		if (isForRecording) {
			try {
				System.out.println("recording...");
				System.out.println("method name: " + method.getName());
				System.out.print("method arguments:");
				for (Object arg : args) {
					System.out.print(" " + arg);
				}
				System.out.println();
				result = method.invoke(objOfApi, args);
				System.out.println("result: " + result);
				RecordedInformation recordedInformation = new RecordedInformation();
				recordedInformation.setMethodName(method.getName());
				recordedInformation.setArgs(args);
				recordedInformation.setReturnValue(result);
				// persist your information

			} catch (InvocationTargetException e) {
				throw e.getTargetException();
			} catch (Exception e) {
				throw new RuntimeException("unexpected invocation exception: "
						+ e.getMessage());
			} finally {
				// do nothing
			}
			return result;
		} else {
			try {
				System.out.println("replying...");
				System.out.println("method name: " + method.getName());
				System.out.print("method arguments:");
				for (Object arg : args) {
					System.out.print(" " + arg);
				}

				RecordedInformation recordedInformation = new RecordedInformation();
				recordedInformation.setMethodName(method.getName());
				recordedInformation.setArgs(args);

				// if your invocation information (this RecordedInformation) is
				// found in the previously collected map, then return the
				// returnValue from that RecordedInformation.
				// if corresponding RecordedInformation does not exists then
				// invoke the real method (like in recording step) and wrap the
				// collected information into RecordedInformation and persist it
				// as you like!

			} catch (InvocationTargetException e) {
				throw e.getTargetException();
			} catch (Exception e) {
				throw new RuntimeException("unexpected invocation exception: "
						+ e.getMessage());
			} finally {
				// do nothing
			}
			return result;
		}
	}

	private RecordInformation playback(Object obj, Method method, Object[] args,
			MethodProxy proxy) {
		RecordInformation result;
		return null;
	}

	private RecordInformation record(RecordInformation recordInformation) {
		// contentTypeStrategy here
		// saveStrategy here
		return recordInformation;
	}

	private RecordInformation foreward(Object obj, Method method, Object[] args,
			MethodProxy proxy) {
		RecordInformation result = null;
		try {
			logStep(method, args, "forewarding...");
			Object methodResult = method.invoke(objOfApi, args);
			result = new RecordInformation();
			result.setMethodName(method.getName());
			result.setArgs(args);
			result.setReturnValue(methodResult);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
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
