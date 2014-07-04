/*
 * (C) 2014 haui
 */
package haui.apirecorder.contenttypestrategy;

import haui.apirecorder.exception.ContentTypeStrategyException;

/**
 * @author haui
 *
 */
public interface ContentTypeStrategy {

	public abstract String serialize(Object object)
		throws ContentTypeStrategyException;

	public abstract Object deserialize(String serializedObject)
		throws ContentTypeStrategyException;

}
