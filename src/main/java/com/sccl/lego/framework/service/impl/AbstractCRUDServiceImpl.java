package com.sccl.lego.framework.service.impl;

import com.sccl.lego.framework.exception.AppException;
import com.sccl.lego.framework.service.ICRUDService;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public abstract class AbstractCRUDServiceImpl<M extends Mapper<T>, T> implements ICRUDService<T> {
    @Autowired
    protected M mapper;


    @Override
    public T selectOne(T entity) throws AppException {
        return mapper.selectOne(entity);
    }

    @Override
    public T selectById(Object id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public List<T> selectList(T entity) {
        return mapper.select(entity);
    }

    @Override
    public List<T> selectListAll() {
        return mapper.selectAll();
    }

    @Override
    public Long selectCount(T entity) {
        return Long.valueOf(mapper.selectCount(entity));
    }

    @Override
    public void insert(T entity) {
        int iRet;
        iRet =  mapper.insert(entity);
        if (iRet == 0) {
            throw new AppException(-1001, "写入数据库失败！");
        }

    }

    @Override
    public void insertSelective(T entity) {
        mapper.insertSelective(entity);
    }

    @Override
    public void delete(T entity) {
        mapper.delete(entity);
    }

    @Override
    public void deleteById(Object id) {
        mapper.deleteByPrimaryKey(id);
    }

    @Override
    public void updateById(T entity) {
        mapper.updateByPrimaryKey(entity);
    }

    @Override
    public void updateSelectiveById(T entity) {
        mapper.updateByPrimaryKeySelective(entity);
    }

}
