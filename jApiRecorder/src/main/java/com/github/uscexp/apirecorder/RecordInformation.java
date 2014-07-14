/*
 * Copyright (C) 2014 by haui - all rights reserved
 */
package com.github.uscexp.apirecorder;

import java.util.Arrays;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author haui
 * 
 */
public class RecordInformation {
    private String methodName;
    private Object[] args;

    /** argument indices which are used to build the primary key */
    private int[] argIdx4Pk;
    private Object returnValue;

    public RecordInformation(String methodName, Object[] args, int[] argIdx4Pk) {
        validateArguments(methodName, args, argIdx4Pk);

        this.methodName = methodName;
        this.args = args;
        this.argIdx4Pk = argIdx4Pk;
    }

    private void validateArguments(String methodName, Object[] args, int[] argIdx4Pk) {
        if (methodName == null) {
            throw new IllegalArgumentException(String.format("Parameter %s can't be NULL!", "methodName"));
        }
        if (args == null || args.length <= 0) {
            throw new IllegalArgumentException(String.format("Parameter %s must have at least one value!", "args"));
        }
        if (argIdx4Pk != null) {
            if (args.length < argIdx4Pk.length) {
                throw new IllegalArgumentException(String.format(
                        "Length of array %s can not be larger than length of array %s!", "argIdx4Pk", "args"));
            }
            for (int i = 0; i < argIdx4Pk.length; i++) {
                if (argIdx4Pk[i] < 0) {
                    throw new IllegalArgumentException(String.format("The index %d in array %s can not be negative!",
                            i, "argIdx4Pk"));
                }
                if (argIdx4Pk[i] >= args.length) {
                    throw new IllegalArgumentException(String.format(
                            "The index %d in array %s can not be larger than length of array %s!", i, "argIdx4Pk",
                            "args"));
                }
            }
        }
    }

    public long getReturnValueId() {
        long result = 0;

        if (argIdx4Pk != null && argIdx4Pk.length > 0) {
            for (int i = 0; i < argIdx4Pk.length; i++) {
                result += args[argIdx4Pk[i]].hashCode();
            }
        } else {
            for (int i = 0; i < args.length; i++) {
                result += args[i].hashCode();
            }
        }

        return result;
    }

    public String getMethodName() {
        return methodName;
    }

    public Object[] getArgs() {
        return args;
    }

    public Object getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(Object returnValue) {
        this.returnValue = returnValue;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(argIdx4Pk);
        result = prime * result + Arrays.hashCode(args);
        result = prime * result + ((methodName == null) ? 0 : methodName.hashCode());
        result = prime * result + ((returnValue == null) ? 0 : returnValue.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RecordInformation other = (RecordInformation) obj;
        if (!Arrays.equals(argIdx4Pk, other.argIdx4Pk))
            return false;
        if (!Arrays.equals(args, other.args))
            return false;
        if (methodName == null) {
            if (other.methodName != null)
                return false;
        } else if (!methodName.equals(other.methodName))
            return false;
        if (returnValue == null) {
            if (other.returnValue != null)
                return false;
        } else if (!returnValue.equals(other.returnValue))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
