/*
 * Copyright (c) Pactera Corp.
 * All Rights Reserved.
 */
package com.scs.framework.core.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.FieldError;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.scs.framework.core.dao.MyBatisBaseDao;
import com.scs.framework.core.exception.BaseException;
import com.scs.framework.core.exception.BusinessException;
import com.scs.framework.core.exception.UnprocesableEntityException;
import com.scs.framework.utils.Assert;

/**
 * Service抽象基础类
 *
 * @version ScsDI v1.0
 * @author Zhang Ran, 2018年4月19日
 */
public abstract class AbstractBaseService<TEntity, K extends Serializable>
        implements IService<TEntity, K> {

    /**
     * 校验的通用提示信息.
     */
    public static final String ENTITY_VALIDATE_ERROR = "entity.validate.error";

    /**
     * 
     * Get default Dao of TEntity..
     * 
     * @return default Dao of TEntity.
     */
    protected abstract MyBatisBaseDao<TEntity, K> getDao();

    /**
     * 添加，默认为空的字段不操作
     * 
     * @param entity
     * @return
     * @see com.scs.scsdi.framework.core.service.IService#add(java.lang.Object)
     */
    @Transactional
    @Override
    public TEntity add(TEntity entity) {

        Assert.notNull(entity, "The given entity must not be null!");

        addCheck(entity);
        getDao().insertSelective(prepareAdd(entity));
        return afterAdd(entity);
    }

    /**
     * 添加所有字段，不管字段是否为空
     * 
     * @param entity
     * @return
     */
    @Transactional
    @Override
    public TEntity addAllFields(TEntity entity) {

        Assert.notNull(entity, "The given entity must not be null!");

        addCheck(entity);
        getDao().insert(prepareAdd(entity));
        return afterAdd(entity);
    }

    /**
     * 批量添加，默认为空的字段不操作
     * 
     * @param entities
     * @return
     * @see com.scs.scsdi.framework.core.service.IService#add(java.util.List)
     */
    @Transactional
    @Override
    public List<TEntity> add(List<TEntity> entities) {

        Assert.notNull(entities, "The given list must nout be null!");

        List<TEntity> resultList = new ArrayList<>();
        
        entities.forEach(e -> resultList.add(add(e)));

        return resultList;
    }

    /**
     * 修改全部，所有字段都赋值
     * 
     * @param entity
     * @return
     * @see com.scs.scsdi.framework.core.service.IService#update(java.lang.Object)
     */
    @Transactional
    @Override
    public TEntity update(TEntity entity) {

        Assert.notNull(entity, "The given entity must not be null!");

        updateCheck(entity);
        getDao().updateByPrimaryKey(prepareUpdate(entity));
        return afterUpdate(entity);
    }

    /**
     * 修改全部，所有字段都赋值
     * 
     * @param entities
     * @return
     * @see com.scs.scsdi.framework.core.service.IService#update(java.util.List)
     */
    @Transactional
    @Override
    public List<TEntity> update(List<TEntity> entities) {

        Assert.notNull(entities, "The given list must not be null!");

        List<TEntity> resultList = new ArrayList<>();

        entities.forEach(e -> resultList.add(update(e)));

        return resultList;
    }

    /**
     * 修改，默认为空的字段不操作.
     * 
     * @param entity
     * @return
     */
    @Transactional
    @Override
    public TEntity updatePartial(TEntity entity) {

        Assert.notNull(entity, "The given entity must not be null!");

        updateCheck(entity);
        getDao().updateSelectiveByPrimaryKey(prepareUpdate(entity));
        return afterUpdate(entity);
    }

    /**
     * Updates all given entities. 批量更新部分，一个事务
     * 
     * @param entities
     * @return
     * @see com.scs.scsdi.framework.core.service.IService#updatePartial(java.util.List)
     */
    @Transactional
    @Override
    public List<TEntity> updatePartial(List<TEntity> entities) {

        Assert.notNull(entities, "The given list must not be null!");

        List<TEntity> resultList = new ArrayList<>();

        entities.forEach(e -> resultList.add(updatePartial(e)));

        return resultList;
    }

    /**
     * 删除
     * 
     * @param id
     * @see araf.core.interfaces.app.IDelete#delete(java.io.Serializable)
     */

    @Transactional
    @Override
    public void delete(K id) {

        Assert.notNull(id, "The given id must not be null!");
        prepareDelete(findOne(id));
        getDao().deleteByPrimaryKey(id);
    }

    /**
     * 删除
     * 
     * @param id
     * @see araf.core.interfaces.app.IDelete#delete(java.io.Serializable)
     */
    @Transactional
    @Override
    public void deleteInBatch(List<K> ids) {

        getDao().deleteBatchByPrimaryKeys(ids);
    }

    /**
     * 根据id查询
     * 
     * @param id
     * @return
     * @see com.scs.scsdi.framework.core.service.IService#findOne(java.lang.Object)
     */
    @Override
    public TEntity findOne(K id) {

        Assert.notNull(id, "The given id must not be null!");
        return getDao().selectByPrimaryKey(id);
    }

    /**
     * 查询所有
     * 
     * @return
     * @see com.scs.scsdi.framework.core.service.IService#findAll()
     */
    @Override
    public List<TEntity> findAll() {

        return getDao().selectAll(null);
    }

    /**
     * Returns all instances of the type by page. 分页查询所有
     * 
     * @param pageBounds
     * @return
     * @see com.scs.scsdi.framework.core.service.IService#findPage(com.github.miemiedev.mybatis.paginator.domain.PageBounds)
     */
    @Override
    public PageList<TEntity> findPage(PageBounds pageBounds) {
        return getDao().selectPage(pageBounds, null);
    }

    /**
     * Returns all instances of the type by contidion. 根据条件查询所有
     * 
     * @param entity
     * @return
     * @see com.scs.scsdi.framework.core.service.IService#findAll(java.lang.Object)
     */
    @Override
    public List<TEntity> findAll(TEntity entity) {
        return getDao().selectAll(entity);
    }

    /**
     * Returns all instances of the type by page、contidion. 根据条件分页查询所有
     * 
     * @param pageBounds
     * @param entity
     * @return
     * @see com.scs.scsdi.framework.core.service.IService#findPage(com.github.miemiedev.mybatis.paginator.domain.PageBounds,
     *      java.lang.Object)
     */
    @Override
    public PageList<TEntity> findPage(PageBounds pageBounds, TEntity entity) {
        return getDao().selectPage(pageBounds, entity);
    }

    /**
     * Check the to-be-added BaseEntity.
     * 
     * @param entity
     * @throws BaseException BaseException
     */
    protected void addCheck(TEntity entity) {

        checkEntity(entity);
    }

    /**
     * Prepare entity for adding data.
     * 
     * @param entity to be prepared entity
     * @return prepared entity
     */
    protected TEntity prepareAdd(TEntity entity) {
        AuditPropertyUtils.processCreatedBy(entity);
        AuditPropertyUtils.processCreatedDate(entity);
        return prepareEntity(entity);
    }

    /**
     * Operation after add save.
     * 
     * @param entity
     * @return TEntity
     */
    protected TEntity afterAdd(TEntity entity) {

        return entity;
    }

    /**
     * Check the to-be-updated BaseEntity.
     * 
     * @param entity
     * @throws BaseException
     */
    protected void updateCheck(TEntity entity) {

        checkEntity(entity);
    }

    /**
     * Prepare entity for updating data.
     * 
     * @param entity to be prepared entity
     * @return prepared entity
     */
    protected TEntity prepareUpdate(TEntity entity) {
        AuditPropertyUtils.processModifyBy(entity);
        AuditPropertyUtils.processModifyDate(entity);
        return prepareEntity(entity);
    }

    /**
     * Operation after update.
     * 
     * @param entity
     * @return TEntity
     */
    protected TEntity afterUpdate(TEntity entity) {

        return entity;
    }

    /**
     * Check the to-be-deleted BaseEntity.检测是否可以删除，针对有级联的情况.默认返回true.
     * 
     * @param entity
     * @throws BaseException
     */
    public boolean deleteCheck(K id) {

        return true;
    }

    /**
     * Prepare entity for deleting data.
     * 
     * @param entity to be prepared entity
     * @return prepared entity
     */
    protected TEntity prepareDelete(TEntity entity) {

        return entity;
    }

    /**
     * Check the to-be-added,updated BaseEntity.
     * 
     * @param entity
     * @throws BaseException
     */
    protected void checkEntity(TEntity entity) {

        if (entity == null) {
            throw new BusinessException("the entity to be checked should not be null!");
        }
    }

    /**
     * 处理spring validation校验后的错误信息
     * 
     * @param field
     * @param msg
     */
    protected void processValidations(final String field, final String msg) {
        FieldError error = new FieldError(field, field, msg);
        List<FieldError> errors = new ArrayList<>();
        errors.add(error);
        processValidations(errors);
    }

    /**
     * 处理spring validation校验后的错误信息
     * 
     * @param errors
     */
    protected void processValidations(final List<FieldError> errors) {
        if (!errors.isEmpty()) {
            Map<String, String> map = new HashMap<>();
            errors.forEach(err -> map.put(err.getField(), err.getDefaultMessage()));
            throw new UnprocesableEntityException("fields invalid", map);
        }
    }

    /**
     * Prepare entity for the entity that will be added,updated.
     * 
     * @param entity
     * @return TEntity
     */
    protected TEntity prepareEntity(TEntity entity) {

        return entity;
    }

}