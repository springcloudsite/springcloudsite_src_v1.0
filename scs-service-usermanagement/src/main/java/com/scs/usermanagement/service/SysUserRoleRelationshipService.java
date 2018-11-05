package com.scs.usermanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scs.framework.core.service.AbstractBaseService;
import com.scs.usermanagement.dao.SysUserRoleRelationshipDao;
import com.scs.usermanagement.domainmodel.SysUserRoleRelationship;

/**
 * 用户与角色关联
 * 
 *
 * @version Scs-DI v1.0
 * @author Sun Yunxu, 2018年10月11日
 */
@Service
public class SysUserRoleRelationshipService
        extends AbstractBaseService<SysUserRoleRelationship, Integer> {

    @Autowired
    private SysUserRoleRelationshipDao sysUserRoleRelationshipDao;

    @Override
    protected SysUserRoleRelationshipDao getDao() {
        return sysUserRoleRelationshipDao;
    }
}
