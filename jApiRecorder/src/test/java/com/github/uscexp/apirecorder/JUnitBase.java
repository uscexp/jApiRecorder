/*
 * Copyright (C) 2014 - 2018 by haui - all rights reserved
 */
package com.github.uscexp.apirecorder;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.BeforeClass;

/**
 * @author haui
 *
 */
public class JUnitBase {

    @BeforeClass
    public static void setupBeforeClass() {
    
    	// Get the root logger
    	Logger rootLogger = Logger.getLogger("");
    	for (Handler handler : rootLogger.getHandlers()) {
    
    		// Change log level of default handler(s) of root logger
    		// The paranoid would check that this is the ConsoleHandler ;)
    		handler.setLevel(Level.FINEST);
    	}
    
    	// Set root logger level
    	rootLogger.setLevel(Level.FINEST);
    }

    public JUnitBase() {
        super();
    }

}