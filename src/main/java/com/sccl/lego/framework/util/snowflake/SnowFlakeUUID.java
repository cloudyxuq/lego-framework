package com.sccl.lego.framework.util.snowflake;

import tk.mybatis.mapper.genid.GenId;

/**
 * @Title:
 * @Package: com.sccl.lego.framework.util.snowflake
 * @Description: (用一句话描述该文件做什么)
 * @Author: tinker
 * @Date: 2020-03-17
 * @Version: V1.0
 */
public class SnowFlakeUUID implements GenId<Long> {

    @Override
    public Long genId(String s, String s1) {
        return Snowflake.getInstance().generateId();
    }
}
