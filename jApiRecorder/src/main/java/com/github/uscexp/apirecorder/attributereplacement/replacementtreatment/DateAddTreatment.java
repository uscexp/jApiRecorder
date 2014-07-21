/*
 * Copyright (C) 2014 by haui - all rights reserved
 */
package com.github.uscexp.apirecorder.attributereplacement.replacementtreatment;

import java.util.Calendar;
import java.util.Date;

import com.github.uscexp.apirecorder.exception.ReplacementValueTreatmentException;
import com.google.common.base.Preconditions;

/**
 * The {@link DateAddTreatment} can manipulate {@link Date} values
 * (add a specific amount to a specified {@link Calendar} field).
 * One can also specify if the start point of the manipulation should
 * be today or leave the value as is.
 * 
 * @author haui
 *
 */
public class DateAddTreatment implements ReplacementValueTreatment {

	private boolean startPointToday;
	private int dateField;
	private int valueToAdd;

	/**
	 * With this treatment one can add a given amout to an {@link Calendar} field.
	 *
	 * @param startPointToday take today as starting point (only DAY, MONTH and YEAR).
	 * @param dateField the {@link Calendar} field e.g {@link Calendar#MONTH} for changing the month.
	 * @param valueToAdd value to add to the given field.
	 */
	public DateAddTreatment(boolean startPointToday, int dateField, int valueToAdd) {
		super();
		this.startPointToday = startPointToday;
		this.dateField = dateField;
		this.valueToAdd = valueToAdd;
	}

	@Override
	public Object treatReplacementValue(Object value)
		throws ReplacementValueTreatmentException {
		Object result = null;
		try {
			Preconditions.checkNotNull(value);
			Preconditions.checkArgument(value instanceof Date, String.format("Parameter value has to be of type %s", Date.class.getName()));
			Date valueToTreat = (Date) value;
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(valueToTreat);
			if (startPointToday) {
				Calendar today = Calendar.getInstance();
				calendar.set(Calendar.DAY_OF_MONTH, today.get(Calendar.DAY_OF_MONTH));
				calendar.set(Calendar.MONTH, today.get(Calendar.MONTH));
				calendar.set(Calendar.YEAR, today.get(Calendar.YEAR));
			}
			calendar.add(dateField, valueToAdd);
			result = calendar.getTime();
		} catch (Exception e) {
			throw new ReplacementValueTreatmentException("Error during change date treatment.", e);
		}
		return result;
	}

}
