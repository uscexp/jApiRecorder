/*
 * Copyright (C) 2014 - 2018 by haui - all rights reserved
 */
package com.github.uscexp.apirecorder.attributereplacement.replacementvalues;

import com.github.uscexp.apirecorder.RecordInformation;
import com.github.uscexp.apirecorder.attributereplacement.replacementtreatment.ReplacementValueTreatment;
import com.github.uscexp.apirecorder.exception.ReplacementValueException;
import com.github.uscexp.apirecorder.exception.ReplacementValueTreatmentException;

/**
 * The {@link ReplacementCommandTreatedValue} manipulates values from previous (parent) {@link ReplacementValue}
 * objects with the given {@link ReplacementValueTreatment}.
 * 
 * @author haui
 *
 */
public class ReplacementCommandTreatedValue extends AbstractReplacementValue {

    ReplacementValueTreatment replacementValueTreatment;
    
    public ReplacementCommandTreatedValue(ReplacementValue replacementValue, ReplacementValueTreatment replacementValueTreatment) {
        super(replacementValue);
        this.replacementValueTreatment = replacementValueTreatment;
    }

    @Override
    public Object getReplacementValue(RecordInformation recordInformation) throws ReplacementValueException {
        try {
            Object value = getValue();
            value = replacementValueTreatment.treatReplacementValue(value);
            setValue(value);
        } catch (ReplacementValueTreatmentException e) {
            throw new ReplacementValueException("Unexpected exception during treatment of replacement value.", e);
        }
        return replacementValue.getReplacementValue(recordInformation);
    }
}
