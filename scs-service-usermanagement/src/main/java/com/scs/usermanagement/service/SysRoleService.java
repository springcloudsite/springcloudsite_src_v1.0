package com.scs.usermanagement.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.scs.framework.core.service.AbstractBaseService;
import com.scs.usermanagement.dao.SysRoleDao;
import com.scs.usermanagement.domainmodel.SysRole;
import com.scs.usermanagement.util.Const;
import com.scs.usermanagement.vo.SearchRole;

/**
 * 
 * 角色管理
 * 
 *
 * @version Scs-DI v1.0
 * @author Sun Yunxu, 2018年10月10日
 */
@Service
public class SysRoleService extends AbstractBaseService<SysRole, Integer> {

    @Autowired
    private SysRoleDao sysRoleDao;

    @Override
    protected SysRoleDao getDao() {
        return sysRoleDao;
    }

    private static final String FIELD_ROLE_CODE = "roleCode";

    /**
     * 根据查询条件查询角色信息（分页）
     * 
     * @param functionType
     * @return
     */
    public PageList<SysRole> findPagerRolesBySearchRole(PageBounds pageBounds, SearchRole search) {
        PageList<SysRole> list = getDao().selectPageBySearchRole(pageBounds, search);
        return list;
    }

    /**
     * 根据查询条件查询可用角色信息（分页）
     * 
     * @param functionType
     * @return
     */
    public PageList<SysRole> findPagerEnableRolesBySearchRole(PageBounds pageBounds,
                                                              SearchRole search) {
        search.setIsDisable(Const.IS_DISABLE_NO);
        search.setIsDelete(Const.IS_DISABLE_NO);
        PageList<SysRole> list = getDao().selectPageBySearchRole(pageBounds, search);
        return list;
    }

    /**
     * 根据查询条件查询未删除角色信息（分页）
     * 
     * @param functionType
     * @return
     */
    public PageList<SysRole> findPagerUndeletedRolesBySearchRole(PageBounds pageBounds,
                                                                 SearchRole search) {
        search.setIsDelete(Const.IS_DISABLE_NO);
        PageList<SysRole> list = getDao().selectPageBySearchRole(pageBounds, search);
        return list;
    }

    /**
     * 根据用户组件查询其关联的角色编码
     * 
     * @param userId
     * @return
     */
    public List<String> findRoleCodesByUserId(Integer userId) {
        List<String> list = null;
        if (userId != null) {
            list = getDao().selectRoleCodesByUserId(userId);
        }
        return list;
    }

    @Override
    protected void addCheck(SysRole role) {
        super.addCheck(role);
        List<FieldError> errors = new ArrayList<>();
        List<SysRole> list = getDao().selectByRoleCode(role.getRoleCode(), Const.IS_DELETE_NO);
        if (!list.isEmpty()) {
            errors.add(new FieldError(FIELD_ROLE_CODE, FIELD_ROLE_CODE, "角色编码已被使用"));
        }
        super.processValidations(errors);
    }

    @Override
    protected void updateCheck(SysRole role) {
        super.updateCheck(role);
        List<FieldError> errors = new ArrayList<>();
        List<SysRole> list = getDao().selectByRoleCode(role.getRoleCode(), Const.IS_DELETE_NO);
        if (!list.isEmpty() && isDuplicateLoginName(list, role.getId())) {
            errors.add(new FieldError(FIELD_ROLE_CODE, FIELD_ROLE_CODE, "角色编码已被使用"));
        }
        super.processValidations(errors);
    }

    /**
     * 修改数据时判断角色编码是否重复
     * 
     * @param list
     * @param id
     * @return
     */
    private boolean isDuplicateLoginName(List<SysRole> list, Integer id) {
        return list.size() > 1 || !list.get(0).getId().equals(id);
    }
}
