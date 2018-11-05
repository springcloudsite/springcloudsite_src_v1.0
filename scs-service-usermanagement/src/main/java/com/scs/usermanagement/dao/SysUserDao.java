package com.scs.usermanagement.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.scs.framework.core.dao.MyBatisBaseDao;
import com.scs.usermanagement.domainmodel.SysUser;
import com.scs.usermanagement.vo.SearchUser;
import com.scs.usermanagement.vo.SearchUserRole;
import com.scs.usermanagement.vo.UserRoleVO;

/**
 *
 * 表sys_user对应的基于MyBatis实现的Dao接口<br/>
 * 在其中添加自定义方法
 *
 */
@Mapper
public interface SysUserDao extends MyBatisBaseDao<SysUser, Integer> {
    /**
     * 根据登录名称查询数据
     * 
     * @param loginName
     * @param isDelete
     * @return
     */
    List<SysUser> selectByLoginName(@Param("loginName") String loginName,
                                    @Param("isDelete") String isDelete);

    /**
     * 根据查询条件查询用户数据（分页）
     * 
     * @param pageBounds
     * @param search
     * @return
     */
    PageList<SysUser> selectPageBySearchUser(PageBounds pageBounds, SearchUser search);

    /**
     * 根据用户与角色关系条件查询用户对象数据（分页）
     * 
     * @param pageBounds
     * @param search
     * @return
     */
    PageList<UserRoleVO> selectPageUserRoleBySearchUserRole(PageBounds pageBounds,
                                                            SearchUserRole search);
}