package com.dataart.secondmonth.utils;

public class ClassUtils {

    public static boolean isNullOrPrimitiveOrWrapperOrString(Object value) {
        return value == null || org.apache.commons.lang3.ClassUtils.isPrimitiveOrWrapper(value.getClass()) || value.getClass() == String.class;
    }

}
