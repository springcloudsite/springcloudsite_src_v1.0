package com.scs.usermanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.scs.framework.core.web.controller.AbstractRestController;
import com.scs.usermanagement.domainmodel.SysUser;
import com.scs.usermanagement.service.SysUserService;
import com.scs.usermanagement.vo.LoginUserVO;
import com.scs.usermanagement.vo.LoginVO;
import com.scs.usermanagement.vo.SearchUser;
import com.scs.usermanagement.vo.SearchUserRole;
import com.scs.usermanagement.vo.SysUserVO;
import com.scs.usermanagement.vo.UserRoleVO;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 用户管理
 * 
 *
 * @version Scs-DI v1.0
 * @author Sun Yunxu, 2018年10月8日
 */
@RestController
public class SysUserController extends AbstractRestController<SysUser, Integer> {

    @Autowired
    private SysUserService sysUserService;

    @Override
    protected SysUserService getService() {
        return sysUserService;
    }

    @ApiOperation(value = "查询用户", notes = "查询所有用户信息")
    @GetMapping("/sysusers/all")
    public ResponseEntity<List<SysUser>> getAllUsers() {
        @SuppressWarnings("unchecked")
        ResponseEntity<List<SysUser>> entity = (ResponseEntity<List<SysUser>>) createOKResponse(
                getService().findAll());
        return entity;
    }

