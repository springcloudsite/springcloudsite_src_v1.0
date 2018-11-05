package com.scs.usermanagement.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.scs.framework.core.dao.MyBatisBaseDao;
import com.scs.usermanagement.domainmodel.SysFunction;
import com.scs.usermanagement.vo.FunctionOriginVO;
import com.scs.usermanagement.vo.FunctionRoleVO;
import com.scs.usermanagement.vo.MenuFunctionVO;
import com.scs.usermanagement.vo.SearchFunctionRole;

/**
 *
 * 表sys_function对应的基于MyBatis实现的Dao接口<br/>
 * 在其中添加自定义方法
 *
 */
@Mapper
public interface SysFunctionDao extends MyBatisBaseDao<SysFunction, Integer> {
    /**
     * 根据查询条件查询资源与角色信息
     * 
     * @param search
     * @return
     */
    List<FunctionRoleVO> selectFunctionRoleBySearchFunctionRole(SearchFunctionRole search);

    /**
     * 根据角色权限查询资源信息
     * 
     * @param search
     * @return
     */
    List<MenuFunctionVO> selectFunctionByOrigin(FunctionOriginVO search);

    /**
     * 根据条件查询超级用户资源对象数据
     * 
     * @param search
     * @return
     */
    List<MenuFunctionVO> selectFunctionByAdmin(FunctionOriginVO search);
}