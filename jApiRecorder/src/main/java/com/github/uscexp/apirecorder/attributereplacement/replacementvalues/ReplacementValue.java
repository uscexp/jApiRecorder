/*
 * Copyright (C) 2014 - 2018 by haui - all rights reserved
 */
package com.github.uscexp.apirecorder.attributereplacement.replacementvalues;

import com.github.uscexp.apirecorder.RecordInformation;
import com.github.uscexp.apirecorder.exception.ReplacementValueException;

/**
 * The {@link ReplacementValue} holds/calculates the real object to replace another value
 * in the result of a (replay) method call.
 * 
 * @author haui
 *
 */
public interface ReplacementValue {

	void setValue(Object value);

	Object getValue();

	Object getReplacementValue(RecordInformation recordInformation) throws ReplacementValueException;
}