    @ApiOperation(value = "查询用户", notes = "根据查询条件查询所有用户信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "当前页", paramType = "query", required = true, dataType = "Integer"),
        @ApiImplicitParam(name = "limit", value = "每页显示条数", paramType = "query", required = true, dataType = "Integer"),
        @ApiImplicitParam(name = "sort", value = "排序条件", paramType = "query", required = false, dataType = "String"),
        @ApiImplicitParam(name = "search", value = "用户查询条件", paramType = "query", required = true, dataType = "SearchUser") })
    @GetMapping("/sysusers/all/search/pager")
    public ResponseEntity<PageList<SysUser>> getAllUsersBySearchUser(@RequestParam("page") int page,
                                                                     @RequestParam("limit") int limit,
                                                                     @RequestParam(required = false) String sort,
                                                                     SearchUser search) {
        PageBounds pageBounds = new PageBounds(page, limit, Order.formString(sort));
        @SuppressWarnings("unchecked")
        ResponseEntity<PageList<SysUser>> entity = (ResponseEntity<PageList<SysUser>>) createOKResponse(
                getService().findPagerUsersBySearchUser(pageBounds, search));
        return entity;
    }

    @ApiOperation(value = "查询用户", notes = "根据查询条件查询所有可用用户信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "当前页", paramType = "query", required = true, dataType = "Integer"),
        @ApiImplicitParam(name = "limit", value = "每页显示条数", paramType = "query", required = true, dataType = "Integer"),
        @ApiImplicitParam(name = "sort", value = "排序条件", paramType = "query", required = false, dataType = "String"),
        @ApiImplicitParam(name = "search", value = "用户查询条件", paramType = "query", required = true, dataType = "SearchUser") })
    @GetMapping("/sysusers/allenable/search/pager")
    public ResponseEntity<PageList<SysUser>> getAllEnableUsersBySearchUser(@RequestParam("page") int page,
                                                                           @RequestParam("limit") int limit,
                                                                           @RequestParam(required = false) String sort,
                                                                           SearchUser search) {
        PageBounds pageBounds = new PageBounds(page, limit, Order.formString(sort));
        @SuppressWarnings("unchecked")
        ResponseEntity<PageList<SysUser>> entity = (ResponseEntity<PageList<SysUser>>) createOKResponse(
                getService().findPagerEnableUsersBySearchUser(pageBounds, search));
        return entity;
    }

    @ApiOperation(value = "查询用户", notes = "根据查询条件查询所有未删除用户信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "当前页", paramType = "query", required = true, dataType = "Integer"),
        @ApiImplicitParam(name = "limit", value = "每页显示条数", paramType = "query", required = true, dataType = "Integer"),
        @ApiImplicitParam(name = "sort", value = "排序条件", paramType = "query", required = false, dataType = "String"),
        @ApiImplicitParam(name = "search", value = "用户查询条件", paramType = "query", required = true, dataType = "SearchUser") })
    @GetMapping("/sysusers/allundeleted/search/pager")
    public ResponseEntity<PageList<SysUser>> getAllUndeletedUsersBySearchUser(@RequestParam("page") int page,
                                                                              @RequestParam("limit") int limit,
                                                                              @RequestParam(required = false) String sort,
                                                                              SearchUser search) {
        PageBounds pageBounds = new PageBounds(page, limit, Order.formString(sort));
        @SuppressWarnings("unchecked")
        ResponseEntity<PageList<SysUser>> entity = (ResponseEntity<PageList<SysUser>>) createOKResponse(
                getService().findPagerUndeletedUsersBySearchUser(pageBounds, search));
        return entity;
    }

    @ApiOperation(value = "查询用户与角色关系", notes = "根据查询条件查询用户与角色信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "当前页", paramType = "query", required = true, dataType = "Integer"),
        @ApiImplicitParam(name = "limit", value = "每页显示条数", paramType = "query", required = true, dataType = "Integer"),
        @ApiImplicitParam(name = "sort", value = "排序条件", paramType = "query", required = false, dataType = "String"),
        @ApiImplicitParam(name = "search", value = "用户查询条件", paramType = "query", required = true, dataType = "SearchUserRole") })
    @GetMapping("/sysusers/userrole/search/pager")
    public ResponseEntity<PageList<UserRoleVO>> getUserRolesBySearchUserRole(@RequestParam("page") int page,
                                                                             @RequestParam("limit") int limit,
                                                                             @RequestParam(required = false) String sort,
                                                                             SearchUserRole search) {
        PageBounds pageBounds = new PageBounds(page, limit, Order.formString(sort));
        @SuppressWarnings("unchecked")
        ResponseEntity<PageList<UserRoleVO>> entity = (ResponseEntity<PageList<UserRoleVO>>) createOKResponse(
                getService().findPagerUserRolesBySearchUserRole(pageBounds, search));
        return entity;
    }

    @ApiOperation(value = "查询用户", notes = "根据用户主键查询用户信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "用户主键", paramType = "path", required = true, dataType = "Integer") })
    @GetMapping("/sysuser/id/{id}")
    public ResponseEntity<SysUser> getUserById(@PathVariable("id") int id) {
        @SuppressWarnings("unchecked")
        ResponseEntity<SysUser> entity = (ResponseEntity<SysUser>) createOKResponse(
                getService().findOne(id));
        return entity;
    }

    @ApiOperation(value = "查询用户", notes = "根据用户主键查询用户信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "login", value = "登录信息", required = true, dataType = "LoginVO") })
    @GetMapping("/sysuser/checklogin")
    public ResponseEntity<LoginUserVO> checkLogin(@Validated LoginVO login, BindingResult result) {
        processValidations(result);
        @SuppressWarnings("unchecked")
        ResponseEntity<LoginUserVO> entity = (ResponseEntity<LoginUserVO>) createOKResponse(
                getService().checkLogin(login));
        return entity;
    }

    @ApiOperation(value = "登录对象", notes = "根据用户主键获取登录对象")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "用户主键", paramType = "path", required = true, dataType = "Integer") })
    @GetMapping("/sysuser/login/id/{id}")
    public ResponseEntity<LoginUserVO> findLoginUserVOById(@PathVariable("id") Integer id) {
        @SuppressWarnings("unchecked")
        ResponseEntity<LoginUserVO> entity = (ResponseEntity<LoginUserVO>) createOKResponse(
                getService().findLoginUserVOById(id));
        return entity;
    }

    @ApiOperation(value = "新增用户", notes = "新增用户信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "sysUser", value = "用户实体SysUser", required = true, dataType = "SysUser") })
    @PostMapping("/sysuser")
    public ResponseEntity<SysUserVO> addUser(@RequestBody @Validated SysUser sysUser,
                                             BindingResult result) {
        processValidations(result);
        @SuppressWarnings("unchecked")
        ResponseEntity<SysUserVO> entity = (ResponseEntity<SysUserVO>) createCreatedResponse(
                getService().addNewUser(sysUser));
        return entity;
    }

    @ApiOperation(value = "修改用户", notes = "根据主键修改用户信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "sysUser", value = "用户实体SysUser", required = true, dataType = "SysUser") })
    @PatchMapping("/sysuser")
    public ResponseEntity<SysUserVO> modifyUser(@RequestBody @Validated SysUser sysUser,
                                                BindingResult result) {
        processValidations(result);
        @SuppressWarnings("unchecked")
        ResponseEntity<SysUserVO> entity = (ResponseEntity<SysUserVO>) createCreatedResponse(
                getService().modifyUserById(sysUser));
        return entity;
    }

    @ApiOperation(value = "修改用户", notes = "根据主键重置用户密码")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "用户主键", required = true, dataType = "Integer") })
    @PatchMapping("/sysuser/resetpassword/id/{id}")
    public ResponseEntity<SysUserVO> resetPassword(@PathVariable("id") Integer id) {
        @SuppressWarnings("unchecked")
        ResponseEntity<SysUserVO> entity = (ResponseEntity<SysUserVO>) createCreatedResponse(
                getService().resetPassword(id));
        return entity;
    }

    @ApiOperation(value = "删除用户", notes = "根据主键删除用户信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "用户主键", required = true, dataType = "Integer") })
    @DeleteMapping("/sysuser/id/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable("id") Integer id) {
        getService().delete(id);
        return createNoContentReponse();
    }
}
