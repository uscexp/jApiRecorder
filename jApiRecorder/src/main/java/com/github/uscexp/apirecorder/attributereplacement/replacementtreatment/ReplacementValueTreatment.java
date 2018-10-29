/*
 * Copyright (C) 2014 - 2018 by haui - all rights reserved
 */
package com.github.uscexp.apirecorder.attributereplacement.replacementtreatment;

import com.github.uscexp.apirecorder.exception.ReplacementValueTreatmentException;

/**
 * A {@link ReplacementValueTreatment} contains the logic to manipulate given value.
 * 
 * @author haui
 *
 */
public interface ReplacementValueTreatment {

    Object treatReplacementValue(Object value) throws ReplacementValueTreatmentException;
}
