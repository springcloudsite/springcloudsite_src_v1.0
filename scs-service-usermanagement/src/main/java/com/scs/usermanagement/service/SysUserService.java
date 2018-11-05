package com.scs.usermanagement.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.scs.framework.core.service.AbstractBaseService;
import com.scs.framework.utils.MD5Util;
import com.scs.framework.utils.ScsBeanUtils;
import com.scs.usermanagement.dao.SysUserDao;
import com.scs.usermanagement.domainmodel.SysUser;
import com.scs.usermanagement.util.Const;
import com.scs.usermanagement.vo.LoginUserVO;
import com.scs.usermanagement.vo.LoginVO;
import com.scs.usermanagement.vo.SearchUser;
import com.scs.usermanagement.vo.SearchUserRole;
import com.scs.usermanagement.vo.SysUserVO;
import com.scs.usermanagement.vo.UserRoleVO;

/**
 * 用户管理
 * 
 *
 * @version Scs-DI v1.0
 * @author Sun Yunxu, 2018年10月8日
 */
@Service
public class SysUserService extends AbstractBaseService<SysUser, Integer> {

    @Autowired
    private SysUserDao sysUserDao;

    @Autowired
    private SysRoleService sysRoleService;

    @Override
    protected SysUserDao getDao() {
        return sysUserDao;
    }

    /**
     * 超级管理员登录名
     */
    @Value("${admin.loginName:administrator}")
    private String adminLoginName;
    /**
     * 超级管理员登录密码
     */
    @Value("${admin.password:pacteraadmin}")
    private String adminPwd;
    /**
     * 超级管理员昵称
     */
    @Value("${admin.nickname:超级管理员}")
    private String adminNickname;
    /**
     * 默认密码
     */
    @Value("${default.password:pactera123}")
    private String defaultPwd;

    private static final String FIELD_LOGIN_NAME = "loginName";
    private static final String FIELD_PASSWORD = "password";

    /**
     * 根据查询条件查询用户信息（分页）
     * 
     * @param functionType
     * @return
     */
    public PageList<SysUser> findPagerUsersBySearchUser(PageBounds pageBounds, SearchUser search) {
        PageList<SysUser> list = getDao().selectPageBySearchUser(pageBounds, search);
        return list;
    }

    /**
     * 根据查询条件查询可用用户信息（分页）
     * 
     * @param functionType
     * @return
     */
    public PageList<SysUser> findPagerEnableUsersBySearchUser(PageBounds pageBounds,
                                                              SearchUser search) {
        search.setIsDisable(Const.IS_DISABLE_NO);
        search.setIsDelete(Const.IS_DISABLE_NO);
        PageList<SysUser> list = getDao().selectPageBySearchUser(pageBounds, search);
        return list;
    }

    /**
     * 根据查询条件查询未删除用户信息（分页）
     * 
     * @param functionType
     * @return
     */
    public PageList<SysUser> findPagerUndeletedUsersBySearchUser(PageBounds pageBounds,
                                                                 SearchUser search) {
        search.setIsDelete(Const.IS_DISABLE_NO);
        PageList<SysUser> list = getDao().selectPageBySearchUser(pageBounds, search);
        return list;
    }

    /**
     * 根据查询条件查询用户与角色关联信息（分页）
     * 
     * @param functionType
     * @return
     */
    public PageList<UserRoleVO> findPagerUserRolesBySearchUserRole(PageBounds pageBounds,
                                                                   SearchUserRole search) {
        search.setIsDelete(Const.IS_DISABLE_NO);
        PageList<UserRoleVO> list = getDao().selectPageUserRoleBySearchUserRole(pageBounds, search);
        return list;
    }

    /**
     * 用户登录校验
     * 
     * @param login
     * @return
     */
    public LoginUserVO checkLogin(LoginVO login) {
        LoginUserVO result = null;
        if (adminLoginName.equals(login.getLoginName())) {
            if (MD5Util.MD5Encode(adminPwd).equals(login.getPassword())) {
                SysUser user = new SysUser();
                user.setLoginName(adminLoginName);
                user.setNickname(adminNickname);
                user.setId(-1);
                result = createLoginUser(user);
            } else {
                this.processValidations(FIELD_PASSWORD, "用户名或密码错误");
            }
        } else {
            SysUser user = new SysUser();
            user.setLoginName(login.getLoginName());
            user.setIsDelete(Const.IS_DELETE_NO);
            user.setIsDisable(Const.IS_DISABLE_NO);
            List<SysUser> list = getDao().selectAll(user);

            if (!list.isEmpty()) {
                user = list.get(0);
                if (login.getPassword().equals(user.getPassword())) {
                    result = createLoginUser(user);
                } else {
                    this.processValidations(FIELD_PASSWORD, "用户名或密码错误");
                }
            } else {
                this.processValidations(FIELD_LOGIN_NAME, "用户名不存在");
            }
        }
        if (result == null) {
            result = new LoginUserVO();
        }
        return result;
    }

