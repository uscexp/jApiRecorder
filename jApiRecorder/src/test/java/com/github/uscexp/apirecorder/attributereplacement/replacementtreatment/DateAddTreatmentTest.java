/*
 * Copyright (C) 2014 by haui - all rights reserved
 */
package com.github.uscexp.apirecorder.attributereplacement.replacementtreatment;

import java.util.Calendar;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import com.github.uscexp.apirecorder.exception.ReplacementValueTreatmentException;

/**
 * @author haui
 *
 */
public class DateAddTreatmentTest {

	private DateAddTreatment dateAddTreatmentSUTreatment;

	@Test
	public void testTreatReplacementValue()
		throws Exception {
		dateAddTreatmentSUTreatment = new DateAddTreatment(false, Calendar.YEAR, 1);
		Date date = new Date();

		Object result = dateAddTreatmentSUTreatment.treatReplacementValue(date);

		Calendar resultCalendar = Calendar.getInstance();
		resultCalendar.setTime((Date) result);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.YEAR, 1);

		Assert.assertNotNull(result);
		Assert.assertEquals(calendar, resultCalendar);
	}

	@Test
	public void testTreatReplacementValueTodayStartPoint()
		throws Exception {
		dateAddTreatmentSUTreatment = new DateAddTreatment(true, Calendar.MONTH, 0);
		Date date = new Date();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, -1);

		Object result = dateAddTreatmentSUTreatment.treatReplacementValue(calendar.getTime());

		Calendar resultCalendar = Calendar.getInstance();
		resultCalendar.setTime((Date) result);

		Calendar expectedCalendar = Calendar.getInstance();
		expectedCalendar.setTime(date);

		Assert.assertNotNull(result);
		Assert.assertEquals(expectedCalendar, resultCalendar);
	}

	@Test(expected = ReplacementValueTreatmentException.class)
	public void testTreatReplacementValueError()
		throws Exception {
		dateAddTreatmentSUTreatment = new DateAddTreatment(false, Calendar.YEAR, 1);

		dateAddTreatmentSUTreatment.treatReplacementValue(null);
	}
}
