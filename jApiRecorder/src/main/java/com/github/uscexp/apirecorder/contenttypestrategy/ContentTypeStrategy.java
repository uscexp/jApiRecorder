/*
 * Copyright (C) 2014 by haui - all rights reserved
 */
package com.github.uscexp.apirecorder.contenttypestrategy;

import com.github.uscexp.apirecorder.exception.ContentTypeStrategyException;

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
