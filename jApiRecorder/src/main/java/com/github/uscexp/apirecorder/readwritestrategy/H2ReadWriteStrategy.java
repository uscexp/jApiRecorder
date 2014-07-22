/*
 * Copyright (C) 2014 by haui - all rights reserved
 */
package com.github.uscexp.apirecorder.readwritestrategy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.github.uscexp.apirecorder.exception.ReadWriteStrategyException;

/**
 * @author haui
 *
 */
public class H2ReadWriteStrategy implements ReadWriteStrategy {
	private static final String TABLE = "RECORDED_VALUES";

	private Connection conn;
	private H2DbMode h2DbMode;
	private String path;

	/**
	 * Create {@link H2ReadWriteStrategy} with in memory DB.
	 */
	public H2ReadWriteStrategy()
		throws ClassNotFoundException, SQLException {
		this(H2DbMode.MEMORY, "");
	}

	/**
	 * Create {@link H2ReadWriteStrategy} with file DB.
	 *
	 * @param path path of the DB file e.g. ~/.db/h2db
	 */
	public H2ReadWriteStrategy(String path)
		throws ClassNotFoundException, SQLException {
		this(H2DbMode.FILE, path);
	}

	private H2ReadWriteStrategy(H2DbMode h2DbMode, String path)
		throws ClassNotFoundException, SQLException {
		Class.forName("org.h2.Driver");
		
		this.h2DbMode = h2DbMode;
		this.path = path;

		conn = DriverManager.getConnection(h2DbMode.getConnectionString() + path, "", "");
		
		createDB();
	}

	private void createDB() throws SQLException {
        Statement stmt = conn.createStatement();
        
        String createQ = "CREATE TABLE IF NOT EXISTS "
                + TABLE
                + "(ID INT PRIMARY KEY NOT NULL, RECORDED_VALUE CLOB)";
        stmt.executeUpdate(createQ);
	}
	
	public H2DbMode getH2DbMode() {
		return h2DbMode;
	}

	public String getPath() {
		return path;
	}

	@Override
	public void write(long id, String serializedObject) throws ReadWriteStrategyException {
		try {
			String insertSql = "INSERT INTO " + TABLE + " VALUES(?,?)";
			
			PreparedStatement statement = conn.prepareStatement(insertSql);
			statement.setLong(1, id);
			statement.setString(2, serializedObject);
			
			statement.executeUpdate();
		} catch (Exception e) {
			throw new ReadWriteStrategyException(String.format("Error writing object %s", serializedObject), e);
		}
	}
	
	@Override
	public String read(long id) throws ReadWriteStrategyException {
		String result = null;
		try {
			String selectSql = "SELECT ID, RECORDED_VALUE FROM " + TABLE + " WHERE ID = ?";
			
			PreparedStatement statement = conn.prepareStatement(selectSql);
			statement.setLong(1, id);
			
			ResultSet resultSet = statement.executeQuery();
			
			if (resultSet.first()) {
				result = resultSet.getString(2);
			}
			if(resultSet.next()) {
				throw new ReadWriteStrategyException(String.format("There is more the one object with id %d", id));
			}
		} catch (Exception e) {
			throw new ReadWriteStrategyException(String.format("Error reading object with id %d", id), e);
		}
		return result;
	}
	
	@Override
	public void close() throws ReadWriteStrategyException {
		try {
			if(conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (Exception e) {
			throw new ReadWriteStrategyException("Error closing strategy resources", e);
		}
	}
}
