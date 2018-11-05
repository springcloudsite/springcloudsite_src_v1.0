package com.scs.usermanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scs.framework.core.service.AbstractBaseService;
import com.scs.usermanagement.dao.SysRoleFunctionRelationshipDao;
import com.scs.usermanagement.domainmodel.SysRoleFunctionRelationship;

/**
 * 资源与角色关联
 * 
 *
 * @version Scs-DI v1.0
 * @author Sun Yunxu, 2018年10月11日
 */
@Service
public class SysRoleFunctionRelationshipService
        extends AbstractBaseService<SysRoleFunctionRelationship, Integer> {

    @Autowired
    private SysRoleFunctionRelationshipDao sysRoleFunctionRelationshipDao;

    @Override
    protected SysRoleFunctionRelationshipDao getDao() {
        return sysRoleFunctionRelationshipDao;
    }
}
