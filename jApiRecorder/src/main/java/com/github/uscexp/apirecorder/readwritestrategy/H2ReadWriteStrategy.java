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
import com.github.uscexp.apirecorder.latencysimulation.LatencyData;

/**
 * @author haui
 *
 */
public class H2ReadWriteStrategy implements ReadWriteStrategy {
	private static final String TABLE = "RECORDED_VALUES";
	private static final String LATENCY_TABLE = "LATENCY_VALUES";
	private static final String LATENCY_DATA_TABLE = "LATENCY_DATA";

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

	private void createDB()
		throws SQLException {
		Statement stmt = conn.createStatement();

		String createQ = "CREATE TABLE IF NOT EXISTS " + TABLE + "(ID LONG PRIMARY KEY NOT NULL, RECORDED_VALUE CLOB)";
		stmt.executeUpdate(createQ);

		createQ = "CREATE TABLE IF NOT EXISTS " + LATENCY_DATA_TABLE + "(ID LONG PRIMARY KEY NOT NULL, CYCLES INTEGER)";
		stmt.executeUpdate(createQ);

		createQ = "CREATE TABLE IF NOT EXISTS " + LATENCY_TABLE + "(FK_LATENCY LONG NOT NULL, LATENCY_VALUE INTEGER)";
		stmt.executeUpdate(createQ);
	}

	public H2DbMode getH2DbMode() {
		return h2DbMode;
	}

	public String getPath() {
		return path;
	}

	@Override
	public void write(long id, String serializedObject)
		throws ReadWriteStrategyException {
		try {
			String insertSql = "MERGE INTO " + TABLE + " KEY(ID) VALUES(?,?)";

			PreparedStatement statement = conn.prepareStatement(insertSql);
			statement.setLong(1, id);
			statement.setString(2, serializedObject);

			statement.executeUpdate();
		} catch (Exception e) {
			throw new ReadWriteStrategyException(String.format("Error writing object %s", serializedObject), e);
		}
	}

	@Override
	public String read(long id)
		throws ReadWriteStrategyException {
		String result = null;
		try {
			String selectSql = "SELECT ID, RECORDED_VALUE FROM " + TABLE + " WHERE ID = ?";

			PreparedStatement statement = conn.prepareStatement(selectSql);
			statement.setLong(1, id);

			ResultSet resultSet = statement.executeQuery();

			if (resultSet.first()) {
				result = resultSet.getString(2);
			}
			if (resultSet.next()) {
				throw new ReadWriteStrategyException(String.format("There is more the one object with id %d", id));
			}
		} catch (Exception e) {
			throw new ReadWriteStrategyException(String.format("Error reading object with id %d", id), e);
		}
		return result;
	}

	@Override
	public void close()
		throws ReadWriteStrategyException {
		try {
			if ((conn != null) && !conn.isClosed()) {
				conn.close();
			}
		} catch (Exception e) {
			throw new ReadWriteStrategyException("Error closing strategy resources", e);
		}
	}

	@Override
	public void writeLatency(long id, LatencyData latencyData)
		throws ReadWriteStrategyException {
		try {
			conn.setAutoCommit(false);
			String insertSql = "MERGE INTO " + LATENCY_DATA_TABLE + " KEY(ID) VALUES(?,?)";

			PreparedStatement statement = conn.prepareStatement(insertSql);
			statement.setLong(1, id);
			statement.setInt(2, latencyData.getNumberOfCyclesIgnored());

			statement.executeUpdate();

			insertSql = "DELETE " + LATENCY_TABLE + " WHERE FK_LATENCY = ?";

			statement = conn.prepareStatement(insertSql);
			statement.setLong(1, id);

			statement.executeUpdate();

			insertSql = "INSERT INTO " + LATENCY_TABLE + " VALUES(?,?)";
			for (Integer value : latencyData.getLatencies()) {
				statement = conn.prepareStatement(insertSql);
				statement.setLong(1, id);
				statement.setInt(2, value);

				statement.executeUpdate();
			}
			conn.commit();
		} catch (Exception e) {
	        if (conn != null) {
	            try {
	                conn.rollback();
	            } catch(SQLException excep) {
	                e = new ReadWriteStrategyException("Error writing on rollback!", e);
	            }
	        }
			throw new ReadWriteStrategyException(String.format("Error writing latency %s", latencyData.toString()), e);
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				throw new ReadWriteStrategyException("Error setting autocommit to true!", e);
			}
		}
	}

	@Override
	public LatencyData readLatency(long id)
		throws ReadWriteStrategyException {
		LatencyData result = null;
		try {
			String selectSql = "SELECT lt.ID, lt.CYCLES, ld.LATENCY_VALUE FROM " + LATENCY_DATA_TABLE + " lt JOIN " + LATENCY_TABLE +
				" ld ON lt.ID = ld.FK_LATENCY WHERE lt.ID = ?";

			PreparedStatement statement = conn.prepareStatement(selectSql);
			statement.setLong(1, id);

			ResultSet resultSet = statement.executeQuery();

			result = new LatencyData();

			if (resultSet.next()) {
				result.setNumberOfCyclesIgnored(resultSet.getInt(2));
				result.getLatencies().add(resultSet.getInt(3));
			}
			while (resultSet.next()) {
				result.getLatencies().add(resultSet.getInt(3));
			}
		} catch (Exception e) {
			throw new ReadWriteStrategyException(String.format("Error reading latency with id %d", id), e);
		}
		return result;
	}
}
