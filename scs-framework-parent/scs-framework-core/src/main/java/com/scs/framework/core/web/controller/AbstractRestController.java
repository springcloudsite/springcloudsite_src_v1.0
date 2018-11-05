/*
 * Copyright (c) Pactera Corp.
 * All Rights Reserved.
 */
package com.scs.framework.core.web.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.scs.framework.core.exception.UnprocesableEntityException;
import com.scs.framework.core.service.IService;

/**
 * restful api 基类
 *
 * @version ScsDI v1.0
 * @author Zhang Ran, 2018年4月19日
 */
public abstract class AbstractRestController<T, K extends Serializable> {

    abstract protected IService<T, K> getService();

    /**
     * Post for add.
     * 
     * @param vo
     * @return
     */
    protected ResponseEntity<?> doPostForAdd(T vo) {

        return createCreatedResponse(getService().add(vo));
    }

    /**
     * Post for add and validation
     * 
     * @param vo
     * @param result
     * @return
     */
    protected ResponseEntity<?> doPostForAdd(T vo, BindingResult result) {

        processValidations(result);
        return doPostForAdd(vo);
    }

    /**
     * Post for batch add.
     * 
     * @param list
     * @return
     */
    protected ResponseEntity<?> doPostForBatchAdd(List<T> list) {

        return createCreatedResponse(getService().add(list));
    }

    /**
     * Post for add all fields and validation
     * 
     * @param vo
     * @return
     */
    protected ResponseEntity<?> doPostForAddAllFields(T vo, BindingResult result) {

        processValidations(result);
        return createCreatedResponse(getService().addAllFields(vo));
    }

    /**
     * Post for add all fields
     * 
     * @param vo
     * @return
     */
    protected ResponseEntity<?> doPostForAddAllFields(T vo) {

        return createCreatedResponse(getService().addAllFields(vo));
    }

    /**
     * Put for update.
     * 
     * @param vo
     * @return
     */
    protected ResponseEntity<?> doPut(T po) {

        return createCreatedResponse(getService().update(po));

    }

    /**
     * Put for update and validation.
     * 
     * @param vo
     * @return
     */
    protected ResponseEntity<?> doPut(T po, BindingResult result) {

        processValidations(result);
        return createCreatedResponse(getService().update(po));

    }

    /**
     * Put for update batch
     * 
     * @param list
     * @return
     */
    protected ResponseEntity<?> doPutBatch(List<T> list) {

        return createCreatedResponse(getService().update(list));

    }

    /**
     * Patch for partial update.
     * 
     * @param id
     * @param vo
     * @return
     */
    protected ResponseEntity<?> doPatch(T po) {

        return createCreatedResponse(getService().updatePartial(po));

    }

    /**
     * Patch batch for partial update.
     * 
     * @param id
     * @param vo
     * @return
     */
    protected ResponseEntity<?> doPatchBatch(List<T> list) {

        return createCreatedResponse(getService().updatePartial(list));

    }

    /**
     * Delete
     * 
     * @param id
     * @return
     */
    protected ResponseEntity<?> doDelete(K id) {
        getService().delete(id);
        return createNoContentReponse();
    }

    /**
     * Delete batch.
     * 
     * @param id
     * @return
     */
    protected ResponseEntity<?> doDeleteBatch(List<K> list) {

        getService().deleteInBatch(list);
        return createNoContentReponse();

    }

    /**
     * Get for one.
     * 
     * @param id
     * @return
     */
    protected ResponseEntity<?> doGetOneEntity(K id) {
        return createOKResponse(getService().findOne(id));
    }

    /**
     * Get for All.
     * 
     * @return
     */
    protected ResponseEntity<?> doGetAll() {
        return createOKResponse(getService().findAll());
    }

    /**
     * Get for page
     * 
     * @param pageBounds
     * @return
     */
    protected ResponseEntity<?> doGetPage(PageBounds pageBounds) {
        return createOKResponse(getService().findPage(pageBounds));
    }

    /**
     * Get for all by condition
     * 
     * @param vo
     * @return
     */
    protected ResponseEntity<?> doGetAll(T vo) {
        return createOKResponse(getService().findAll(vo));
    }

    /**
     * Get for page by condition
     * 
     * @param pageBounds
     * @param vo
     * @return
     */
    protected ResponseEntity<?> doGetPage(PageBounds pageBounds, T vo) {
        return createOKResponse(getService().findPage(pageBounds, vo));
    }

    /**
     * 创建restful api返回结果
     * 
     * @param payload
     * @param status
     * @return
     */
    protected ResponseEntity<?> createResponse(Object payload, HttpStatus status) {
        return new ResponseEntity<>(payload, status);
    }

    /**
     * 创建restful api返回结果
     * 
     * @param status
     * @return
     */
    protected ResponseEntity<?> createResponse(HttpStatus status) {
        return new ResponseEntity<>(status);
    }

    /**
     * 成功返回用户请求的数据，该操作是幂等的（Idempotent）。
     * 
     * @param payload
     * @return
     */
    protected ResponseEntity<?> createOKResponse(Object payload) {
        return createResponse(payload, HttpStatus.OK);
    }

    /**
     * 用户新建或修改数据成功。
     * 
     * @param payload
     * @return
     */
    protected ResponseEntity<?> createCreatedResponse(Object payload) {
        return createResponse(payload, HttpStatus.CREATED);
    }

    /**
     * 表示一个请求已经进入后台排队（异步任务）
     * 
     * @param payload
     * @return
     */
    protected ResponseEntity<?> createAcceptedReponse(Object payload) {
        return createResponse(payload, HttpStatus.ACCEPTED);
    }

    /**
     * 用户删除数据成功。
     * 
     * @param payload
     * @return
     */
    protected ResponseEntity<?> createNoContentReponse() {
        return createResponse(HttpStatus.NO_CONTENT);
    }

    /**
     * 处理spring validation校验后的错误信息
     * 
     * @param result
     */
    protected void processValidations(BindingResult result) {

        if (result.hasErrors()) {
            Map<String, String> map = new HashMap<>();

            result.getFieldErrors()
                    .forEach(err -> map.put(err.getField(), err.getDefaultMessage()));
            throw new UnprocesableEntityException("fields invalid", map);
        }
    }

}
