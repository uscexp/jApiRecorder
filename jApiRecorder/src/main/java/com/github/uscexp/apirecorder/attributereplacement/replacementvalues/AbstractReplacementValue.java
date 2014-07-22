/*
 * Copyright (C) 2014 by haui - all rights reserved
 */
package com.github.uscexp.apirecorder.attributereplacement.replacementvalues;


/**
 * @author haui
 *
 */
public abstract class AbstractReplacementValue implements ReplacementValue {

    /** concrete replacement value  */
    protected ReplacementValue replacementValue;

    public AbstractReplacementValue(ReplacementValue replacementValue) {
        super();
        this.replacementValue = replacementValue;
    }

    @Override
    public void setValue(Object value) {
        replacementValue.setValue(value);
    }

    @Override
    public Object getValue() {
        return replacementValue.getValue();
    }
}
