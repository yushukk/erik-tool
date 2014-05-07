package com.erik.tools.utils;

import org.apache.commons.lang.StringUtils;

/**
 * 骆驼命名转换辅助类
 * <p/>
 * User: erik
 * Date: 14-5-7 下午1:41
 * version $Id: CamelCaseUtils.java, v 0.1 Exp $
 */
public class CamelCaseUtils {

    /**
     * 骆驼命名拆分回下划线
     *
     * @param name the name
     * @return the string
     */
    public static String camelCase2Underline(String name) {

        if (StringUtils.isBlank(name)) {
            return null;
        }

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < name.length(); i++) {

            char c = name.charAt(i);

            if (i > 0 && Character.isUpperCase(c)) {
                sb.append("_");
            }

            sb.append(c);
        }

        return sb.toString().toUpperCase();
    }

}
