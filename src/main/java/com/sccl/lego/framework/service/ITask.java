package com.sccl.lego.framework.service;

/**
 * <p>Title: 系统任务</p>
 * <p>Description:  </p>
 * <p>@author xuq </p>
 * <p>@version 1.0.0 </p>
 * <p>@date 17/01/20181:33 PM
 */
public interface ITask {
    /**
     * 获取任务关键字
     *
     * @return
     */
     String getTaskKey();

    /**
     * 获取任务执行参数
     *
     * @return
     */
     String getExcuteParam();

    /**
     * 获取任务执行策略
     *
     * @return
     */
     Integer getExcuteStrategy();

    /**
     * 获取任务执行时间
     *
     * @return
     */
     Integer getExcuteDate();
}
