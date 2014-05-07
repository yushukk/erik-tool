package com.erik.tools.converter;

/**
 * 数据类型转换接口
 * <p/>
 * User: erik
 * Date: 14-5-7 下午12:04
 * version $Id: DataTypeConverter.java, v 0.1 Exp $
 */
public interface DataTypeConverter {

    /**
     * Convert the specified input object into an output object of the
     * specified type.
     *
     * @param targetClass Data type to which this value should be converted
     * @param value The input value to be converted
     * @return The converted value
     *
     */
    public Object convert(Class<?> targetClass, Object value);

}
