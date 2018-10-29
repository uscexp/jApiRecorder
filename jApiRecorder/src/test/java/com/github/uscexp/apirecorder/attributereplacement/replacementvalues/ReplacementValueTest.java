/*
 * Copyright (C) 2014 - 2018 by haui - all rights reserved
 */
package com.github.uscexp.apirecorder.attributereplacement.replacementvalues;

import java.util.Calendar;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.uscexp.apirecorder.RecordInformation;
import com.github.uscexp.apirecorder.attributereplacement.replacementtreatment.DateAddTreatment;
import com.github.uscexp.apirecorder.attributereplacement.replacementtreatment.ReplacementValueTreatment;
import com.github.uscexp.apirecorder.exception.ReplacementValueException;

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
	public void testGetReplacementGivenValue()
		throws Exception {
		String value = "test";
		replacementValueSUT = ReplacementValueFactory.createReplacementGivenValue(value);

		Object result = replacementValueSUT.getReplacementValue(recordInformation);

		Assert.assertEquals(value, result);
	}

	@Test
	public void testGetReplacementArgumentValue()
		throws Exception {
		int index = 0;
		replacementValueSUT = ReplacementValueFactory.createReplacementArgumentValue(index);

		Object result = replacementValueSUT.getReplacementValue(recordInformation);

		Assert.assertEquals(recordInformation.getArgs()[0], result);
	}

	@Test
	public void testGetTreatedReplacementGivenValue()
		throws Exception {
		Calendar calendar = Calendar.getInstance();
		Date value = calendar.getTime();
		ReplacementValueTreatment replacementValueTreatment1 = new DateAddTreatment(false, Calendar.DAY_OF_MONTH, 1);
		ReplacementValueTreatment replacementValueTreatment2 = new DateAddTreatment(false, Calendar.MONTH, 1);
		replacementValueSUT = ReplacementValueFactory.createTreatedReplacementGivenValue(value, replacementValueTreatment1,
				replacementValueTreatment2);
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.MONTH, 1);
		Date expectedValue = calendar.getTime();

		Object result = replacementValueSUT.getReplacementValue(recordInformation);

		Assert.assertEquals(expectedValue, result);
	}

	@Test
	public void testGetTreatedReplacementArgumentValue()
		throws Exception {
		int index = 0;
		Calendar calendar = Calendar.getInstance();
		Date value = (Date) recordInformation.getArgs()[0];
		ReplacementValueTreatment replacementValueTreatment1 = new DateAddTreatment(false, Calendar.DAY_OF_MONTH, 1);
		ReplacementValueTreatment replacementValueTreatment2 = new DateAddTreatment(false, Calendar.MONTH, 1);
		replacementValueSUT = ReplacementValueFactory.createTreatedReplacementArgumentValue(index, replacementValueTreatment1,
				replacementValueTreatment2);
		calendar.setTime(value);
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.MONTH, 1);
		Date expectedValue = calendar.getTime();

		Object result = replacementValueSUT.getReplacementValue(recordInformation);

		Assert.assertEquals(expectedValue, result);
	}

	@Test(expected = ReplacementValueException.class)
	public void testGetTreatedReplacementGivenValueError()
		throws Exception {
		String value = "test";
		ReplacementValueTreatment replacementValueTreatment1 = new DateAddTreatment(false, Calendar.DAY_OF_MONTH, 1);
		ReplacementValueTreatment replacementValueTreatment2 = new DateAddTreatment(false, Calendar.MONTH, 1);
		replacementValueSUT = ReplacementValueFactory.createTreatedReplacementGivenValue(value, replacementValueTreatment1,
				replacementValueTreatment2);

		replacementValueSUT.getReplacementValue(recordInformation);
	}
}
