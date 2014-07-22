/*
 * Copyright (C) 2014 by haui - all rights reserved
 */
package com.github.uscexp.apirecorder.attributereplacement;

import java.util.Collection;
import java.util.Date;
import java.util.logging.Logger;

import com.github.uscexp.apirecorder.RecordInformation;
import com.github.uscexp.apirecorder.attributereplacement.replacementtreatment.ReplacementValueTreatment;
import com.github.uscexp.apirecorder.attributereplacement.replacementvalues.ReplacementArgumentValue;
import com.github.uscexp.apirecorder.attributereplacement.replacementvalues.ReplacementCommandTreatedValue;
import com.github.uscexp.apirecorder.attributereplacement.replacementvalues.ReplacementGivenValue;
import com.github.uscexp.apirecorder.attributereplacement.replacementvalues.ReplacementValue;
import com.github.uscexp.apirecorder.exception.ReplacementValueException;
import com.github.uscexp.dotnotation.DotNotationAccessor;
import com.github.uscexp.dotnotation.exception.AttributeAccessExeption;

/**
 * This class replaces attribute values in the given root element.
 * <p>
 * One can specify an attribute path from the result object in dot notation form to the attribute one want to change
 * (see: {@link DotNotationAccessor}). One can set a fix value ({@link ReplacementGivenValue}) or use a passed argument
 * ({@link ReplacementArgumentValue}) to the secified attribute of the result object.<br>
 * The value one want to set to an attribute in the result can be manipulated before setting it to the attribute.
 * E.g. one want to change a {@link Date} attribute of the result and change it to the current date. This is possibe
 * with the {@link ReplacementCommandTreatedValue} where one can add serveral {@link ReplacementValueTreatment} objects
 * to change the value, in this example to change it to the current date.
 * </p>
 * @author haui
 *
 */
public final class AttributeValueReplacer {

	private static Logger logger = Logger.getLogger(AttributeValueReplacer.class.getName());

	private AttributeValueReplacer() {

	}

	public static <T> T replace(T rootElement, RecordInformation recordInformation,
			Collection<ReplacementConfiguration> replacementConfigurations)
		throws AttributeAccessExeption, ReplacementValueException {
		T result = rootElement;
		DotNotationAccessor dotNotationAccessor = new DotNotationAccessor(false, true, true);

		for (ReplacementConfiguration replacementConfiguration : replacementConfigurations) {
			String replacementPath = replacementConfiguration.getReplacementPath();
			ReplacementValue value = replacementConfiguration.getValue();
			dotNotationAccessor.setAttribute(rootElement, replacementPath, value.getReplacementValue(recordInformation));
			logger.finest(String.format("Replaced attribute '%s' in object '%s'.", replacementPath,
					rootElement.getClass().getSimpleName()));
		}
		return result;
	}
}
