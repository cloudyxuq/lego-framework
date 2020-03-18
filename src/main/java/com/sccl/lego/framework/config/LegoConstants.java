package com.sccl.lego.framework.config;

/**
 * Lego服务中心常量
 *
 * `JAVA_OPTS` Debug下`-Dspring.profiles.active=test`运行测试环境
 * ./mvnw -Pprod,swagger运行
 * ./mvnw -Pprod package 打包
 * ./java -jar lego.war --spring.profiles.active=prod 生产环境运行
 * @author xuq
 */
public final class LegoConstants {
    /**
     * LEGO CLOUD开发模式
     */
    public static final String LEGO_PROFILE_DEV = "dev";
    /**
     * LEGO CLOUD测试模式
     */
    public static final String LEGO_PROFILE_TEST = "test";
    /**
     * LEGO CLOUD生产模式
     */
    public static final String LEGO_PROFILE_PRODUCT = "prod";
    /**
     * swagger api profile，用于是否禁用
     */
    public static final String LEGO_PROFILE_SWAGGER = "swagger";


//    @GeneratedValue(generator = "snowflake")
//    @GenericGenerator(name = "snowflake", strategy = "com.sccl.lego.framework.util.snowflake.Snowflake")

    /**
     * 雪花ID generator
     */
    public static final String LEGO_SNOWFLAKE_GENERTORY = "Snowflake";
    /**
     * 雪花id strategy
     */
    public static final String LEGO_SNOWFLAKE_Strategy ="com.sccl.lego.framework.util.SnowflakeId";

}
