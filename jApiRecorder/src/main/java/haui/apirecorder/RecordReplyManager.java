/**
 * (C) 2014 haui
 */
package haui.apirecorder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * @author haui
 *
 */
public class RecordReplyManager extends Enhancer implements MethodInterceptor {

    private Object objOfApi;
    private RecordReplyMode recordReplyMode;

    public static Object newInstance(Object obj, RecordReplyMode recordReplyMode) {

    	RecordReplyManager recordReplyManager = new RecordReplyManager(obj, recordReplyMode);
    	recordReplyManager.setSuperclass(obj.getClass());
    	
        return recordReplyManager;
    }

    private RecordReplyManager(Object obj, RecordReplyMode recordReplyMode) {
        this.objOfApi = obj;
        this.recordReplyMode = recordReplyMode;
    }


    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) {
        Object result;
        
        switch (recordReplyMode) {
		case FOREWARD:
			
			break;

		case RECORD:
			
			break;

		case PB_ONLINE:
			
			break;
			
		case PB_OFFLINE:
			
			break;
		default:
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
                //persist your information

            } catch (InvocationTargetException e) {
                throw e.getTargetException();
            } catch (Exception e) {
                throw new RuntimeException("unexpected invocation exception: " +
                        e.getMessage());
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

                //if your invocation information (this RecordedInformation) is found in the previously collected map, then return the returnValue from that RecordedInformation.
                //if corresponding RecordedInformation does not exists then invoke the real method (like in recording step) and wrap the collected information into RecordedInformation and persist it as you like!

            } catch (InvocationTargetException e) {
                throw e.getTargetException();
            } catch (Exception e) {
                throw new RuntimeException("unexpected invocation exception: " +
                        e.getMessage());
            } finally {
                // do nothing
            }
            return result;
        }
    }
}

