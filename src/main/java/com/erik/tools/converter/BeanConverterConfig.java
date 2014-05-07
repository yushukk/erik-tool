package com.erik.tools.converter;

import com.erik.tools.enums.DefinitionEnumAware;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Java Bean 对象转换器配置
 * <p/>
 * User: erik
 * Date: 14-5-6 下午1:56
 * version $Id: BeanConverterConfig.java, v 0.1 Exp $
 */
public class BeanConverterConfig {

    /** 包含 BeanConverter 的 ContextClassLoader 实例索引 */
    private static final ContextClassLoaderLocal BEANS_BY_CLASSLOADER = new ContextClassLoaderLocal() {

                                                                          // 创建默认的实例
                                                                          protected Object initialValue() {
                                                                              return new BeanConverterConfig();
                                                                          }
                                                                      };

    /** 指定对象的转换器，key为需要转换的类对象 */
    private Map<Class<?>, DataTypeConverter>     converters           = new WeakHashMap<Class<?>, DataTypeConverter>();

    /** 注解开启标识 */
    private boolean                              enableAnnotation     = false;

    /** 骆驼全名法转换标识 */
    private boolean                              enableCamelCase      = false;

    /**
     * 获取实例，提供的功能应用于 {@link BeanConverter}.
     * 这是一个伪单例 - 每一个线程的ContextClassLoader提供一个单例的实例
     * 这种机制提供了在同一个web容器中部署的应用程序之间的隔离
     *
     * @return 该伪单例的实例 BeanConverterConfig
     */
    public static BeanConverterConfig getInstance() {
        BeanConverterConfig beanConverterConfig = (BeanConverterConfig) BEANS_BY_CLASSLOADER.get();
        //默认初始化时注册枚举转换器
        beanConverterConfig.registerConverter(DefinitionEnumAware.class,
            new DefinitionEnumConverter());
        return beanConverterConfig;
    }

    /**
     * 设置实例，提供的功能应用于 {@link BeanConverter}.
     * 这是一个伪单例 - 每一个线程的ContextClassLoader提供一个单例的实例
     * 这种机制提供了在同一个web容器中部署的应用程序之间的隔离
     *
     * @param newInstance 该伪单例的实例 BeanConverterConfig
     */
    public static void setInstance(BeanConverterConfig newInstance) {
        BEANS_BY_CLASSLOADER.set(newInstance);
    }

    /**
     * 注册转换器
     *
     * @param clazz the clazz
     * @param converter the converter
     */
    public void registerConverter(Class<?> clazz, DataTypeConverter converter) {
        this.converters.put(clazz, converter);
    }

    /**
     * 移除注册的转换器
     *
     * @param clazz the clazz
     */
    public void unregisterConverter(Class<?> clazz) {
        this.converters.remove(clazz);
    }

    /**
     * 清空注册的转换器
     */
    public void clearConverter() {
        this.converters.clear();
    }

    /**
     * 获取转换器
     *
     * @param clazz the clazz
     * @return the data type converter
     */
    public DataTypeConverter getDataTypeConverter(Class<?> clazz) {
        return this.converters.get(clazz);
    }

    /**
     * 设置注解开启标识
     *
     * @param enableAnnotation 注解开启标识
     */
    public void setEnableAnnotation(boolean enableAnnotation) {
        this.enableAnnotation = enableAnnotation;
    }

    /**
     * 获取注解开启标识
     *
     * @return the enable annotation
     */
    public boolean getEnableAnnotation() {
        return this.enableAnnotation;
    }

    /**
     * 获取骆驼全名法转换标识
     *
     * @return the enable camel case
     */
    public boolean getEnableCamelCase() {
        return enableCamelCase;
    }

    /**
     * 设置骆驼全名法转换标识
     *
     * @param enableCamelCase the enable camel case
     */
    public void setEnableCamelCase(boolean enableCamelCase) {
        this.enableCamelCase = enableCamelCase;
    }
}
