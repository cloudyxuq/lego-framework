package com.sccl.lego.framework.exception.auth;


import com.sccl.lego.framework.constant.CommonConstants;
import com.sccl.lego.framework.exception.BaseException;

/**
 * Created by lego on 2019/9/12.
 */
public class ClientForbiddenException extends BaseException {
    public ClientForbiddenException(String message) {
        super(message, CommonConstants.EX_CLIENT_FORBIDDEN_CODE);
    }

}
