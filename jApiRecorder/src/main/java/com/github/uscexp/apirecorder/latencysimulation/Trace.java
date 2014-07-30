/*
 * Copyright (C) 2014 by haui - all rights reserved
 */
package com.github.uscexp.apirecorder.latencysimulation;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author haui
 *
 */
public class Trace {

	private static final Logger LOG = Logger.getLogger(Trace.class.getName());

	private long startTime = System.currentTimeMillis();

	private String name;
	private Logger log;

	public Trace(String name, Logger log) {
		this.name = name;
		this.log = log;
	}

	public Trace(String name) {
		this(name, null);
	}

	public Trace() {
		this("", null);
	}

	public void measure(String message) {
		long duration = getDuration();
		startTime = System.currentTimeMillis();
		if (isEnabled()) {
			trace(getName() + message + " executed in " + duration + " (ms)!");
		}
	}

	private final String getName() {
		if (name == null)
			return "";
		return name + ": ";
	}

	private final Logger getLog() {
		if (log == null)
			return LOG;
		return log;
	}

	public void reset() {
		startTime = System.currentTimeMillis();
	}

	public final long getDuration() {
		return System.currentTimeMillis() - startTime;
	}

	protected boolean isEnabled() {
		return getLog().getLevel() == Level.FINE;
	}

	protected void trace(String message) {
		getLog().log(Level.FINE, message);
	}

	public long getStartTime() {
		return startTime;
	}
}
