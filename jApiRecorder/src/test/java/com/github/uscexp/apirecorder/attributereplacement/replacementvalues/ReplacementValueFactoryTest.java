/*
 * Copyright (C) 2014 by haui - all rights reserved
 */
package com.github.uscexp.apirecorder.attributereplacement.replacementvalues;

import java.lang.reflect.Field;

import org.junit.Assert;
import org.junit.Test;

import com.github.uscexp.apirecorder.attributereplacement.replacementtreatment.DateAddTreatment;
import com.github.uscexp.apirecorder.attributereplacement.replacementtreatment.ReplacementValueTreatment;
import com.github.uscexp.apirecorder.exception.ReplacementValueTreatmentException;

/**
 * @author haui
 *
 */
public class ReplacementValueFactoryTest {

    @Test
    public void testCreateReplacementGivenValue() throws Exception {
        String value = "test";
        ReplacementValue result = ReplacementValueFactory.createReplacementGivenValue(value);
        
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof ReplacementGivenValue);
        Object parentReplacementValue = getReplacementValue(result, "replacementValue");
        Assert.assertTrue(parentReplacementValue instanceof ReplacementValueImpl);
        Object givenValue = getReplacementValue(result, "givenValue");
        Assert.assertEquals(value, givenValue);
    }

    private Object getReplacementValue(Object current, String attribute) throws IllegalAccessException {
        Class<?> clazz = current.getClass();
        Field field = null;
        while (field == null) {
            try {
                field = clazz.getDeclaredField(attribute);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        field.setAccessible(true);
        Object result = field.get(current);
        return result;
    }

    @Test
    public void testCreateReplacementArgumentValue() throws Exception {
        int index = 1;
        ReplacementValue result = ReplacementValueFactory.createReplacementArgumentValue(index);
        
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof ReplacementArgumentValue);
        Object parentReplacementValue = getReplacementValue(result, "replacementValue");
        Assert.assertTrue(parentReplacementValue instanceof ReplacementValueImpl);
        Object argumentIndex = getReplacementValue(result, "argumentIndex");
        Assert.assertEquals(index, argumentIndex);
    }

    @Test
    public void testCreateTreatedReplacementGivenValue() throws Exception {
        String value = "test";
        ReplacementValueTreatment treatment1 = new DateAddTreatment(false, 0, 0);
        ReplacementValueTreatment treatment2 = new NopeTreatment();
        ReplacementValue result = ReplacementValueFactory.createTreatedReplacementGivenValue(value, treatment1, treatment2);
        
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof ReplacementGivenValue);
        Object parentReplacementValue = getReplacementValue(result, "replacementValue");
        Assert.assertTrue(parentReplacementValue instanceof ReplacementCommandTreatedValue);
        Object replacementValueTreatment = getReplacementValue(parentReplacementValue, "replacementValueTreatment");
        Assert.assertTrue(replacementValueTreatment instanceof DateAddTreatment);
        parentReplacementValue = getReplacementValue(parentReplacementValue, "replacementValue");
        Assert.assertTrue(parentReplacementValue instanceof ReplacementCommandTreatedValue);
        replacementValueTreatment = getReplacementValue(parentReplacementValue, "replacementValueTreatment");
        Assert.assertTrue(replacementValueTreatment instanceof NopeTreatment);
        parentReplacementValue = getReplacementValue(parentReplacementValue, "replacementValue");
        Assert.assertTrue(parentReplacementValue instanceof ReplacementValueImpl);
        Object givenValue = getReplacementValue(result, "givenValue");
        Assert.assertEquals(value, givenValue);
    }

    @Test
    public void testCreateTreatedReplacementArgumentValue() throws Exception {
        int index = 1;
        ReplacementValueTreatment treatment1 = new DateAddTreatment(false, 0, 0);
        ReplacementValueTreatment treatment2 = new NopeTreatment();
        ReplacementValue result = ReplacementValueFactory.createTreatedReplacementArgumentValue(index, treatment1, treatment2);
        
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof ReplacementArgumentValue);
        Object parentReplacementValue = getReplacementValue(result, "replacementValue");
        Assert.assertTrue(parentReplacementValue instanceof ReplacementCommandTreatedValue);
        Object replacementValueTreatment = getReplacementValue(parentReplacementValue, "replacementValueTreatment");
        Assert.assertTrue(replacementValueTreatment instanceof DateAddTreatment);
        parentReplacementValue = getReplacementValue(parentReplacementValue, "replacementValue");
        Assert.assertTrue(parentReplacementValue instanceof ReplacementCommandTreatedValue);
        replacementValueTreatment = getReplacementValue(parentReplacementValue, "replacementValueTreatment");
        Assert.assertTrue(replacementValueTreatment instanceof NopeTreatment);
        parentReplacementValue = getReplacementValue(parentReplacementValue, "replacementValue");
        Assert.assertTrue(parentReplacementValue instanceof ReplacementValueImpl);
        Object argumentIndex = getReplacementValue(result, "argumentIndex");
        Assert.assertEquals(index, argumentIndex);
    }

    public class NopeTreatment implements ReplacementValueTreatment {

        @Override
        public Object treatReplacementValue(Object value) throws ReplacementValueTreatmentException {
            return value;
        }
        
    }
}
