/*
 * (C) 2014 haui
 */
package haui.apirecorder;

import java.util.HashMap;
import java.util.Map;

/**
 * @author haui
 *
 */
public class RecordPlaybackConfiguration {

    private Map<String, int[]> methodArgIdx4Pk = new HashMap<>();
    
    public void addArgumentIndices4PrimaryKey(String method, int...index) {
        methodArgIdx4Pk.put(method, index);
    }

    public int[] getArgumentIndices4PrimaryKey(String method) {
        return methodArgIdx4Pk.get(method);
    }
}
