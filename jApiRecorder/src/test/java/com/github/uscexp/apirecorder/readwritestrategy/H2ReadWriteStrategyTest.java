/*
 * Copyright (C) 2014 - 2018 by haui - all rights reserved
 */
package com.github.uscexp.apirecorder.readwritestrategy;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.github.uscexp.apirecorder.latencysimulation.LatencyData;

/**
 * @author haui
 *
 */
public class H2ReadWriteStrategyTest {

    private H2ReadWriteStrategy h2ReadWriteStrategySUT;
    
    @Before
    public void setup() throws Exception {
        h2ReadWriteStrategySUT = new H2ReadWriteStrategy();
    }
    
    @Test
    public void testReadWrite() throws Exception {
        h2ReadWriteStrategySUT.write(1, "bla");
        H2DbMode h2DbMode = h2ReadWriteStrategySUT.getH2DbMode();
        String path = h2ReadWriteStrategySUT.getPath();
        String result = h2ReadWriteStrategySUT.read(1);
        h2ReadWriteStrategySUT.close();
        
        assertEquals("bla", result);
        assertEquals(H2DbMode.MEMORY, h2DbMode);
        assertEquals("", path);
    }

    @Test
    public void testReadWriteTwice() throws Exception {
        h2ReadWriteStrategySUT.write(1, "blub");
        h2ReadWriteStrategySUT.write(1, "bla");
        H2DbMode h2DbMode = h2ReadWriteStrategySUT.getH2DbMode();
        String path = h2ReadWriteStrategySUT.getPath();
        String result = h2ReadWriteStrategySUT.read(1);
        h2ReadWriteStrategySUT.close();
        
        assertEquals("bla", result);
        assertEquals(H2DbMode.MEMORY, h2DbMode);
        assertEquals("", path);
    }

	@Test
	public void testReadWriteLatency() throws Exception {
		LatencyData latencyData = new LatencyData();
		latencyData.setNumberOfCyclesIgnored(1);
		latencyData.getLatencies().add(1);
		h2ReadWriteStrategySUT.writeLatency(1, latencyData);
		
		LatencyData result = h2ReadWriteStrategySUT.readLatency(1);
		h2ReadWriteStrategySUT.close();
		
		assertEquals(latencyData, result);
	}

	@Test
	public void testReadWriteTwiceLatency() throws Exception {
		LatencyData latencyData1 = new LatencyData();
		latencyData1.setNumberOfCyclesIgnored(2);
		latencyData1.getLatencies().add(2);
		h2ReadWriteStrategySUT.writeLatency(1, latencyData1);
		LatencyData latencyData = new LatencyData();
		latencyData.setNumberOfCyclesIgnored(1);
		latencyData.getLatencies().add(1);
		h2ReadWriteStrategySUT.writeLatency(1, latencyData);
		
		LatencyData result = h2ReadWriteStrategySUT.readLatency(1);
		h2ReadWriteStrategySUT.close();
		
		assertEquals(latencyData, result);
	}
}
