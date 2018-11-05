package com.scs.usermanagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.scs.framework.core.service.AbstractBaseService;
import com.scs.framework.core.web.context.RequestHeaderUtils;
import com.scs.framework.utils.ScsBeanUtils;
import com.scs.usermanagement.dao.SysFunctionDao;
import com.scs.usermanagement.domainmodel.SysFunction;
import com.scs.usermanagement.util.Const;
import com.scs.usermanagement.vo.FunctionOriginVO;
import com.scs.usermanagement.vo.FunctionRoleVO;
import com.scs.usermanagement.vo.MenuFunctionVO;
import com.scs.usermanagement.vo.SearchFunctionRole;
import com.scs.usermanagement.vo.SysFunctionVO;

/**
 * 资源管理
 * 
 *
 * @version Scs-DI v1.0
 * @author Sun Yunxu, 2018年9月27日
 */
@Service
public class SysFunctionService extends AbstractBaseService<SysFunction, Integer> {

    @Autowired
    private SysFunctionDao sysFunctionDao;

    /**
     * 超级管理员登录名
     */
    @Value("${admin.loginName:administrator}")
    private String adminLoginName;
    /**
     * 游客角色编码
     */
    @Value("${role.anyones:ANYONES}")
    private String anyones;

    @Override
    protected SysFunctionDao getDao() {
        return sysFunctionDao;
    }

    /**
     * 根据资源类型查询资源信息
     * 
     * @param functionType
     * @return
     */
    public List<SysFunction> findFunctionsByType(String functionType) {
        SysFunction params = new SysFunction();
        params.setFunctionType(functionType);
        List<SysFunction> list = getDao().selectAll(params);
        return list;
    }

    /**
     * 根据资源类型查询可用资源信息
     * 
     * @param functionType
     * @return
     */
    public List<SysFunction> findEnableFunctionsByType(String functionType) {
        SysFunction params = new SysFunction();
        params.setFunctionType(functionType);
        params.setIsDisable(Const.IS_DISABLE_NO);
        List<SysFunction> list = getDao().selectAll(params);
        return list;
    }

    /**
     * 根据资源类型和父级Id查询资源信息
     * 
     * @param functionType
     * @return
     */
    public List<SysFunction> findFunctionsByTypeAndParentId(String functionType, Integer parentId) {
        SysFunction params = new SysFunction();
        params.setFunctionType(functionType);
        params.setParentId(parentId);
        List<SysFunction> list = getDao().selectAll(params);
        return list;
    }

    /**
     * 根据资源类型和父级Id查询可用资源信息
     * 
     * @param functionType
     * @return
     */
    public List<SysFunction> findEnableFunctionsByTypeAndParentId(String functionType,
                                                                  Integer parentId) {
        SysFunction params = new SysFunction();
        params.setFunctionType(functionType);
        params.setParentId(parentId);
        params.setIsDisable(Const.IS_DISABLE_NO);
        List<SysFunction> list = getDao().selectAll(params);
        return list;
    }

    /**
     * 根据ID查询资源名称
     * 
     * @param id
     * @return
     */
    public SysFunctionVO findFunctionById(Integer id) {
        SysFunction ftn = getDao().selectByPrimaryKey(id);
        SysFunctionVO result = new SysFunctionVO();
        ScsBeanUtils.copyProperties(ftn, result);
        if (ftn.getParentId() != null) {
            SysFunction pFtn = getDao().selectByPrimaryKey(ftn.getParentId());
            result.setParentFtnName(pFtn.getFunctionName());
        }
        return result;
    }

    /**
     * 根据查询条件查询资源与角色关联信息（分页）
     * 
     * @param functionType
     * @return
     */
    public List<FunctionRoleVO> findFunctionRolesBySearchFunctionRole(SearchFunctionRole search) {
        List<FunctionRoleVO> list = getDao().selectFunctionRoleBySearchFunctionRole(search);
        return list;
    }

    /**
     * 根据资源类型和父级Id查询可用资源信息
     * 
     * @param functionType
     * @return
     */
    public List<MenuFunctionVO> findMenuByOrigin() {
        List<MenuFunctionVO> list = null;
        FunctionOriginVO search = new FunctionOriginVO();
        search.setFunctionType("1");
        search.setIsDisable(Const.IS_DISABLE_NO);
        if (adminLoginName.equals(RequestHeaderUtils.getUserName())) {
            list = getDao().selectFunctionByAdmin(search);
        } else {
            List<String> roleCodes = RequestHeaderUtils.getUserRole();
            roleCodes.add(anyones);
            search.setRoleCodes(roleCodes);
            list = getDao().selectFunctionByOrigin(search);
        }
        return list;
    }
}
