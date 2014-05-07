package com.erik.tools.enums;

/**
 * All the enum best implements the interface
 * <p/>
 * User: erik
 * Date: 14-5-7 上午10:17
 * version $Id: DefinitionEnumAware.java, v 0.1 Exp $
 */
public interface DefinitionEnumAware {

    /**
     * Get target enum by code
     *
     * @param code the code
     * @return the target enum
     */
    public DefinitionEnumAware getTargetEnum(String code);

    /**
     * Get all enums.Apply to custom, such as enumerating the need to sort
     *
     * @return the definition enum aware [ ]
     */
    public DefinitionEnumAware[] getAllEnums();

    /**
     * Get the enum code.
     *
     * @return the code
     */
    public String getCode();

    /**
     * Get the enum description.
     *
     * @return the desc
     */
    public String getDesc();

}
