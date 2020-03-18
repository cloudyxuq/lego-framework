package com.sccl.lego.framework.msg;

/**
 * 基础响应
 *
 */
public class BaseResponse {
    /**
     * 响应状态码，默认成功200
     */
    private int status = 200;
    /**
     * 响应信息
     */
    private String message;

    public BaseResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public BaseResponse() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


}
