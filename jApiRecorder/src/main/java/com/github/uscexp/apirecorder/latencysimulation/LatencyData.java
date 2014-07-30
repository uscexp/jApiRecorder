/*
 * Copyright (C) 2014 by haui - all rights reserved
 */
package com.github.uscexp.apirecorder.latencysimulation;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * holds the latency data for a specific recorded object.
 * 
 * @author haui
 *
 */
public class LatencyData {

	private int numberOfCyclesIgnored;
	private List<Integer> latencies = new ArrayList<>();
	
	public int getNumberOfCyclesIgnored() {
		return numberOfCyclesIgnored;
	}
	
	public void setNumberOfCyclesIgnored(int numberOfCyclesIgnored) {
		this.numberOfCyclesIgnored = numberOfCyclesIgnored;
	}
	
	public List<Integer> getLatencies() {
		return latencies;
	}

	public void increaseNumberOfCyclesIgnored() {
		++this.numberOfCyclesIgnored;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((latencies == null) ? 0 : latencies.hashCode());
		result = prime * result + numberOfCyclesIgnored;
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
		LatencyData other = (LatencyData) obj;
		if (latencies == null) {
			if (other.latencies != null)
				return false;
		} else if (!latencies.equals(other.latencies))
			return false;
		if (numberOfCyclesIgnored != other.numberOfCyclesIgnored)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
