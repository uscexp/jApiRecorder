/*
 * (C) 2014 haui
 */
package haui.apirecorder.attributereplacement;

/**
 * @author haui
 *
 */
public class ReplacementConfiguration {

    private final String methodName;
    private final String replacementPath;
    private final Object value;
    
    public ReplacementConfiguration(String methodName, String replacementPath, Object value) {
        super();
        this.methodName = methodName;
        this.replacementPath = replacementPath;
        this.value = value;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getReplacementPath() {
        return replacementPath;
    }

    public Object getValue() {
        return value;
    } 

}
