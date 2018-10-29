/*
 * Copyright (C) 2014 - 2018 by haui - all rights reserved
 */
package com.github.uscexp.apirecorder;

import java.util.Date;

/**
 * @author haui
 *
 */
public class TestClass {
    
	private int methodCallCount = 0;
    private String text = "intern";
    private TestClass testClass;
    private static final int METHOD_LATENCY = 100;
    
    public TestClass() {
        
    }
    
    public TestClass(String text) {
        this.text = text;
    }

    public String simpleMethod(int i, String text, Date date) {
    	try {
    		Thread.sleep(METHOD_LATENCY);
    		
        	if(methodCallCount == 0)
        		Thread.sleep(METHOD_LATENCY);
        	
        	++methodCallCount;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
        return "" + i + text + date.toString() + this.text;
    }
    
    public TestClass simpleLevel2Method(int i, String text, Date date) {
        this.testClass = new TestClass(text);
        return testClass;
    }

    public String getText() {
        return text;
    }

	public static int getMethodLatency() {
		return METHOD_LATENCY;
	}
}
