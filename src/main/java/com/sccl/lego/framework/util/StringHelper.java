package com.sccl.lego.framework.util;

/**
 * Created by lego on 2019/9/10.
 */
public class StringHelper {
    public static String getObjectValue(Object obj){
        return obj==null?"":obj.toString();
    }
}
