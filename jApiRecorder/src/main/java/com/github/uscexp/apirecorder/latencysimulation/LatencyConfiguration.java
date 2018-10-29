/*
 * Copyright (C) 2014 - 2018 by haui - all rights reserved
 */
package com.github.uscexp.apirecorder.latencysimulation;

/**
 * configuration for the latency simulation.
 *
 * @author haui
 *
 */
public class LatencyConfiguration {

	private LatencyType latencyType;

	private boolean recordAtOnce = false;

	private int ignoreFirstNumberOfCycles;

	private int numberOfCycles;

	private int staticDelayInMilliseconds;

	/**
	 * constructor for real measured lacency for the method call over a number of cycles.
	 *
	 * @param ignoreFirstNumberOfCycles number of latency measures to be ignored (e.g. on cache effects).
	 * @param numberOfCycles number of cycles to measure the latency (after the ignored cycles).
	 * @param recordAtOnce record all cycles in one run (results in a numberOfCycles longer method call).
	 */
	public LatencyConfiguration(int ignoreFirstNumberOfCycles, int numberOfCycles, boolean recordAtOnce) {
		this(ignoreFirstNumberOfCycles, numberOfCycles, 0, recordAtOnce);
	}

	/**
	 * constructor for a static lacency for the method call.
	 *
	 * @param staticDelayInMilliseconds the static dalay in milliseconds to be used for the latency simulation.
	 */
	public LatencyConfiguration(int staticDelayInMilliseconds) {
		this(0, 0, staticDelayInMilliseconds, false);
	}

	private LatencyConfiguration(int ignoreFirstNumberOfCycles, int numberOfCycles, int staticDelayInMilliseconds, boolean recordAtOnce) {
		super();
		this.ignoreFirstNumberOfCycles = ignoreFirstNumberOfCycles;
		this.numberOfCycles = numberOfCycles;
		this.staticDelayInMilliseconds = staticDelayInMilliseconds;
		this.recordAtOnce = recordAtOnce;
		if (numberOfCycles > 0)
			this.latencyType = LatencyType.CYCLES_CALCULATED_DELAY;
		else
			this.latencyType = LatencyType.STATIC_DELAY;
	}

	/**
	 * @return the latency type (static or real measured).
	 */
	public LatencyType getLatencyType() {
		return latencyType;
	}

	/**
	 * is only valid on {@link LatencyType#CYCLES_CALCULATED_DELAY}.
	 *
	 * @return true if the measure cycles will be recorded with one run (numberOfCycles method calls).
	 */
	public boolean isRecordAtOnce() {
		return recordAtOnce;
	}

	/**
	 * is only valid on {@link LatencyType#CYCLES_CALCULATED_DELAY}.
	 *
	 * @return number of latency measures to be ignored (e.g. on cache effects).
	 */
	public int getIgnoreFirstNumberOfCycles() {
		return ignoreFirstNumberOfCycles;
	}

	/**
	 * is only valid on {@link LatencyType#CYCLES_CALCULATED_DELAY}.
	 *
	 * @return number of cycles to measure the latency (after the ignored cycles).
	 */
	public int getNumberOfCycles() {
		return numberOfCycles;
	}

	/**
	 * is only valid on {@link LatencyType#STATIC_DELAY}.
	 *
	 * @return the static dalay in milliseconds to be used for the latency simulation.
	 */
	public int getStaticDelayInMilliseconds() {
		return staticDelayInMilliseconds;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ignoreFirstNumberOfCycles;
		result = (prime * result) + ((latencyType == null) ? 0 : latencyType.hashCode());
		result = (prime * result) + numberOfCycles;
		result = (prime * result) + (recordAtOnce ? 1231 : 1237);
		result = (prime * result) + staticDelayInMilliseconds;
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
		LatencyConfiguration other = (LatencyConfiguration) obj;
		if (ignoreFirstNumberOfCycles != other.ignoreFirstNumberOfCycles)
			return false;
		if (latencyType != other.latencyType)
			return false;
		if (numberOfCycles != other.numberOfCycles)
			return false;
		if (recordAtOnce != other.recordAtOnce)
			return false;
		if (staticDelayInMilliseconds != other.staticDelayInMilliseconds)
			return false;
		return true;
	}

}
