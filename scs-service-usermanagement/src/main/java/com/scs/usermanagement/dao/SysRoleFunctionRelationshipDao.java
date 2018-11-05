package com.scs.usermanagement.dao;

import org.apache.ibatis.annotations.Mapper;

import com.scs.usermanagement.domainmodel.SysRoleFunctionRelationship;
import com.scs.framework.core.dao.MyBatisBaseDao;

/**
 *
 * 表sys_role_function_relationship对应的基于MyBatis实现的Dao接口<br/>
 * 在其中添加自定义方法
 *
 */
@Mapper
public interface SysRoleFunctionRelationshipDao extends MyBatisBaseDao<SysRoleFunctionRelationship, Integer> {

}