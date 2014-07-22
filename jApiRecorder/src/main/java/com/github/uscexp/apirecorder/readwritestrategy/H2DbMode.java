/*
 * Copyright (C) 2014 by haui - all rights reserved
 */
package com.github.uscexp.apirecorder.readwritestrategy;

/**
 * @author haui
 *
 */
public enum H2DbMode {
	MEMORY("jdbc:h2:mem:h2db;DB_CLOSE_DELAY=-1"),FILE("jdbc:h2:");
	
	private String connectionString;
	
	private H2DbMode(String connectionString) {
		this.connectionString = connectionString;
	}

	public String getConnectionString() {
		return connectionString;
	}
}
