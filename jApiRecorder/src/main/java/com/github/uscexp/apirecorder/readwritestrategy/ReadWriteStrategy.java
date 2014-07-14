/*
 * Copyright (C) 2014 by haui - all rights reserved
 */
package com.github.uscexp.apirecorder.readwritestrategy;

import com.github.uscexp.apirecorder.exception.ReadWriteStrategyException;

/**
 * @author haui
 *
 */
public interface ReadWriteStrategy {

	public abstract void write(long id, String serializedObject)
			throws ReadWriteStrategyException;

	public abstract String read(long id) throws ReadWriteStrategyException;

	public abstract void close() throws ReadWriteStrategyException;

}