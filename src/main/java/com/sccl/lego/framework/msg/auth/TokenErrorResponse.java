package com.sccl.lego.framework.msg.auth;

import com.sccl.lego.framework.constant.RestCodeConstants;
import com.sccl.lego.framework.msg.BaseResponse;

/**
 * Created by lego on 2019/8/23.
 */
public class TokenErrorResponse extends BaseResponse {
    public TokenErrorResponse(String message) {
        super(RestCodeConstants.TOKEN_ERROR_CODE, message);
    }
}
