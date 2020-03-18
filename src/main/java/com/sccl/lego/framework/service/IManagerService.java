package com.sccl.lego.framework.service;

import com.sccl.lego.framework.exception.AppException;


public interface IManagerService extends IService {
    /**
     * 启动服务
     */
    void startService() throws AppException;

    /**
     * 停止服务
     */
    void stopService() throws AppException;
}
