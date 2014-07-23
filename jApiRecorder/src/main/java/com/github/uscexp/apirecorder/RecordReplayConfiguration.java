/*
 * Copyright (C) 2014 by haui - all rights reserved
 */
package com.github.uscexp.apirecorder;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.github.uscexp.apirecorder.attributereplacement.AttributeValueReplacer;
import com.github.uscexp.apirecorder.attributereplacement.ReplacementConfiguration;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

/**
 * The configuration of the identification of the recorded objects and the attribute replacement.
 * <p>
 * For the identification of the method call result, one specifiy the indices of the given arguments
 * of the method call which should be used to build a id for later retreivement of the result object.
 * </p>
 * <p>
 * The result values can be altered after retreiving it from the store (only on replay). To do so one have to
 * specify the {@link ReplacementConfiguration} objects with the corresponding method key.
 * See {@link AttributeValueReplacer} for more details.
 * </p>
 *
 * @author haui
 *
 */
public class RecordReplayConfiguration {

	private Map<String, int[]> methodArgIdx4Pk = new HashMap<>();
	private Multimap<String, ReplacementConfiguration> methodRelpacementConfiguration = ArrayListMultimap.create();

	public void addArgumentIndices4PrimaryKey(String method, int... index) {
		methodArgIdx4Pk.put(method, index);
	}

	public int[] getArgumentIndices4PrimaryKey(String method) {
		return methodArgIdx4Pk.get(method);
	}

	public void addReplacementConfiguration(String method, ReplacementConfiguration configuration) {
		methodRelpacementConfiguration.put(method, configuration);
	}

	public Collection<ReplacementConfiguration> getReplacementConfigurations(String method) {
		return methodRelpacementConfiguration.get(method);
	}
}
