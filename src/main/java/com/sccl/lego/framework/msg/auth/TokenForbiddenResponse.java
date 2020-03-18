package com.sccl.lego.framework.msg.auth;

import com.sccl.lego.framework.constant.RestCodeConstants;
import com.sccl.lego.framework.msg.BaseResponse;

/**
 * Created by lego on 2019/8/25.
 */
public class TokenForbiddenResponse  extends BaseResponse {
    public TokenForbiddenResponse(String message) {
        super(RestCodeConstants.TOKEN_FORBIDDEN_CODE, message);
    }
}
