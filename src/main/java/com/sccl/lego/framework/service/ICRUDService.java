package com.sccl.lego.framework.service;

import com.sccl.lego.framework.exception.AppException;
import com.sccl.lego.framework.msg.TableResultResponse;
import com.sccl.lego.framework.util.Query;

import java.util.List;


public interface ICRUDService<T> extends IService {

    /****************SELECT*********************/


    /****************SELECT*********************/
    /****************SELECT*********************/
    /****************SELECT*********************/





    /**
     * 根据实体中的属性值进行查询，查询条件使用等号
     * @param entity
     * @return 单个对象
     * @throws AppException
     */
    T selectOne(T entity) throws AppException;

    /**
     * 根据主键字段进行查询，查询条件使用等号
     * @param id 方法参数必须包含完整的主键属性
     * @return 单个对象
     * @throws AppException
     */
    T selectById(Object id) throws AppException;

    /**
     * 根据实体中的属性值进行查询，查询条件使用等号
     * @param entity
     * @return list集合
     */
    List<T> selectList(T entity) throws AppException;


    /**
     * 获取所有对象
     *
     * @return
     */
    List<T> selectListAll() throws AppException;


    /**
     * 查询总记录数
     *
     * @param entity
     * @return
     */
    Long selectCount(T entity) throws AppException;

    /**
     * 添加
     *
     * @param entity
     */
    void insert(T entity) throws AppException;


    /**
     * 插入 不插入null字段
     *
     * @param entity
     */
    void insertSelective(T entity) throws AppException;

    /**
     * 删除
     *
     * @param entity
     */
    void delete(T entity) throws AppException;

    /**
     * 根据Id删除
     *
     * @param id
     */
    void deleteById(Object id) throws AppException;


    /**
     * 根据id更新
     *
     * @param entity
     */
    void updateById(T entity) throws AppException;


    /**
     * 不update null
     *
     * @param entity
     */
    void updateSelectiveById(T entity) throws AppException;

    /**
     *
     * @param example
     * @return
     * @throws AppException
     */
    List<T> selectByExample(Object example) throws AppException;

    /**
     *
     * @param example
     * @return
     * @throws AppException
     */
    int selectCountByExample(Object example) throws AppException;

    /**
     *
     * @param query
     * @return
     */
    TableResultResponse<T> selectByQuery(Query query);
}
