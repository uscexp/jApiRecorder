/*
 * Copyright (C) 2014 by haui - all rights reserved
 */
package com.github.uscexp.apirecorder;

import java.util.Date;

/**
 * @author haui
 *
 */
public class TestClass {
    
    private String text = "intern";
    private TestClass testClass;
    
    public TestClass() {
        
    }
    
    public TestClass(String text) {
        this.text = text;
    }

    public String simpleMethod(int i, String text, Date date) {
        return "" + i + text + date.toString() + this.text;
    }
    
    public TestClass simpleLevel2Method(int i, String text, Date date) {
        this.testClass = new TestClass(text);
        return testClass;
    }

    public String getText() {
        return text;
    }
}
