/*
 * (C) 2014 haui
 */
package haui.apirecorder;

import haui.apirecorder.attributereplacement.ReplacementConfiguration;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

/**
 * @author haui
 *
 */
public class RecordPlaybackConfiguration {

    private Map<String, int[]> methodArgIdx4Pk = new HashMap<>();
    private Multimap<String, ReplacementConfiguration> methodRelpacementConfiguration = ArrayListMultimap.create();
    
    public void addArgumentIndices4PrimaryKey(String method, int...index) {
        methodArgIdx4Pk.put(method, index);
    }

    public int[] getArgumentIndices4PrimaryKey(String method) {
        return methodArgIdx4Pk.get(method);
    }
    
    public void addReplacementConfiguration(String method, ReplacementConfiguration configuration) {
        methodRelpacementConfiguration.put(method, configuration);
    }
    
    public Collection<ReplacementConfiguration> getReplacementConfigurations(String method) {
        return methodRelpacementConfiguration.get(method);
    }
}
