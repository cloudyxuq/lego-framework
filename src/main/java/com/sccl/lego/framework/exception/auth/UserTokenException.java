package com.sccl.lego.framework.exception.auth;


import com.sccl.lego.framework.constant.CommonConstants;
import com.sccl.lego.framework.exception.BaseException;

/**
 * Created by lego on 2019/9/8.
 */
public class UserTokenException extends BaseException {
    public UserTokenException(String message) {
        super(message, CommonConstants.EX_USER_INVALID_CODE);
    }
}
