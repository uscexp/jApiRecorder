/*
 * Copyright (C) 2014 by haui - all rights reserved
 */
package com.github.uscexp.apirecorder.attributereplacement;

import com.github.uscexp.apirecorder.attributereplacement.replacementvalues.ReplacementValue;

/**
 * Holds the configuration for one attribute to change. The replacement path specifies an attribute path
 * in dot notation to the attribute one wants to change and the {@link ReplacementValue} calculates the value
 * to replace the current attribute value with.
 * @author haui
 *
 */
public class ReplacementConfiguration {

//    private final String methodName;
	private final String replacementPath;
	private final ReplacementValue value;

	public ReplacementConfiguration(String replacementPath, ReplacementValue value) {
		super();
//        this.methodName = methodName;
		this.replacementPath = replacementPath;
		this.value = value;
	}

//    public String getMethodName() {
//        return methodName;
//    }

	public String getReplacementPath() {
		return replacementPath;
	}

	public ReplacementValue getValue() {
		return value;
	}

}
