/*
 * (C) 2014 haui
 */
package haui.apirecorder.readwritestrategy;

import haui.apirecorder.exception.ReadWriteStrategyException;

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