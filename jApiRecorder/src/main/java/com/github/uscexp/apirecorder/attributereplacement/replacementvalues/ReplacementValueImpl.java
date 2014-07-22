/*
 * Copyright (C) 2014 by haui - all rights reserved
 */
package com.github.uscexp.apirecorder.attributereplacement.replacementvalues;

import com.github.uscexp.apirecorder.RecordInformation;
import com.github.uscexp.apirecorder.exception.ReplacementValueException;

/**
 * @author haui
 *
 */
public class ReplacementValueImpl implements ReplacementValue {

    private Object value;

    @Override
    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public Object getReplacementValue(RecordInformation recordInformation) throws ReplacementValueException {
        return value;
    }
}
