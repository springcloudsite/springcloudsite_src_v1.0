/*
 * Copyright (c) Pactera Corp.
 * All Rights Reserved.
 */
package com.scs.framework.core.dao;

import java.util.List;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;

/**
 * Mybatis基类
 *
 * @version ScsDI v1.0
 * @author Zhang Ran, 2018年4月19日
 */
public interface MyBatisBaseDao<T, I> {

    int insert(T paramT);

    /**
     * 为空的字段不操作.
     * 
     * @param paramT
     * @return
     */
    int insertSelective(T paramT);

    int deleteByPrimaryKey(I paramI);

    int deleteBatchByPrimaryKeys(List<I> paramList);

    int updateByPrimaryKey(T paramT);

    /**
     * 为空的字段不操作.
     * 
     * @param paramT
     * @return
     */
    int updateSelectiveByPrimaryKey(T paramT);

    T selectByPrimaryKey(I paramI);

    List<T> selectBatchByPrimaryKeys(List<I> paramList);

    PageList<T> selectPage(PageBounds pageBounds, T paramT);
    
    List<T> selectAll(T paramT);
}
