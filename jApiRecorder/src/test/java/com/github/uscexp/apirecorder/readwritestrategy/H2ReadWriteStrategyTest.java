/*
 * Copyright (C) 2014 by haui - all rights reserved
 */
package com.github.uscexp.apirecorder.readwritestrategy;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
        
        Assert.assertEquals("bla", result);
        Assert.assertEquals(H2DbMode.MEMORY, h2DbMode);
        Assert.assertEquals("", path);
    }

}
