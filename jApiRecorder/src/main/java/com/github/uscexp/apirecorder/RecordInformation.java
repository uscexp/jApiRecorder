/*
 * Copyright (C) 2014 by haui - all rights reserved
 */
package com.github.uscexp.apirecorder;

import java.util.Arrays;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.github.uscexp.apirecorder.exception.ReadWriteStrategyException;
import com.github.uscexp.apirecorder.latencysimulation.LatencyConfiguration;
import com.github.uscexp.apirecorder.latencysimulation.LatencyData;
import com.github.uscexp.apirecorder.readwritestrategy.ReadWriteStrategy;

/**
 * @author haui
 *
 */
public class RecordInformation {
	private String methodName;
	private Object[] args;

	/** argument indices which are used to build the primary key */
	private int[] argIdx4Pk;
	private LatencyConfiguration latencyConfiguration;
	private LatencyData latencyData;
	private Object returnValue;

	public RecordInformation(String methodName, Object[] args, RecordReplayConfiguration recordReplayConfiguration,
			ReadWriteStrategy readWriteStrategy)
		throws ReadWriteStrategyException {
		this(methodName, args, recordReplayConfiguration.getArgumentIndices4PrimaryKey(methodName),
			recordReplayConfiguration.getLatencyConfiguration(methodName),
			readWriteStrategy.readLatency(getReturnValueId(recordReplayConfiguration.getArgumentIndices4PrimaryKey(methodName), args)));
	}

	public RecordInformation(String methodName, Object[] args, int[] argIdx4Pk) {
		this(methodName, args, argIdx4Pk, new LatencyConfiguration(0), new LatencyData());
	}

	private RecordInformation(String methodName, Object[] args, int[] argIdx4Pk, LatencyConfiguration latencyConfiguration,
			LatencyData latencyData) {
		super();
		validateArguments(methodName, args, argIdx4Pk);
		this.methodName = methodName;
		this.args = args;
		this.argIdx4Pk = argIdx4Pk;
		this.latencyConfiguration = latencyConfiguration;
		this.latencyData = latencyData;
		if(this.latencyData == null)
			this.latencyData = new LatencyData();
	}

	private void validateArguments(String methodName, Object[] args, int[] argIdx4Pk) {
		if (methodName == null) {
			throw new IllegalArgumentException(String.format("Parameter %s can't be NULL!", "methodName"));
		}
		if ((args == null) || (args.length <= 0)) {
			throw new IllegalArgumentException(String.format("Parameter %s must have at least one value!", "args"));
		}
		if (argIdx4Pk != null) {
			if (args.length < argIdx4Pk.length) {
				throw new IllegalArgumentException(String.format("Length of array %s can not be larger than length of array %s!",
						"argIdx4Pk", "args"));
			}
			for (int i = 0; i < argIdx4Pk.length; i++) {
				if (argIdx4Pk[i] < 0) {
					throw new IllegalArgumentException(String.format("The index %d in array %s can not be negative!", i, "argIdx4Pk"));
				}
				if (argIdx4Pk[i] >= args.length) {
					throw new IllegalArgumentException(String.format("The index %d in array %s can not be larger than length of array %s!",
							i, "argIdx4Pk", "args"));
				}
			}
		}
	}

	public long getReturnValueId() {
		return getReturnValueId(argIdx4Pk, args);
	}

	private static long getReturnValueId(int[] argIdx4Pk, Object[] args) {
		long result = 0;

		if ((argIdx4Pk != null) && (argIdx4Pk.length > 0)) {
			for (int i = 0; i < argIdx4Pk.length; i++) {
				result += args[argIdx4Pk[i]].hashCode() * 31L;
			}
		} else {
			for (int i = 0; i < args.length; i++) {
				result += args[i].hashCode() * 31L;
			}
		}

		return result;
	}

	protected static int calculateLatency(LatencyConfiguration latencyConfiguration, LatencyData latencyData) {
		int result = 0;
		switch (latencyConfiguration.getLatencyType()) {
			case STATIC_DELAY:
				result = latencyConfiguration.getStaticDelayInMilliseconds();
				break;

			case CYCLES_CALCULATED_DELAY:
				if (latencyData.getLatencies().size() > 0) {
					int latency = 0;
					for (Integer lat : latencyData.getLatencies()) {
						latency += lat.intValue();
					}
					latency = latency / latencyData.getLatencies().size();
					result = latency;
				} else {
					result = latencyConfiguration.getStaticDelayInMilliseconds();
				}
				break;
		}
		return result;
	}

	public int calculateLatency() {
		return calculateLatency(latencyConfiguration, latencyData);
	}

	public void addLatency(int currentLatencyInMilliseconds) {
		switch (latencyConfiguration.getLatencyType()) {
			case STATIC_DELAY:
				break;

			case CYCLES_CALCULATED_DELAY:
				int cycles = latencyData.getLatencies().size();
				if ((cycles < latencyConfiguration.getNumberOfCycles()) ||
						(latencyData.getNumberOfCyclesIgnored() < latencyConfiguration.getIgnoreFirstNumberOfCycles())) {
					if (latencyData.getNumberOfCyclesIgnored() <= latencyConfiguration.getIgnoreFirstNumberOfCycles()) {
						latencyData.getLatencies().clear();
						latencyData.getLatencies().add(currentLatencyInMilliseconds);
						latencyData.increaseNumberOfCyclesIgnored();
					} else {
						latencyData.getLatencies().add(currentLatencyInMilliseconds);
					}
				}
				break;
		}
	}

	public LatencyData getLatencyData() {
		return latencyData;
	}

	public String getMethodName() {
		return methodName;
	}

	public Object[] getArgs() {
		return args;
	}

	public Object getReturnValue() {
		return returnValue;
	}

	public void setReturnValue(Object returnValue) {
		this.returnValue = returnValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + Arrays.hashCode(argIdx4Pk);
		result = (prime * result) + Arrays.hashCode(args);
		result = (prime * result) + ((latencyConfiguration == null) ? 0 : latencyConfiguration.hashCode());
		result = (prime * result) + ((latencyData == null) ? 0 : latencyData.hashCode());
		result = (prime * result) + ((methodName == null) ? 0 : methodName.hashCode());
		result = (prime * result) + ((returnValue == null) ? 0 : returnValue.hashCode());
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
		RecordInformation other = (RecordInformation) obj;
		if (!Arrays.equals(argIdx4Pk, other.argIdx4Pk))
			return false;
		if (!Arrays.equals(args, other.args))
			return false;
		if (latencyConfiguration == null) {
			if (other.latencyConfiguration != null)
				return false;
		} else if (!latencyConfiguration.equals(other.latencyConfiguration))
			return false;
		if (latencyData == null) {
			if (other.latencyData != null)
				return false;
		} else if (!latencyData.equals(other.latencyData))
			return false;
		if (methodName == null) {
			if (other.methodName != null)
				return false;
		} else if (!methodName.equals(other.methodName))
			return false;
		if (returnValue == null) {
			if (other.returnValue != null)
				return false;
		} else if (!returnValue.equals(other.returnValue))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
