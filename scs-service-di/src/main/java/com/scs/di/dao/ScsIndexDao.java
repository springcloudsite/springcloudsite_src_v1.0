package com.scs.di.dao;

import org.apache.ibatis.annotations.Mapper;

import com.scs.di.domainmodel.ScsIndex;
import com.scs.framework.core.dao.MyBatisBaseDao;

/**
 *
 * 表scs_index对应的基于MyBatis实现的Dao接口<br/>
 * 在其中添加自定义方法
 *
 */
@Mapper
public interface ScsIndexDao extends MyBatisBaseDao<ScsIndex, Integer> {

}