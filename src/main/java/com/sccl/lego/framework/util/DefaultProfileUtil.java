package com.sccl.lego.framework.util;

import com.sccl.lego.framework.config.LegoConstants;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;

/**
 * 加载spring profile工具栏
 * 当没有<code>spring.profile.active</code>设置在env或者命令中-Ddev时。
 * 在application.yml中进行profile设置，如果没有，默认为dev
 *
 * @author xuq
 */
public final class DefaultProfileUtil {
    /**
     * spring profile 默认key
     */
    private static final String SPRING_PROFILE_DEFAULT = "spring.profiles.default";

    private DefaultProfileUtil() {
    }

    /**
     * 没有profile设置时，设置默认dev,在springboot启动处使用
     *
     * @param app
     */
    public static void addDefaultProfile(SpringApplication app) {
        Map<String, Object> defaultMap = new HashMap<String, Object>();
        defaultMap.put(SPRING_PROFILE_DEFAULT, LegoConstants.LEGO_PROFILE_DEV);
        app.setDefaultProperties(defaultMap);
    }

    /**
     * 获取active profiles
     *
     * @param env spring环境
     * @return profiles
     */
    public static String[] getActiveProfiles(Environment env) {
        String[] activeProfile = env.getActiveProfiles();
        if (null != activeProfile && 0 == activeProfile.length) {
            //未获取配置生效的profile，获取默认
            return env.getDefaultProfiles();
        }
        return activeProfile;
    }

}
