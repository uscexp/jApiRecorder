/*
 * Copyright (C) 2014 by haui - all rights reserved
 */
package com.github.uscexp.apirecorder.attributereplacement;

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