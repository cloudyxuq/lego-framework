package com.sccl.lego.framework.exception;

public class AppException extends RuntimeException {

    private static final long serialVersionUID = 3770580067312032700L;

    /** 错误代码 */
    private int code = 99999;

    /** 错误信息 */
    private String message = "";

    public AppException() {

    }

    public AppException(String message) {
        this(-1, message);
    }

    public AppException(int code) {
        this.code = code;
    }

    public AppException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public AppException(Throwable e) {
        super(e);
    }

    public AppException(String message, Throwable e) {
        super(message, e);
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
