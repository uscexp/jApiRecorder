/*
 * (C) 2014 haui
 */
package haui.apirecorder.attributereplacement;

import java.util.Collection;

/**
 * @author haui
 *
 */
public final class AttributeValueReplacer {

    private AttributeValueReplacer() {
        
    }
    
    public static <T> T replace(T rootElement, Collection<ReplacementConfiguration> replacementConfigurations) {
        T result = rootElement;
        return result;
    }
}