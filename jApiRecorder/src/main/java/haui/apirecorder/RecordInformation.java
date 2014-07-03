/**
 * (C) 2014 haui
 */
package haui.apirecorder;

/**
 * @author haui
 * 
 */
public class RecordInformation {
	private String methodName;
	private Object[] args;
	private Object returnValue;

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

	public Object getReturnValue() {
		return returnValue;
	}

	public void setReturnValue(Object returnValue) {
		this.returnValue = returnValue;
	}

	@Override
	public int hashCode() {
		return super.hashCode(); // change your implementation as you like!
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj); // change your implementation as you like!
	}
}
