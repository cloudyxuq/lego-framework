package com.sccl.lego.framework.service.impl.snowflake;

import com.sccl.lego.framework.util.snowflake.Snowflake;
import org.springframework.stereotype.Service;

/**
 * @Title:
 * @Package: com.sccl.lego.framework.service.impl.snowflake
 * @Description: (用一句话描述该文件做什么)
 * @Author: tinker
 * @Date: 2020-03-17
 * @Version: V1.0
 */
@Service
public class SnowFlakeService {

    public long snowGenId() {
        return Snowflake.getInstance().generateId();
    }

    public String snowGenStrId() {
        return String.valueOf(Snowflake.getInstance().generateId());
    }
}
