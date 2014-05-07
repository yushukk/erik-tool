package com.erik.tools.exceptions;


/**
 * 工具包自定义exception
 * 
 * @author erik
 * @version $Id: ToolsException.java, v 0.1 2012-4-6 上午11:30:46 erik Exp $
 */
public class ToolsException extends GlobalException {

    /** serialVersionUID */
    private static final long serialVersionUID = 1466391909346137480L;

    /**
     * 创建一个<code>ToolsException</code>对象
     */
    public ToolsException() {
        super();
    }

    /**
     * 创建一个<code>ToolsException</code>对象
     * 
     * @param resultMsg   异常结果码
     */
    public ToolsException(String resultMsg) {
        super(resultMsg);
    }

    /**
     * 创建一个<code>ToolsException</code>对象
     * 
     * @param resultCode     异常结果码  
     * @param resultMsg      异常结果信息
     */
    public ToolsException(String resultCode, String resultMsg) {
        super(resultCode, resultMsg);
    }

    /**
     * 创建一个<code>ToolsException</code>
     * 
     * @param cause      异常原因
     */
    public ToolsException(Throwable cause) {
        super(cause);
    }
}
