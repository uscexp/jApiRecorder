/*
 * Copyright (C) 2014 by haui - all rights reserved
 */
package com.github.uscexp.apirecorder.attributereplacement;

import java.util.Collection;
import java.util.logging.Logger;

import com.github.uscexp.dotnotation.DotNotationAccessor;
import com.github.uscexp.dotnotation.exception.AttributeAccessExeption;

/**
 * @author haui
 *
 */
public final class AttributeValueReplacer {

	private static Logger logger = Logger.getLogger(AttributeValueReplacer.class.getName());

	private AttributeValueReplacer() {

	}

	public static <T> T replace(T rootElement, Collection<ReplacementConfiguration> replacementConfigurations)
		throws AttributeAccessExeption {
		T result = rootElement;
		DotNotationAccessor dotNotationAccessor = new DotNotationAccessor(false, true, true);

		for (ReplacementConfiguration replacementConfiguration : replacementConfigurations) {
			String replacementPath = replacementConfiguration.getReplacementPath();
			Object value = replacementConfiguration.getValue();
			dotNotationAccessor.setAttribute(rootElement, replacementPath, value);
			logger.finest(String.format("Replaced attribute '%s' with value '%s' in object '%s'.", replacementPath, value.toString(),
					rootElement.getClass().getSimpleName()));
		}
		return result;
	}
}
