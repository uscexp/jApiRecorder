/*
 * Copyright (C) 2014 by haui - all rights reserved
 */
package com.github.uscexp.apirecorder.readwritestrategy;

import com.github.uscexp.apirecorder.exception.ReadWriteStrategyException;
import com.github.uscexp.apirecorder.latencysimulation.LatencyData;

/**
 * The {@link ReadWriteStrategy} provides the logic to store and retreive the data.
 *
 * @author haui
 *
 */
public interface ReadWriteStrategy {

	/**
	 * writes a serialized object to a store.
	 *
	 * @param id used to identify the serialized object.
	 * @param serializedObject serialized object to save.
	 * @throws ReadWriteStrategyException on error.
	 */
	void write(long id, String serializedObject)
		throws ReadWriteStrategyException;

	/**
	 * reads a serialized object from a store.
	 *
	 * @param id used to identify the serialized object.
	 * @return serialized object.
	 * @throws ReadWriteStrategyException on error.
	 */
	String read(long id)
		throws ReadWriteStrategyException;

	/**
	 * closes the connection to the store.
	 *
	 * @throws ReadWriteStrategyException on error.
	 */
	void close()
		throws ReadWriteStrategyException;

	/**
	 * writes the corresponding {@link LatencyData} of a serialized object to a store.
	 *
	 * @param id the identifier of the serialized object/latency data.
	 * @param latencyData the data to save.
	 * @throws ReadWriteStrategyException on error.
	 */
	void writeLatency(long id, LatencyData latencyData)
		throws ReadWriteStrategyException;

	/**
	 * reads the corresponding {@link LatencyData} of a serialized object from a store.
	 *
	 * @param id the identifier of the serialized object/latency data.
	 * @return the latency data of a serialized object.
	 * @throws ReadWriteStrategyException on error.
	 */
	public abstract LatencyData readLatency(long id)
		throws ReadWriteStrategyException;
}
