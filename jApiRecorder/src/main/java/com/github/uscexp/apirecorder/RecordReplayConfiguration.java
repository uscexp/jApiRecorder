/*
 * Copyright (C) 2014 by haui - all rights reserved
 */
package com.github.uscexp.apirecorder;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.github.uscexp.apirecorder.attributereplacement.AttributeValueReplacer;
import com.github.uscexp.apirecorder.attributereplacement.ReplacementConfiguration;
import com.github.uscexp.apirecorder.latencysimulation.LatencyConfiguration;
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
 * <p>
 * One can specifiy a global and/or a per method latency configuration to simulate the method call latency of the real call.
 * </p>
 *
 * @author haui
 *
 */
public class RecordReplayConfiguration {

	private Map<String, int[]> methodArgIdx4Pk = new HashMap<>();
	private Multimap<String, ReplacementConfiguration> methodRelpacementConfiguration = ArrayListMultimap.create();
	private Map<String, LatencyConfiguration> methodLatencyConfiguration = new HashMap<>();
	private LatencyConfiguration globalLatencyConfiguration;
	private boolean simulateLatency = false;

	/**
	 * constructor for a configuration without latency simulation.
	 */
	public RecordReplayConfiguration() {
		this(null, false);
	}

	/**
	 * constructor for a configuration without global latency configuration.
	 *
	 * @param simulateLatency true to activate the latency simulation.
	 */
	public RecordReplayConfiguration(boolean simulateLatency) {
		this(null, simulateLatency);
	}

	/**
	 * constructor for a configuration with a global latency configuration.
	 *
	 * @param globalLatencyConfiguration the global latency configuration (will be used if there is no specific latency configuration for a method defined).
	 * @param simulateLatency true to activate the latency simulation.
	 */
	public RecordReplayConfiguration(LatencyConfiguration globalLatencyConfiguration, boolean simulateLatency) {
		super();
		this.globalLatencyConfiguration = globalLatencyConfiguration;
		this.simulateLatency = simulateLatency;
		if (this.globalLatencyConfiguration == null) {
			this.globalLatencyConfiguration = new LatencyConfiguration(0);
		}
	}

	/**
	 * @return true if the latency simulation is activated.
	 */
	public boolean isSimulateLatency() {
		return simulateLatency;
	}

	/**
	 * @param simulateLatency true to activate the latency simulation.
	 */
	public void setSimulateLatency(boolean simulateLatency) {
		this.simulateLatency = simulateLatency;
	}

	/**
	 * specifies the relevant arguments (index of the arguments in method call) which count as id for the recorded value of a method call.
	 *
	 * @param method method name.
	 * @param index indices of the argument which will be used for the identification of the recorded value.
	 */
	public void addArgumentIndices4PrimaryKey(String method, int... index) {
		methodArgIdx4Pk.put(method, index);
	}

	/**
	 * get the relevant arguments (index of the arguments in method call) which count as id for the recorded value of a method call.
	 *
	 * @param method method name.
	 * @return indices of the argument which will be used for the identification of the recorded value.
	 */
	public int[] getArgumentIndices4PrimaryKey(String method) {
		return methodArgIdx4Pk.get(method);
	}

	/**
	 * adds a replacement configuration for a recorded value of a specific method call.
	 *
	 * @param method method name.
	 * @param configuration the replacement configuration for a recorded value.
	 */
	public void addReplacementConfiguration(String method, ReplacementConfiguration configuration) {
		methodRelpacementConfiguration.put(method, configuration);
	}

	/**
	 * get the replacement configuration for a recorded value of a specific method call.
	 *
	 * @param method method name.
	 * @return collection of replacement configurations.
	 */
	public Collection<ReplacementConfiguration> getReplacementConfigurations(String method) {
		return methodRelpacementConfiguration.get(method);
	}

	/**
	 * get a latency configuration for a specific method call.
	 * if there is no configuration specified for a specific method call the global latency configuration (if exists) will be returned.
	 * if neither a configuration for a specific method call nor a global latency configuration exists, it returns null.
	 *
	 * @param method method name.
	 * @return the latency configuration for a method call.
	 */
	public LatencyConfiguration getLatencyConfiguration(String method) {
		LatencyConfiguration latencyConfiguration = null;
		if (method != null)
			latencyConfiguration = methodLatencyConfiguration.get(method);

		if (latencyConfiguration == null) {
			latencyConfiguration = globalLatencyConfiguration;
		}
		return latencyConfiguration;
	}

	/**
	 * adds a latency configuration for a specific method call.
	 *
	 * @param method method name.
	 * @param latencyConfiguration the latency configuration.
	 */
	public void addLatencyConfiguration(String method, LatencyConfiguration latencyConfiguration) {
		methodLatencyConfiguration.put(method, latencyConfiguration);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((globalLatencyConfiguration == null) ? 0 : globalLatencyConfiguration.hashCode());
		result = (prime * result) + ((methodArgIdx4Pk == null) ? 0 : methodArgIdx4Pk.hashCode());
		result = (prime * result) + ((methodLatencyConfiguration == null) ? 0 : methodLatencyConfiguration.hashCode());
		result = (prime * result) + ((methodRelpacementConfiguration == null) ? 0 : methodRelpacementConfiguration.hashCode());
		result = (prime * result) + (simulateLatency ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RecordReplayConfiguration other = (RecordReplayConfiguration) obj;
		if (globalLatencyConfiguration == null) {
			if (other.globalLatencyConfiguration != null)
				return false;
		} else if (!globalLatencyConfiguration.equals(other.globalLatencyConfiguration))
			return false;
		if (methodArgIdx4Pk == null) {
			if (other.methodArgIdx4Pk != null)
				return false;
		} else if (!methodArgIdx4Pk.equals(other.methodArgIdx4Pk))
			return false;
		if (methodLatencyConfiguration == null) {
			if (other.methodLatencyConfiguration != null)
				return false;
		} else if (!methodLatencyConfiguration.equals(other.methodLatencyConfiguration))
			return false;
		if (methodRelpacementConfiguration == null) {
			if (other.methodRelpacementConfiguration != null)
				return false;
		} else if (!methodRelpacementConfiguration.equals(other.methodRelpacementConfiguration))
			return false;
		if (simulateLatency != other.simulateLatency)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
