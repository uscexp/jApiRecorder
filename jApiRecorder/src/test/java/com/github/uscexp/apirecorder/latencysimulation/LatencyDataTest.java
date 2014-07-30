/*
 * Copyright (C) 2014 by haui - all rights reserved
 */
package com.github.uscexp.apirecorder.latencysimulation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * @author haui
 *
 */
public class LatencyDataTest {

	@Test
	public void testHashCode() throws Exception {
		LatencyData latencyData = new LatencyData();
		latencyData.setNumberOfCyclesIgnored(1);
		latencyData.getLatencies().add(1);
		LatencyData latencyData1 = new LatencyData();
		latencyData1.setNumberOfCyclesIgnored(1);
		latencyData1.getLatencies().add(1);
		
		assertEquals(latencyData, latencyData1);;
	}

	@Test
	public void testEquals() throws Exception {
		LatencyData latencyData = new LatencyData();
		latencyData.setNumberOfCyclesIgnored(1);
		latencyData.getLatencies().add(1);
		LatencyData latencyData1 = new LatencyData();
		latencyData1.setNumberOfCyclesIgnored(1);
		latencyData1.getLatencies().add(1);
		
		assertTrue(latencyData.equals(latencyData1));;
		assertTrue(latencyData.equals(latencyData));;
		assertFalse(latencyData.equals(null));;
		assertFalse(latencyData.equals(new Object()));;

		latencyData = new LatencyData();
		latencyData.setNumberOfCyclesIgnored(2);
		latencyData.getLatencies().add(1);
		latencyData1 = new LatencyData();
		latencyData1.setNumberOfCyclesIgnored(1);
		latencyData1.getLatencies().add(1);
		
		assertFalse(latencyData.equals(latencyData1));;

		latencyData = new LatencyData();
		latencyData.setNumberOfCyclesIgnored(1);
		latencyData.getLatencies().add(2);
		latencyData1 = new LatencyData();
		latencyData1.setNumberOfCyclesIgnored(1);
		latencyData1.getLatencies().add(1);
		
		assertFalse(latencyData.equals(latencyData1));;
	}

	@Test
	public void testToString() throws Exception {
		LatencyData latencyData = new LatencyData();
		latencyData.setNumberOfCyclesIgnored(1);
		latencyData.getLatencies().add(1);
		
		assertNotNull(latencyData.toString());
	}

}
