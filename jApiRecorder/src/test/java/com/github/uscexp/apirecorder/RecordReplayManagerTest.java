/*
 * Copyright (C) 2014 - 2018 by haui - all rights reserved
 */
package com.github.uscexp.apirecorder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;

import com.github.uscexp.apirecorder.attributereplacement.ReplacementConfiguration;
import com.github.uscexp.apirecorder.attributereplacement.replacementvalues.ReplacementValue;
import com.github.uscexp.apirecorder.attributereplacement.replacementvalues.ReplacementValueFactory;
import com.github.uscexp.apirecorder.contenttypestrategy.ContentTypeStrategy;
import com.github.uscexp.apirecorder.contenttypestrategy.XStreamContentTypeStrategy;
import com.github.uscexp.apirecorder.latencysimulation.LatencyConfiguration;
import com.github.uscexp.apirecorder.latencysimulation.LatencyData;
import com.github.uscexp.apirecorder.latencysimulation.Trace;
import com.github.uscexp.apirecorder.readwritestrategy.H2ReadWriteStrategy;
import com.github.uscexp.apirecorder.readwritestrategy.ReadWriteStrategy;

/**
 * @author haui
 *
 */
public class RecordReplayManagerTest extends JUnitBase {

    @Test
	public void testRPOnlineConstructorNoArgs()
		throws Exception {
		ContentTypeStrategy contentTypeStrategy = new XStreamContentTypeStrategy();
		ReadWriteStrategy readWriteStrategy = new H2ReadWriteStrategy();
		RecordReplayConfiguration recordReplayConfiguration = new RecordReplayConfiguration(false);
		recordReplayConfiguration.addArgumentIndices4PrimaryKey("simpleMethod", 0, 1);
		TestClass testClass = (TestClass) RecordReplayManager.newInstance(TestClass.class, RecordReplayMode.RP_ONLINE,
				contentTypeStrategy, readWriteStrategy, recordReplayConfiguration);

		Date date = new Date();
		String result = testClass.simpleMethod(2, "text2", date);

		assertEquals("" + 2 + "text2" + date.toString() + "intern", result);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testRPOnlineConstructorWithArgs()
		throws Exception {
		ContentTypeStrategy contentTypeStrategy = new XStreamContentTypeStrategy();
		ReadWriteStrategy readWriteStrategy = new H2ReadWriteStrategy();
		RecordReplayConfiguration recordReplayConfiguration = new RecordReplayConfiguration(false);
		recordReplayConfiguration.addArgumentIndices4PrimaryKey("simpleMethod", 0, 1);
		Class<? extends Object>[] parameterTypes = new Class[1];
		parameterTypes[0] = String.class;
		Object[] args = { "extern" };
		TestClass testClass = (TestClass) RecordReplayManager.newInstance(TestClass.class, parameterTypes, args,
				RecordReplayMode.RP_ONLINE, contentTypeStrategy, readWriteStrategy, recordReplayConfiguration);

		Date date = new Date();
		String result = testClass.simpleMethod(1, "text1", date);

		assertEquals("" + 1 + "text1" + date.toString() + args[0], result);
	}

	@Test
	public void testRPOffline()
		throws Exception {
		ContentTypeStrategy contentTypeStrategy = new XStreamContentTypeStrategy();
		ReadWriteStrategy readWriteStrategy = new H2ReadWriteStrategy();
		RecordReplayConfiguration recordReplayConfiguration = new RecordReplayConfiguration(false);
		recordReplayConfiguration.addArgumentIndices4PrimaryKey("simpleMethod", 0, 1);
		TestClass testClass = (TestClass) RecordReplayManager.newInstance(TestClass.class, RecordReplayMode.RP_OFFLINE,
				contentTypeStrategy, readWriteStrategy, recordReplayConfiguration);

		Date date = new Date();
		String result = testClass.simpleMethod(4, "text4", date);

		assertNull(result);
	}

	@Test
	public void testRecordRPOffline()
		throws Exception {
		ContentTypeStrategy contentTypeStrategy = new XStreamContentTypeStrategy();
		ReadWriteStrategy readWriteStrategy = new H2ReadWriteStrategy();
		RecordReplayConfiguration recordReplayConfiguration = new RecordReplayConfiguration(false);
		recordReplayConfiguration.addArgumentIndices4PrimaryKey("simpleMethod", 0, 1);
		TestClass testClass = (TestClass) RecordReplayManager.newInstance(TestClass.class, RecordReplayMode.RECORD, contentTypeStrategy,
				readWriteStrategy, recordReplayConfiguration);

		Date date = new Date();
		int i = 3;
		String result = testClass.simpleMethod(i, "text3", date);

		assertEquals("" + i + "text3" + date.toString() + "intern", result);

		testClass = (TestClass) RecordReplayManager.newInstance(TestClass.class, RecordReplayMode.RP_OFFLINE, contentTypeStrategy,
				readWriteStrategy, recordReplayConfiguration);

		Date date2 = new Date();
		result = testClass.simpleMethod(3, "text3", date2);

		assertEquals("" + i + "text3" + date.toString() + "intern", result);
	}

	@Test
	public void testRPOnlineWithLatencySimulation()
		throws Exception {
		ContentTypeStrategy contentTypeStrategy = new XStreamContentTypeStrategy();
		ReadWriteStrategy readWriteStrategy = new H2ReadWriteStrategy();
		LatencyConfiguration latencyConfiguration = new LatencyConfiguration(1, 3, false);
		RecordReplayConfiguration recordReplayConfiguration = new RecordReplayConfiguration(latencyConfiguration , true);
		recordReplayConfiguration.addArgumentIndices4PrimaryKey("simpleMethod", 0);
		TestClass testClass = (TestClass) RecordReplayManager.newInstance(TestClass.class, RecordReplayMode.RP_ONLINE, contentTypeStrategy,
				readWriteStrategy, recordReplayConfiguration);

		Date date = new Date();
		String text = "text3";
		int i = 8;
		String result = testClass.simpleMethod(i, text, date);
		
		int id = new Integer(i).hashCode() * 31;
		LatencyData latencyData = readWriteStrategy.readLatency(id);

		assertEquals("" + i + text + date.toString() + "intern", result);
		int latency = RecordInformation.calculateLatency(latencyConfiguration, latencyData);
		assertTrue(latency >= TestClass.getMethodLatency() * 2);
		
		result = testClass.simpleMethod(i, text, date);
		
		latencyData = readWriteStrategy.readLatency(id);

		assertTrue(latency > RecordInformation.calculateLatency(latencyConfiguration, latencyData));
		latency = RecordInformation.calculateLatency(latencyConfiguration, latencyData);
		assertTrue(latency >= TestClass.getMethodLatency());
		assertTrue(latency < TestClass.getMethodLatency() * 2);
		
		result = testClass.simpleMethod(i, text, date);
		
		latencyData = readWriteStrategy.readLatency(id);

		latency = RecordInformation.calculateLatency(latencyConfiguration, latencyData);
		assertTrue(latency >= TestClass.getMethodLatency());
		assertTrue(latency < TestClass.getMethodLatency() * 2);
	}

	@Test
	public void testRPOnlineWithLatencySimulationAndRecordAtOnce()
		throws Exception {
		ContentTypeStrategy contentTypeStrategy = new XStreamContentTypeStrategy();
		ReadWriteStrategy readWriteStrategy = new H2ReadWriteStrategy();
		LatencyConfiguration latencyConfiguration = new LatencyConfiguration(1, 3, true);
		RecordReplayConfiguration recordReplayConfiguration = new RecordReplayConfiguration(latencyConfiguration , true);
		recordReplayConfiguration.addArgumentIndices4PrimaryKey("simpleMethod", 0);
		TestClass testClass = (TestClass) RecordReplayManager.newInstance(TestClass.class, RecordReplayMode.RP_ONLINE, contentTypeStrategy,
				readWriteStrategy, recordReplayConfiguration);

		Date date = new Date();
		String text = "text3";
		int i = 9;
		Trace trace = new Trace();
		String result = testClass.simpleMethod(i, text, date);
		int delay = (int) trace.getDuration();
		
		assertTrue(delay >= TestClass.getMethodLatency() * 5);
		
		LatencyData latencyData = readWriteStrategy.readLatency(new Integer(i).hashCode() * 31);

		assertEquals("" + i + text + date.toString() + "intern", result);
		int latency = RecordInformation.calculateLatency(latencyConfiguration, latencyData);
		assertTrue(latency >= TestClass.getMethodLatency());
	}

	@Test
	public void testRPOnlineWithLatencySimulationStaticDeleay()
		throws Exception {
		ContentTypeStrategy contentTypeStrategy = new XStreamContentTypeStrategy();
		ReadWriteStrategy readWriteStrategy = new H2ReadWriteStrategy();
		LatencyConfiguration latencyConfiguration = new LatencyConfiguration(200);
		RecordReplayConfiguration recordReplayConfiguration = new RecordReplayConfiguration(latencyConfiguration , true);
		recordReplayConfiguration.addArgumentIndices4PrimaryKey("simpleMethod", 0);
		TestClass testClass = (TestClass) RecordReplayManager.newInstance(TestClass.class, RecordReplayMode.RP_ONLINE, contentTypeStrategy,
				readWriteStrategy, recordReplayConfiguration);

		Date date = new Date();
		String text = "text3";
		int i = 10;
		Trace trace = new Trace();
		String result = testClass.simpleMethod(i, text, date);
		int delay = (int) trace.getDuration();
		
		assertTrue(delay > 200);
		
		LatencyData latencyData = readWriteStrategy.readLatency(new Integer(i).hashCode() * 31);

		assertEquals("" + i + text + date.toString() + "intern", result);
		int latency = RecordInformation.calculateLatency(latencyConfiguration, latencyData);
		assertTrue(latency == 200);
	}


	@Test
	public void testRPOnlineWithoutLatencyConfigurationButLatencySimulationActivated()
		throws Exception {
		ContentTypeStrategy contentTypeStrategy = new XStreamContentTypeStrategy();
		ReadWriteStrategy readWriteStrategy = new H2ReadWriteStrategy();
		RecordReplayConfiguration recordReplayConfiguration = new RecordReplayConfiguration(true);
		recordReplayConfiguration.addArgumentIndices4PrimaryKey("simpleMethod", 0);
		TestClass testClass = (TestClass) RecordReplayManager.newInstance(TestClass.class, RecordReplayMode.RP_ONLINE, contentTypeStrategy,
				readWriteStrategy, recordReplayConfiguration);

		Date date = new Date();
		String text = "text3";
		int i = 11;
		testClass.simpleMethod(i, text, date);
		
		LatencyData latencyData = readWriteStrategy.readLatency(new Integer(i).hashCode() * 31);

		LatencyConfiguration latencyConfiguration = recordReplayConfiguration.getLatencyConfiguration("simpleMethod");
		int latency = RecordInformation.calculateLatency(latencyConfiguration, latencyData);
		assertTrue(latency == 0);
	}

	@Test
	public void testRecordTwice()
		throws Exception {
		ContentTypeStrategy contentTypeStrategy = new XStreamContentTypeStrategy();
		ReadWriteStrategy readWriteStrategy = new H2ReadWriteStrategy();
		RecordReplayConfiguration recordReplayConfiguration = new RecordReplayConfiguration(false);
		recordReplayConfiguration.addArgumentIndices4PrimaryKey("simpleMethod", 0, 1);
		TestClass testClass = (TestClass) RecordReplayManager.newInstance(TestClass.class, RecordReplayMode.RECORD, contentTypeStrategy,
				readWriteStrategy, recordReplayConfiguration);

		Date date = new Date();
		int i = 7;
		String text = "text3";
		String result = testClass.simpleMethod(i, text, date);

		assertEquals("" + i + text + date.toString() + "intern", result);

		result = testClass.simpleMethod(i, text, date);

		assertEquals("" + i + text + date.toString() + "intern", result);
	}

    @Test
    public void testRecordRPOfflineWithAttributeReplacement()
        throws Exception {
        ContentTypeStrategy contentTypeStrategy = new XStreamContentTypeStrategy();
        ReadWriteStrategy readWriteStrategy = new H2ReadWriteStrategy();
        RecordReplayConfiguration recordReplayConfiguration = new RecordReplayConfiguration(false);
        recordReplayConfiguration.addArgumentIndices4PrimaryKey("simpleLevel2Method", 0, 1);
        TestClass testClass = (TestClass) RecordReplayManager.newInstance(TestClass.class, RecordReplayMode.RECORD, contentTypeStrategy,
                readWriteStrategy, recordReplayConfiguration);

        Date date = new Date();
        String text = "extern";
        TestClass result = testClass.simpleLevel2Method(3, text, date);

        assertEquals(text, result.getText());

        String value = "post_extern";
        ReplacementValue replacementValue = ReplacementValueFactory.createReplacementGivenValue(value);
        ReplacementConfiguration replacementConfiguration = new ReplacementConfiguration("text", replacementValue);
        recordReplayConfiguration.addReplacementConfiguration("simpleLevel2Method", replacementConfiguration );
        testClass = (TestClass) RecordReplayManager.newInstance(TestClass.class, RecordReplayMode.RP_OFFLINE, contentTypeStrategy,
                readWriteStrategy, recordReplayConfiguration);

        Date date2 = new Date();
        result = testClass.simpleLevel2Method(3, text, date2);

        assertEquals(value, result.getText());
    }

	@Test
	public void testForeward()
		throws Exception {
		ContentTypeStrategy contentTypeStrategy = new XStreamContentTypeStrategy();
		ReadWriteStrategy readWriteStrategy = new H2ReadWriteStrategy();
		RecordReplayConfiguration recordReplayConfiguration = new RecordReplayConfiguration(false);
		recordReplayConfiguration.addArgumentIndices4PrimaryKey("simpleMethod", 0, 1);
		TestClass testClass = (TestClass) RecordReplayManager.newInstance(TestClass.class, RecordReplayMode.FOREWARD,
				contentTypeStrategy, readWriteStrategy, recordReplayConfiguration);

		Date date = new Date();
		int i = 5;
		String text = "text7";
		String result = testClass.simpleMethod(i, text, date);

		assertEquals("" + i + text + date.toString() + "intern", result);

		testClass = (TestClass) RecordReplayManager.newInstance(TestClass.class, RecordReplayMode.RP_OFFLINE, contentTypeStrategy,
				readWriteStrategy, recordReplayConfiguration);

		Date date2 = new Date();
		result = testClass.simpleMethod(i, text, date2);

		assertNull(result);
	}

	@Test
	public void testRecordRPOfflineConfigNo()
		throws Exception {
		ContentTypeStrategy contentTypeStrategy = new XStreamContentTypeStrategy();
		ReadWriteStrategy readWriteStrategy = new H2ReadWriteStrategy();

		TestClass testClass = (TestClass) RecordReplayManager.newInstance(TestClass.class, RecordReplayMode.RECORD, contentTypeStrategy,
				readWriteStrategy, null);

		Date date = new Date();
		String result = testClass.simpleMethod(6, "text6", date);

		assertEquals("" + 6 + "text6" + date.toString() + "intern", result);

		testClass = (TestClass) RecordReplayManager.newInstance(TestClass.class, RecordReplayMode.RP_OFFLINE, contentTypeStrategy,
				readWriteStrategy, null);

		result = testClass.simpleMethod(6, "text6", date);

		assertEquals("" + 6 + "text6" + date.toString() + "intern", result);
	}
}
