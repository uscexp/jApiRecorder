/*
 * Copyright (C) 2014 by haui - all rights reserved
 */
package com.github.uscexp.apirecorder.attributereplacement.replacementvalues;

import com.github.uscexp.apirecorder.RecordInformation;
import com.github.uscexp.apirecorder.exception.ReplacementValueException;

/**
 * Specifies a fix value for later attribute replacement.
 * 
 * @author haui
 *
 */
public class ReplacementGivenValue extends AbstractReplacementValue {
    
    private Object givenValue;
    
    public ReplacementGivenValue(ReplacementValue replacementValue, Object givenValue) {
        super(replacementValue);
        this.givenValue = givenValue;
    }

    @Override
    public Object getReplacementValue(RecordInformation recordInformation) throws ReplacementValueException {
        setValue(givenValue);
        return replacementValue.getReplacementValue(recordInformation);
    }
}
