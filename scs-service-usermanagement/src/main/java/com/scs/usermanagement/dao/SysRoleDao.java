package com.scs.usermanagement.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.scs.framework.core.dao.MyBatisBaseDao;
import com.scs.usermanagement.domainmodel.SysRole;
import com.scs.usermanagement.vo.SearchRole;

/**
 *
 * 表sys_role对应的基于MyBatis实现的Dao接口<br/>
 * 在其中添加自定义方法
 *
 */
@Mapper
public interface SysRoleDao extends MyBatisBaseDao<SysRole, Integer> {
    /**
     * 根据角色编码查询数据
     * 
     * @param roleCode
     * @param isDelete
     * @return
     */
    List<SysRole> selectByRoleCode(@Param("roleCode") String roleCode,
                                   @Param("isDelete") String isDelete);

    /**
     * 根据查询条件查询用户数据（分页）
     * 
     * @param pageBounds
     * @param search
     * @return
     */
    PageList<SysRole> selectPageBySearchRole(PageBounds pageBounds, SearchRole search);

    /**
     * 根据用户组件查询其关联的角色编码
     * 
     * @param userId
     * @return
     */
    List<String> selectRoleCodesByUserId(@Param("userId") Integer userId);
}