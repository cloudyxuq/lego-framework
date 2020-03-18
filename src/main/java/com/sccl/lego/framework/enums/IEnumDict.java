package com.sccl.lego.framework.enums;

/**
 * 枚举字典
 *
 * @author mic
 * @date 2018/1/2
 */
public interface IEnumDict<T> {
    /**
     * 字典值
     *
     * @return
     */
    T getValue();

    /**
     * 字典显示
     *
     * @return
     */
    String getLabel();
}
