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
public interface ReplacementValue {

	void setValue(Object value);

	Object getValue();

	Object getReplacementValue(RecordInformation recordInformation) throws ReplacementValueException;
}
