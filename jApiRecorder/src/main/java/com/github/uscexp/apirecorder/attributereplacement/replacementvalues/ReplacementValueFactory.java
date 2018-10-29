/*
 * Copyright (C) 2014 - 2018 by haui - all rights reserved
 */
package com.github.uscexp.apirecorder.attributereplacement.replacementvalues;

import com.github.uscexp.apirecorder.attributereplacement.replacementtreatment.ReplacementValueTreatment;

/**
 * The {@link ReplacementValueFactory} constructes valid {@link ReplacementValue} combinations.
 * 
 * @author haui
 *
 */
public final class ReplacementValueFactory {

	private ReplacementValueFactory() {
	}

	public static ReplacementValue createReplacementGivenValue(Object givenValue) {
		ReplacementValue result = new ReplacementGivenValue(new ReplacementValueImpl(), givenValue);
		return result;
	}

	public static ReplacementValue createReplacementArgumentValue(int argumentIndex) {
		ReplacementValue result = new ReplacementArgumentValue(new ReplacementValueImpl(), argumentIndex);
		return result;
	}

	public static ReplacementValue createTreatedReplacementGivenValue(Object givenValue,
			ReplacementValueTreatment... replacementValueTreatments) {
		ReplacementCommandTreatedValue replacementCommandTreatedValue = createTreatments(replacementValueTreatments);
		ReplacementValue result = new ReplacementGivenValue(replacementCommandTreatedValue, givenValue);
		return result;
	}

	public static ReplacementValue createTreatedReplacementArgumentValue(int argumentIndex,
			ReplacementValueTreatment... replacementValueTreatments) {
		ReplacementCommandTreatedValue replacementCommandTreatedValue = createTreatments(replacementValueTreatments);
		ReplacementValue result = new ReplacementArgumentValue(replacementCommandTreatedValue, argumentIndex);
		return result;
	}

	private static ReplacementCommandTreatedValue createTreatments(ReplacementValueTreatment... replacementValueTreatments) {
		ReplacementCommandTreatedValue replacementCommandTreatedValue = null;
		for (int i = replacementValueTreatments.length - 1; i > -1; i--) {
			if (replacementCommandTreatedValue == null) {
				replacementCommandTreatedValue = new ReplacementCommandTreatedValue(new ReplacementValueImpl(),
						replacementValueTreatments[i]);
			} else {
				replacementCommandTreatedValue = new ReplacementCommandTreatedValue(replacementCommandTreatedValue,
						replacementValueTreatments[i]);
			}
		}
		return replacementCommandTreatedValue;
	}
}
