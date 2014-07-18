/*
 * Copyright (C) 2014 by haui - all rights reserved
 */
package com.github.uscexp.apirecorder.attributereplacement.replacementvalues;

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.uscexp.apirecorder.RecordInformation;

/**
 * @author haui
 *
 */
public class ReplacementValueTest {
    
    private ReplacementValue replacementValueSUT;
    
    private RecordInformation recordInformation;
    
    @Before
    public void setup() {
        int[] argIdx4Pk = { 0 };
        Object[] args = new Date[1];
        args[0] = new Date();
        recordInformation = new RecordInformation("testMethod", args, argIdx4Pk);
    }

    @Test
    public void testGetReplacementValueGivenValue() throws Exception {
        String value = "test";
        replacementValueSUT = ReplacementValueFactory.createReplacementGivenValue(value);
        
        Object result = replacementValueSUT.getReplacementValue(recordInformation);
        
        Assert.assertEquals(value, result);
    }

}
