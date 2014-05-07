package com.erik.tools.converter;

import com.erik.tools.converter.annotation.ConvertMapping;
import com.erik.tools.enums.DefinitionEnumAware;
import com.erik.tools.exceptions.ToolsException;
import com.erik.tools.utils.CamelCaseUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Java Bean 对象转换器
 * <p/>
 * User: erik
 * Date: 14-5-7 下午4:29
 * version $Id: BeanConverter.java, v 0.1 Exp $
 */
public class BeanConverter {

    /**
     * 日志对象
     */
    private static final Logger LOG = LoggerFactory.getLogger(BeanConverter.class);


    /**
     * 单个对象转换
     *
     * @param target 目标对象
     * @param map    源对象
     * @return 转换后的目标对象
     */
    public static <T> T convert(T target, Map<String, Object> map) {
        return convert(target, map, null);
    }

    /**
     * 单个对象转换
     *
     * @param target 目标对象
     * @param map    源对象
     * @return 转换后的目标对象
     */
    public static <T> T convert(T target, Map<String, Object> map, String[] ignoreProperties) {

        //过滤的属性
        List<String> ignoreList = (ignoreProperties != null) ? Arrays.asList(ignoreProperties)
                : null;

        //获取目标对象属性信息
        Field[] fields = getFields(target.getClass());

        for (Field field : fields) {

            try {
                if (CollectionUtils.isNotEmpty(ignoreList) && ignoreList.contains(field.getName())) {

                    continue;
                }

                Object value = null;

                //map的key
                String mapKey = field.getName();

                if (field.isAnnotationPresent(ConvertMapping.class)) {

                    ConvertMapping convertMapping = field.getAnnotation(ConvertMapping.class);

                    mapKey = convertMapping.origField();

                } else if (getEnableCamelCase()) {

                    //骆驼全名转换
                    mapKey = CamelCaseUtils.camelCase2Underline(mapKey);
                }

                if (map.containsKey(mapKey)) {

                    value = map.get(mapKey);
                }

                PropertyDescriptor targetPd = getPropertyDescriptor(target.getClass(),
                        field.getName());

                if (targetPd.getWriteMethod() == null) {

                    continue;
                }

                //自定义转换
                value = customConvert(targetPd.getPropertyType(), value);

                if (value == null) {
                    continue;
                }

                Method writeMethod = targetPd.getWriteMethod();

                if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                    writeMethod.setAccessible(true);
                }

                writeMethod.invoke(target, value);
            } catch (Exception e) {

                LOG.warn("属性转换失败[field={}]", field.getName());
                throw new ToolsException(e);
            }
        }

