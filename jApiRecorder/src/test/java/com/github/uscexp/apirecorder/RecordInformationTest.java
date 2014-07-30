/*
 * Copyright (C) 2014 by haui - all rights reserved
 */
package com.github.uscexp.apirecorder;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.github.uscexp.apirecorder.latencysimulation.LatencyConfiguration;
import com.github.uscexp.apirecorder.latencysimulation.LatencyData;
import com.github.uscexp.apirecorder.readwritestrategy.H2ReadWriteStrategy;
import com.github.uscexp.apirecorder.readwritestrategy.ReadWriteStrategy;

/**
 * @author haui
 *
 */
public class RecordInformationTest extends JUnitBase {

    @Test
    public void testRecordInformation() throws Exception {
        Object[] args = { "a", "b" };
        int[] argIdx4Pk = { 0 };
        RecordInformation recordInformation = new RecordInformation("test", args, argIdx4Pk);
        
        assertArrayEquals(args, recordInformation.getArgs());
        assertEquals("test", recordInformation.getMethodName());
        assertEquals(args[0].hashCode() * 31, recordInformation.getReturnValueId());
    }

    @Test
    public void testRecordInformationWithConfig() throws Exception {
        Object[] args = { "a", "b" };
		RecordReplayConfiguration recordReplayConfiguration = new RecordReplayConfiguration(true);
		recordReplayConfiguration.addArgumentIndices4PrimaryKey("test", 0);
        ReadWriteStrategy readWriteStrategy = new H2ReadWriteStrategy();
		RecordInformation recordInformation = new RecordInformation("test", args, recordReplayConfiguration, readWriteStrategy );
        
        assertArrayEquals(args, recordInformation.getArgs());
        assertEquals("test", recordInformation.getMethodName());
        assertEquals(args[0].hashCode() * 31, recordInformation.getReturnValueId());
        assertNotNull(recordInformation.getLatencyData());
    }


    @Test
    public void testRecordInformationWithConfigAndExistingLatencyData() throws Exception {
        Object[] args = { "a", "b" };
        LatencyConfiguration latencyConfiguration = new LatencyConfiguration(1);
		RecordReplayConfiguration recordReplayConfiguration = new RecordReplayConfiguration(latencyConfiguration , true);
		recordReplayConfiguration.addArgumentIndices4PrimaryKey("test", 0);
        ReadWriteStrategy readWriteStrategy = new H2ReadWriteStrategy();
        LatencyData latencyData = new LatencyData();
        latencyData.setNumberOfCyclesIgnored(1);
        latencyData.getLatencies().add(1);
        readWriteStrategy.writeLatency(args[0].hashCode() * 31, latencyData);
		RecordInformation recordInformation = new RecordInformation("test", args, recordReplayConfiguration, readWriteStrategy );
        
        assertArrayEquals(args, recordInformation.getArgs());
        assertEquals("test", recordInformation.getMethodName());
        assertEquals(args[0].hashCode() * 31, recordInformation.getReturnValueId());
        assertNotNull(recordInformation.getLatencyData());
        assertEquals(latencyData, recordInformation.getLatencyData());
    }
    @Test
    public void testGetReturnValue() throws Exception {
        Object[] args = { "a", "b" };
        int[] argIdx4Pk = { 0 };
        RecordInformation recordInformation = new RecordInformation("test", args, argIdx4Pk);

        recordInformation.setReturnValue("test");
        
        Object result = recordInformation.getReturnValue();
        
        assertEquals("test", result);
    }

    @Test
    public void testGetReturnValueId() throws Exception {
        Object[] args = { "a", "b" };
        int[] argIdx4Pk = {};
        RecordInformation recordInformation = new RecordInformation("test", args, argIdx4Pk);

        recordInformation.setReturnValue("test");
        
        long result = recordInformation.getReturnValueId();
       
        long current = 0;
        for (int i = 0; i < args.length; i++) {
            current += args[i].hashCode() * 31;
        }
        assertEquals(current, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParameterValidationMethod() {
        Object[] args = { "a", "b" };
        int[] argIdx4Pk = { 0 };
        new RecordInformation(null, args, argIdx4Pk);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParameterValidationArgs() {
        Object[] args = null;
        int[] argIdx4Pk = { 0 };
        new RecordInformation("test", args, argIdx4Pk);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParameterValidationArgs2() {
        Object[] args = new Object[0];
        int[] argIdx4Pk = { 0 };
        new RecordInformation("test", args, argIdx4Pk);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParameterValidationArgs3() {
        Object[] args = { "a" };
        int[] argIdx4Pk = { 0, 0 };
        new RecordInformation("test", args, argIdx4Pk);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParameterValidationArgIdx4Pk3() {
        Object[] args = { "a", "b" };
        int[] argIdx4Pk = { -1 };
        new RecordInformation("test", args, argIdx4Pk);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParameterValidationArgIdx4Pk4() {
        Object[] args = { "a", "b" };
        int[] argIdx4Pk = { 3 };
        new RecordInformation("test", args, argIdx4Pk);
    }
}
