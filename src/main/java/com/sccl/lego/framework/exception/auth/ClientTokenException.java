package com.sccl.lego.framework.exception.auth;


import com.sccl.lego.framework.constant.CommonConstants;
import com.sccl.lego.framework.exception.BaseException;

/**
 * Created by lego on 2019/9/10.
 */
public class ClientTokenException extends BaseException {
    public ClientTokenException(String message) {
        super(message, CommonConstants.EX_CLIENT_INVALID_CODE);
    }
}
