package com.erik.tools.exceptions;

import org.apache.commons.lang.StringUtils;

/**
 * 全局异常
 * 
 * @author erik
 * @version $Id: GlobalException.java, v 0.1 2017-12-4 下午7:52:08 erik Exp $
 */
public class GlobalException extends RuntimeException {

    /** serialVersionUID */
    private static final long serialVersionUID = 1068543011536534296L;

    /** 异常结果码 */
    protected String          resultCode       = "UN_KNOWN_EXCEPTION";

    /** 异常结果信息 */
    protected String          resultMsg        = "未知异常";

    /**
     * 创建一个<code>GlobalException</code>对象
     */
    public GlobalException() {
        super();
    }

    /**
     * 创建一个<code>GlobalException</code>对象
     * 
     * @param resultMsg   异常结果信息
     */
    public GlobalException(String resultMsg) {
        super(resultMsg);
        this.resultMsg = resultMsg;
    }

    /**
     * 创建一个<code>GlobalException</code>对象
     * 
     * @param resultCode     异常结果码  
     * @param resultMsg      异常结果信息
     */
    public GlobalException(String resultCode, String resultMsg) {
        super(resultMsg);
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }

    /**
     * 创建一个<code>GlobalException</code>
     * 
     * @param cause      异常原因
     */
    public GlobalException(Throwable cause) {
        super(cause);
    }

    /**
     * Getter method for property <tt>resultCode</tt>.
     * 
     * @return property value of resultCode
     */
    public String getResultCode() {
        return resultCode;
    }

    /**
     * Setter method for property <tt>resultCode</tt>.
     * 
     * @param resultCode value to be assigned to property resultCode
     */
    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    /**
     * Getter method for property <tt>resultMsg</tt>.
     * 
     * @return property value of resultMsg
     */
    public String getResultMsg() {
        return resultMsg;
    }

    /**
     * Setter method for property <tt>resultMsg</tt>.
     * 
     * @param resultMsg value to be assigned to property resultMsg
     */
    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    @Override
    public String toString() {
        String s = getClass().getName();
        String message = getLocalizedMessage();
        return (StringUtils.isNotBlank(message)) ? (s + ": " + message)
            : (s + ": " + resultCode + "。");
    }

}
