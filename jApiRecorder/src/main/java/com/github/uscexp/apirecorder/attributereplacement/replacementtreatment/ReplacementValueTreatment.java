/*
 * Copyright (C) 2014 by haui - all rights reserved
 */
package com.github.uscexp.apirecorder.attributereplacement.replacementtreatment;

import com.github.uscexp.apirecorder.exception.ReplacementValueTreatmentException;

/**
 * @author haui
 *
 */
public interface ReplacementValueTreatment {

    Object treatReplacementValue(Object value) throws ReplacementValueTreatmentException;
}
