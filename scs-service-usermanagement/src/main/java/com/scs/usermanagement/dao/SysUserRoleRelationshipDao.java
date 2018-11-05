package com.scs.usermanagement.dao;

import org.apache.ibatis.annotations.Mapper;

import com.scs.usermanagement.domainmodel.SysUserRoleRelationship;
import com.scs.framework.core.dao.MyBatisBaseDao;

/**
 *
 * 表sys_user_role_relationship对应的基于MyBatis实现的Dao接口<br/>
 * 在其中添加自定义方法
 *
 */
@Mapper
public interface SysUserRoleRelationshipDao extends MyBatisBaseDao<SysUserRoleRelationship, Integer> {

}