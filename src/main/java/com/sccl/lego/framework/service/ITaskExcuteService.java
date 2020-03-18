package com.sccl.lego.framework.service;

import com.sccl.lego.framework.exception.AppException;

/**
 * <p>Title: 任务执行服务</p>
 * <p>@author xuq </p>
 * <p>@version 1.0.0 </p>
 * <p>@date 17/01/20181:35 PM
 */
public interface ITaskExcuteService extends ITask {

    /**
     * 执行
     *
     * @param task
     * @throws AppException
     */
    void excuteTask(ITask task) throws AppException;

    /**
     * 停止
     *
     * @param task
     * @throws AppException
     */
    void stopTask(ITask task) throws AppException;
}