        return target;
    }

    /**
     * 单个对象转换
     *
     * @param target 目标对象
     * @param source 源对象
     * @return 转换后的目标对象
     */
    public static <T> T convert(T target, Object source) {
        return convert(target, source, null);
    }

    /**
     * 单个对象转换
     *
     * @param target           目标对象
     * @param source           源对象
     * @param ignoreProperties 需要过滤的属性
     * @return 转换后的目标对象
     */
    public static <T> T convert(T target, Object source, String[] ignoreProperties) {

        //过滤的属性
        List<String> ignoreList = (ignoreProperties != null) ? Arrays.asList(ignoreProperties)
                : null;

        //拷贝相同的属性
        copySameProperties(target, source, ignoreList);

        //拷贝注解的属性
        copyAnnotationProperties(target, source, ignoreList);

        return target;
    }

    /**
     * 拷贝相同的属性
     *
     * @param target     the target
     * @param source     the source
     * @param ignoreList the ignore list
     */
    private static void copySameProperties(Object target, Object source, List<String> ignoreList) {

        //获取目标对象属性信息
        PropertyDescriptor[] targetPds = getPropertyDescriptors(target.getClass());

        for (PropertyDescriptor targetPd : targetPds) {

            try {
                if (targetPd.getWriteMethod() == null
                        || (CollectionUtils.isNotEmpty(ignoreList) && ignoreList.contains(targetPd
                        .getName()))) {

                    continue;
                }

                PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(),
                        targetPd.getName());

                if (sourcePd != null && sourcePd.getReadMethod() != null) {

                    Method readMethod = sourcePd.getReadMethod();
                    if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                        readMethod.setAccessible(true);
                    }
                    Object value = readMethod.invoke(source);

                    //自定义转换
                    value = customConvert(targetPd.getPropertyType(), value);

                    Method writeMethod = targetPd.getWriteMethod();

                    if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                        writeMethod.setAccessible(true);
                    }

                    writeMethod.invoke(target, value);
                }
            } catch (Exception e) {
                LOG.warn("Bean转换时拷贝同名的属性失败[field={}]", targetPd.getName());
                throw new ToolsException(e);
            }

        }
    }

    /**
     * 拷贝注解的属性
     *
     * @param target     the target
     * @param source     the source
     * @param ignoreList the ignore list
     */
    private static void copyAnnotationProperties(Object target, Object source,
                                                 List<String> ignoreList) {

        //判断注解标识
        if (!getEnableAnnotation()) {
            return;
        }

        Field[] fields = getFields(target.getClass());

        for (Field field : fields) {

            try {
                if (!field.isAnnotationPresent(ConvertMapping.class)
                        || (CollectionUtils.isNotEmpty(ignoreList) && ignoreList.contains(field
                        .getName()))) {

                    continue;
                }

                PropertyDescriptor targetPd = getPropertyDescriptor(target.getClass(),
                        field.getName());

                if (targetPd.getWriteMethod() == null) {

                    continue;
                }

                ConvertMapping convertMapping = field.getAnnotation(ConvertMapping.class);

                String origFieldName = convertMapping.origField();

                if (StringUtils.isBlank(origFieldName)) {
                    continue;
                }

                PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(),
                        origFieldName);

                if (sourcePd != null && sourcePd.getReadMethod() != null) {

                    Method readMethod = sourcePd.getReadMethod();
                    if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                        readMethod.setAccessible(true);
                    }
                    Object value = readMethod.invoke(source);

                    //自定义转换
                    value = customConvert(field.getType(), value);

                    Method writeMethod = targetPd.getWriteMethod();

                    if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                        writeMethod.setAccessible(true);
                    }

                    writeMethod.invoke(target, value);
                }
            } catch (Exception e) {
                LOG.warn("Bean转换时拷贝注解标识的属性失败[field={}]", field.getName());
                throw new ToolsException(e);
            }
        }
    }

    /**
     * 用户自定义转换
     *
     * @param targetPropertyType the target property type
     * @param value              the value
     * @return the object
     */
    private static Object customConvert(Class<?> targetPropertyType, Object value) {

        Object result = value;

        Class<?> targetClass = targetPropertyType;

        if (value != null && value.getClass().isEnum()
                && DefinitionEnumAware.class.isAssignableFrom(value.getClass())) {

            LOG.debug("源对象为枚举类型[DefinitionEnumAware]");
            result = ((DefinitionEnumAware) value).getCode();
        }

        if (targetClass.isEnum() && DefinitionEnumAware.class.isAssignableFrom(targetClass)) {
            LOG.debug("目标对象为枚举类型[DefinitionEnumAware]");
            targetClass = DefinitionEnumAware.class;
        }

        //存在自定义数据转换器，进行转换
        DataTypeConverter dataTypeConverter = getDataTypeConverter(targetClass);

        if (dataTypeConverter != null) {

            result = dataTypeConverter.convert(targetPropertyType, value);
        }

        return result;
    }

    /**
     * 注册转换器
     *
     * @param clazz     the clazz
     * @param converter the converter
     */
    public static void registerConverter(Class<?> clazz, DataTypeConverter converter) {
        BeanConverterConfig.getInstance().registerConverter(clazz, converter);
    }

    /**
     * 移除注册的转换器
     *
     * @param clazz the clazz
     */
    public static void unregisterConverter(Class<?> clazz) {
        BeanConverterConfig.getInstance().unregisterConverter(clazz);
    }

    /**
     * 清空注册的转换器
     */
    public static void clearConverter() {
        BeanConverterConfig.getInstance().clearConverter();
    }

    /**
     * 设置注解开启标识
     *
     * @param isEnable 开启标识
     */
    public static void setEnableAnnotation(boolean isEnable) {
        BeanConverterConfig.getInstance().setEnableAnnotation(isEnable);
    }

    /**
     * 获取注解开启标识
     */
    public static boolean getEnableAnnotation() {
        return BeanConverterConfig.getInstance().getEnableAnnotation();
    }

    /**
     * 获取骆驼全名法转换标识
     *
     * @return the enable camel case
     */
    public static boolean getEnableCamelCase() {
        return BeanConverterConfig.getInstance().getEnableCamelCase();
    }

    /**
     * 设置骆驼全名法转换标识
     *
     * @param enableCamelCase the enable camel case
     */
    public static void setEnableCamelCase(boolean enableCamelCase) {
        BeanConverterConfig.getInstance().setEnableCamelCase(enableCamelCase);
    }

    /**
     * 返回JavaBean所有属性的<code>PropertyDescriptor</code>
     *
     * @param beanClass the bean class
     * @return the property descriptor [ ]
     */
    private static PropertyDescriptor[] getPropertyDescriptors(Class<?> beanClass) {

        IntrospectionCache introspectionCache = IntrospectionCache.forClass(beanClass);
        return introspectionCache.getPropertyDescriptors();
    }

    /**
     * 返回JavaBean给定JavaBean给定属性的 <code>PropertyDescriptors</code>
     *
     * @param beanClass    the bean class
     * @param propertyName the name of the property
     * @return the corresponding PropertyDescriptor, or <code>null</code> if none
     */
    private static PropertyDescriptor getPropertyDescriptor(Class<?> beanClass, String propertyName) {

        IntrospectionCache introspectionCache = IntrospectionCache.forClass(beanClass);
        return introspectionCache.getPropertyDescriptor(propertyName);
    }

    /**
     * 返回JavaBean所有字段信息
     *
     * @param beanClass the bean class
     * @return the field [ ]
     */
    private static Field[] getFields(Class<?> beanClass) {
        IntrospectionCache introspectionCache = IntrospectionCache.forClass(beanClass);
        return introspectionCache.getFields();
    }

    /**
     * 获取转换器
     *
     * @param clazz the clazz
     * @return the data type converter
     */
    private static DataTypeConverter getDataTypeConverter(Class<?> clazz) {

        return BeanConverterConfig.getInstance().getDataTypeConverter(clazz);
    }

}
