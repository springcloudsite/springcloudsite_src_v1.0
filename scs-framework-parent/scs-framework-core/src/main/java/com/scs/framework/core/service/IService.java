/*
 * Copyright (c) Pactera Corp.
 * All Rights Reserved.
 */
package com.scs.framework.core.service;

import java.util.List;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;

/**
 * Service基础接口
 *
 * @version ScsDI v1.0
 * @author Zhang Ran, 2018年4月19日
 */
public interface IService<TEntity, ID> {

    /**
     * Adds a given entity. 添加，默认为空字段不生成sql
     * 
     * @param entity will be added entity
     * @return the added entity
     * @throws IllegalArgumentException in case the given entity is (@literal null}.
     */
    TEntity add(TEntity entity);

    /**
     * Adds all given entities. 批量添加，一个事务提交
     * 
     * @param entities will be added entities
     * @return the added entities
     */
    List<TEntity> add(List<TEntity> entities);
    
    /**
     * 添加所有字段，不管字段是否为空
     * @param entity
     * @return
     */
    TEntity addAllFields(TEntity entity);

    /**
     * Updates a given entity. 更新全部
     * 
     * @param entity will be updated entity
     * @return the updated entity
     * @throws IllegalArgumentException in case the given entity is (@literal null}.
     */
    TEntity update(TEntity entity);

    /**
     * Updates a given entity. 批量更新全部，一个事务
     * 
     * @param entities will be updated entities
     * @return the updated entities
     */
    List<TEntity> update(List<TEntity> entities);

    /**
     * Updates a given entity's partial 更新部分
     * 
     * @param entity
     * @return
     */
    TEntity updatePartial(TEntity entity);

    /**
     * Updates all given entities. 批量更新部分，一个事务
     * 
     * @param entities will be updated entities
     * @return the updated entities
     */
    List<TEntity> updatePartial(List<TEntity> entities);

    /**
     * Deletes the entity with the given id. 根据id删除
     * 
     * @param id must not be null.
     */
    void delete(ID id);

    /**
     * Deletes the given entities in a batch 批量删除，一个事务
     * 
     * @param ids
     */
    void deleteInBatch(List<ID> ids);

    /**
     * Retrieves an entity by its id
     * 
     * @param id must not be null.
     * @return the entity with the given id or null if not found.
     */
    TEntity findOne(ID id);

    /**
     * Returns all instances of the type.查询所有
     * 
     * @return all entities.
     */
    List<TEntity> findAll();
    
    /**
     * Returns all instances of the type by page. 分页查询所有
     * 
     * @param pageBounds
     * @return
     */
    PageList<TEntity> findPage(PageBounds pageBounds);
    
    /**
     * Returns all instances of the type by contidion. 根据条件查询所有
     * @param pageBounds
     * @param entity
     * @return
     */
    List<TEntity> findAll(TEntity entity);
    
    /**
     * Returns all instances of the type by page、contidion. 根据条件分页查询所有
     * @param pageBounds
     * @param entity
     * @return
     */
    PageList<TEntity> findPage(PageBounds pageBounds, TEntity entity);
    
    
}
