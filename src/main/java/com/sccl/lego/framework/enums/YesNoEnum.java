package com.sccl.lego.framework.enums;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Title:
 * @Package: com.sccl.lego.framework.enums
 * @Description: (用一句话描述该文件做什么)
 * @Author: tinker
 * @Date: 2020-03-17
 * @Version: V1.0
 */
public enum YesNoEnum  {
  
    YES(1, "是"),
    NO(0, "否");

    private int value;
    private String desc;


    YesNoEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;

    }


    public int getValue() {
        return this.value;
    }

    public String getDesc() {
        return desc;
    }

    public static YesNoEnum getByValue(int value) {
        for (YesNoEnum examStatus : YesNoEnum.values()) {
            if (examStatus.getValue() == value) {
                return examStatus;
            }
        }
        return null;
    }
    /**
     * 获取Map集合
     * @param eItem 不包含项
     * @return
     */
    public static Map<String,String> getEnumMap(int... eItem) {
        Map<String,String> resultMap= new LinkedHashMap<String,String>();
        for (YesNoEnum item : YesNoEnum.values()) {
            try{
                boolean hasE = false;
                for (int e : eItem){
                    if(item.getValue()==e){
                        hasE = true;
                        break;
                    }
                }
                if(!hasE){
                    resultMap.put(item.getValue()+"", item.getDesc());
                }
            }catch(Exception ex){
            }
        }
        return resultMap;
    }
}
