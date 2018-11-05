package com.scs.di.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.scs.di.domainmodel.ScsObject;
import com.scs.di.vo.SearchObject;
import com.scs.di.vo.TableObjectVO;
import com.scs.framework.core.dao.MyBatisBaseDao;

/**
 *
 * 表scs_object对应的基于MyBatis实现的Dao接口<br/>
 * 在其中添加自定义方法
 *
 */
@Mapper
public interface ScsObjectDao extends MyBatisBaseDao<ScsObject, Integer> {
    /**
     * 根据查询条件查询分析对象（分页）
     * 
     * @param pageBounds
     * @param search
     * @return
     */
    PageList<TableObjectVO> selectPageBySearchObject(PageBounds pageBounds, SearchObject search);

    /**
     * 根据查询条件查询分析对象
     * 
     * @param pageBounds
     * @param search
     * @return
     */
    List<TableObjectVO> selectBySearchObject(SearchObject search);

    /**
     * 根据业务主键查询分析对象
     * 
     * @param businessId
     * @return
     */
    List<ScsObject> selectByBusinessId(@Param("businessid") String businessId);
}