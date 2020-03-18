package com.sccl.lego.framework.exception.auth;


import com.sccl.lego.framework.constant.CommonConstants;
import com.sccl.lego.framework.exception.BaseException;

/**
 * Created by lego on 2019/9/8.
 */
public class UserInvalidException extends BaseException {
    public UserInvalidException(String message) {
        super(message, CommonConstants.EX_USER_PASS_INVALID_CODE);
    }
}