    /**
     * 创建登录用户返回对象
     * 
     * @param user
     * @return
     */
    private LoginUserVO createLoginUser(SysUser user) {
        LoginUserVO result = new LoginUserVO();
        ScsBeanUtils.copyNoNullProperties(user, result);
        result.setUserId(user.getId());
        if (!adminLoginName.equals(user.getLoginName())) {
            // 查询用后角色编码
            List<String> roleCodes = sysRoleService.findRoleCodesByUserId(user.getId());
            result.setRoleCodes(roleCodes);
        }
        return result;
    }

    /**
     * 添加新用户（密码为空时，自动添加默认密码）
     * 
     * @param user
     * @return
     */
    public SysUserVO addNewUser(SysUser user) {
        if (StringUtils.isBlank(user.getPassword())) {
            user.setPassword(MD5Util.MD5Encode(defaultPwd));
        }
        user = super.add(user);
        SysUserVO result = new SysUserVO();
        ScsBeanUtils.copyNoNullProperties(user, result);
        return result;
    }

    /**
     * 修改用户信息
     * 
     * @param user
     * @return
     */
    public SysUserVO modifyUserById(SysUser user) {
        user = super.updatePartial(user);
        SysUserVO result = new SysUserVO();
        ScsBeanUtils.copyNoNullProperties(user, result);
        return result;
    }

    /**
     * 根据用户主键重置用户密码
     * 
     * @param user
     * @return
     */
    public SysUserVO resetPassword(Integer id) {
        SysUser user = new SysUser();
        user.setId(id);
        user.setPassword(MD5Util.MD5Encode(defaultPwd));
        SysUserVO result = this.modifyUserById(user);
        return result;
    }

    /**
     * 根据用户主键查询登录对象
     * 
     * @param id
     * @return
     */
    public LoginUserVO findLoginUserVOById(Integer id) {
        LoginUserVO result = null;
        if (id.equals(-1)) {
            SysUser user = new SysUser();
            user.setLoginName(adminLoginName);
            user.setNickname(adminNickname);
            user.setId(-1);
            result = createLoginUser(user);
        } else {
            SysUser user = new SysUser();
            user.setId(id);
            user.setIsDelete(Const.IS_DELETE_NO);
            user.setIsDisable(Const.IS_DISABLE_NO);
            List<SysUser> list = getDao().selectAll(user);

            if (!list.isEmpty()) {
                user = list.get(0);
                result = createLoginUser(user);
            }
        }
        return result;
    }

    @Override
    protected void addCheck(SysUser user) {
        super.addCheck(user);
        List<FieldError> errors = new ArrayList<>();
        List<SysUser> list = getDao().selectByLoginName(user.getLoginName(), Const.IS_DELETE_NO);
        if (!list.isEmpty()) {
            errors.add(new FieldError(FIELD_LOGIN_NAME, FIELD_LOGIN_NAME, "登录名称已被使用"));
        }
        super.processValidations(errors);
    }

    @Override
    protected void updateCheck(SysUser user) {
        super.updateCheck(user);
        List<FieldError> errors = new ArrayList<>();
        List<SysUser> list = getDao().selectByLoginName(user.getLoginName(), Const.IS_DELETE_NO);
        if (!list.isEmpty() && isDuplicateLoginName(list, user.getId())) {
            errors.add(new FieldError(FIELD_LOGIN_NAME, FIELD_LOGIN_NAME, "登录名称已被使用"));
        }
        super.processValidations(errors);
    }

    /**
     * 修改数据时判断登录名称是否重复
     * 
     * @param list
     * @param id
     * @return
     */
    private boolean isDuplicateLoginName(List<SysUser> list, Integer id) {
        return list.size() > 1 || !list.get(0).getId().equals(id);
    }
}
