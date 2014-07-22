/*
 * Copyright (C) 2014 by haui - all rights reserved
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

	public abstract String serialize(Object object)
		throws ContentTypeStrategyException;

	public abstract Object deserialize(String serializedObject)
		throws ContentTypeStrategyException;

}
