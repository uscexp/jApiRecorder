/*
 * Copyright (C) 2014 by haui - all rights reserved
 */
package com.github.uscexp.apirecorder.attributereplacement.replacementvalues;

import com.github.uscexp.apirecorder.RecordInformation;
import com.github.uscexp.apirecorder.exception.ReplacementValueException;

/**
 * Specifies an index of a argument value of the method call for later attribute replacement.
 * 
 * @author haui
 *
 */
public class ReplacementArgumentValue extends AbstractReplacementValue {

    private int argumentIndex;
    
    public ReplacementArgumentValue(ReplacementValue replacementValue, int argumentIndex) {
        super(replacementValue);
        this.replacementValue = replacementValue;
        this.argumentIndex = argumentIndex;
    }

    @Override
    public Object getReplacementValue(RecordInformation recordInformation) throws ReplacementValueException {
        Object value = recordInformation.getArgs()[argumentIndex];
        setValue(value);
        return replacementValue.getReplacementValue(recordInformation);
    }
}
