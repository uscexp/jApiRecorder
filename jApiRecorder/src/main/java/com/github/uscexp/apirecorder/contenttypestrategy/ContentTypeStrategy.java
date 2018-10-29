/*
 * Copyright (C) 2014 - 2018 by haui - all rights reserved
 */
package com.github.uscexp.apirecorder.contenttypestrategy;

import com.github.uscexp.apirecorder.exception.ContentTypeStrategyException;

/**
 * The {@link ContentTypeStrategy} provides the logic to serialize and deserialize the objects.
 *
 * @author haui
 *
 */
public interface ContentTypeStrategy {

	/**
	 * serializes an object with given strategy.
	 *
	 * @param object to be serialized.
	 * @return serialized object.
	 * @throws ContentTypeStrategyException on error.
	 */
	String serialize(Object object)
		throws ContentTypeStrategyException;

	/**
	 * deserializes a serialized object with a given strategy.
	 *
	 * @param serializedObject serialized object.
	 * @return deserialized object.
	 * @throws ContentTypeStrategyException on error.
	 */
	Object deserialize(String serializedObject)
		throws ContentTypeStrategyException;

}
