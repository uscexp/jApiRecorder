package com.github.uscexp.apirecorder.latencysimulation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LatencyConfigurationTest {

	@Test
	public void testHashCode() throws Exception {
		LatencyConfiguration latencyConfiguration = new LatencyConfiguration(1);
		LatencyConfiguration latencyConfiguration1 = new LatencyConfiguration(1);
		
		assertEquals(latencyConfiguration.hashCode(), latencyConfiguration1.hashCode());

		latencyConfiguration = new LatencyConfiguration(1, 2, true);
		latencyConfiguration1 = new LatencyConfiguration(1, 2, true);
		
		assertEquals(latencyConfiguration.hashCode(), latencyConfiguration1.hashCode());
	}

	@Test
	public void testEquals() throws Exception {
		LatencyConfiguration latencyConfiguration = new LatencyConfiguration(1);
		LatencyConfiguration latencyConfiguration1 = new LatencyConfiguration(1);
		
		assertFalse(latencyConfiguration.equals(null));
		assertTrue(latencyConfiguration.equals(latencyConfiguration));
		assertTrue(latencyConfiguration.equals(latencyConfiguration1));

		latencyConfiguration = new LatencyConfiguration(1, 2, true);
		latencyConfiguration1 = new LatencyConfiguration(1, 2, true);
		
		assertTrue(latencyConfiguration.equals(latencyConfiguration1));

		latencyConfiguration = new LatencyConfiguration(1);
		latencyConfiguration1 = new LatencyConfiguration(1, 2, true);
		
		assertFalse(latencyConfiguration.equals(latencyConfiguration1));
		assertFalse(latencyConfiguration.equals(new Object()));

		latencyConfiguration = new LatencyConfiguration(1);
		latencyConfiguration1 = new LatencyConfiguration(2);
		
		assertFalse(latencyConfiguration.equals(latencyConfiguration1));

		latencyConfiguration = new LatencyConfiguration(0, 2, true);
		latencyConfiguration1 = new LatencyConfiguration(1, 2, true);
		
		assertFalse(latencyConfiguration.equals(latencyConfiguration1));

		latencyConfiguration = new LatencyConfiguration(1, 3, true);
		latencyConfiguration1 = new LatencyConfiguration(1, 2, true);
		
		assertFalse(latencyConfiguration.equals(latencyConfiguration1));

		latencyConfiguration = new LatencyConfiguration(1, 2, false);
		latencyConfiguration1 = new LatencyConfiguration(1, 2, true);
		
		assertFalse(latencyConfiguration.equals(latencyConfiguration1));
	}

}
