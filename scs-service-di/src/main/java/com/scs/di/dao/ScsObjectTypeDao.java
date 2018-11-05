package com.scs.di.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.scs.di.domainmodel.ScsObjectType;
import com.scs.di.vo.SearchObjectType;
import com.scs.framework.core.dao.MyBatisBaseDao;

/**
 *
 * 表scs_object_type对应的基于MyBatis实现的Dao接口<br/>
 * 在其中添加自定义方法
 *
 */
@Mapper
public interface ScsObjectTypeDao extends MyBatisBaseDao<ScsObjectType, Integer> {
    /**
     * 根据查询条件查询分析对象类型（分页）
     * 
     * @param pageBounds
     * @param search
     * @return
     */
    PageList<ScsObjectType> selectPageBySearchObjectType(PageBounds pageBounds,
                                                         SearchObjectType search);

    /**
     * 根据查询条件查询分析对象类型
     * 
     * @param pageBounds
     * @param search
     * @return
     */
    List<ScsObjectType> selectBySearchObjectType(SearchObjectType search);
}