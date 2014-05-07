package com.erik.tools.converter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 属性转换注解，此注解用作标识属性转换时的属性映射
 * 
 * @author erik
 * @version $Id: ConvertMapping.java, v 0.1 2012-10-20 下午6:35:43 erik Exp $
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface ConvertMapping {

    /** 源字段名称 */
    String origField() default "";

    /** map key 名称 */
    String mapKey() default "";

}
