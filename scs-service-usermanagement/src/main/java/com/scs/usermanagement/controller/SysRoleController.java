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
import com.scs.usermanagement.domainmodel.SysRole;
import com.scs.usermanagement.service.SysRoleService;
import com.scs.usermanagement.vo.SearchRole;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 角色管理
 * 
 *
 * @version Scs-DI v1.0
 * @author Sun Yunxu, 2018年10月10日
 */
@RestController
public class SysRoleController extends AbstractRestController<SysRole, Integer> {

    @Autowired
    private SysRoleService sysRoleService;

    @Override
    protected SysRoleService getService() {
        return sysRoleService;
    }

    @ApiOperation(value = "查询角色", notes = "查询所有角色信息")
    @GetMapping("/sysroles/all")
    public ResponseEntity<List<SysRole>> getAllRoles() {
        @SuppressWarnings("unchecked")
        ResponseEntity<List<SysRole>> entity = (ResponseEntity<List<SysRole>>) createOKResponse(
                getService().findAll());
        return entity;
    }

    @ApiOperation(value = "查询角色", notes = "根据查询条件查询所有角色信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "当前页", paramType = "query", required = true, dataType = "Integer"),
        @ApiImplicitParam(name = "limit", value = "每页显示条数", paramType = "query", required = true, dataType = "Integer"),
        @ApiImplicitParam(name = "sort", value = "排序条件", paramType = "query", required = false, dataType = "String"),
        @ApiImplicitParam(name = "search", value = "角色查询条件", paramType = "query", required = true, dataType = "SearchRole") })
    @GetMapping("/sysroles/all/search/pager")
    public ResponseEntity<PageList<SysRole>> getAllRolesBySearchRole(@RequestParam("page") int page,
                                                                     @RequestParam("limit") int limit,
                                                                     @RequestParam(required = false) String sort,
                                                                     SearchRole search) {
        PageBounds pageBounds = new PageBounds(page, limit, Order.formString(sort));
        @SuppressWarnings("unchecked")
        ResponseEntity<PageList<SysRole>> entity = (ResponseEntity<PageList<SysRole>>) createOKResponse(
                getService().findPagerRolesBySearchRole(pageBounds, search));
        return entity;
    }

    @ApiOperation(value = "查询角色", notes = "根据查询条件查询所有可用角色信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "当前页", paramType = "query", required = true, dataType = "Integer"),
        @ApiImplicitParam(name = "limit", value = "每页显示条数", paramType = "query", required = true, dataType = "Integer"),
        @ApiImplicitParam(name = "sort", value = "排序条件", paramType = "query", required = false, dataType = "String"),
        @ApiImplicitParam(name = "search", value = "角色查询条件", paramType = "query", required = true, dataType = "SearchRole") })
    @GetMapping("/sysroles/allenable/search/pager")
    public ResponseEntity<PageList<SysRole>> getAllEnableRolesBySearchRole(@RequestParam("page") int page,
                                                                           @RequestParam("limit") int limit,
                                                                           @RequestParam(required = false) String sort,
                                                                           SearchRole search) {
        PageBounds pageBounds = new PageBounds(page, limit, Order.formString(sort));
        @SuppressWarnings("unchecked")
        ResponseEntity<PageList<SysRole>> entity = (ResponseEntity<PageList<SysRole>>) createOKResponse(
                getService().findPagerEnableRolesBySearchRole(pageBounds, search));
        return entity;
    }

    @ApiOperation(value = "查询角色", notes = "根据查询条件查询所有未删除的角色信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "当前页", paramType = "query", required = true, dataType = "Integer"),
        @ApiImplicitParam(name = "limit", value = "每页显示条数", paramType = "query", required = true, dataType = "Integer"),
        @ApiImplicitParam(name = "sort", value = "排序条件", paramType = "query", required = false, dataType = "String"),
        @ApiImplicitParam(name = "search", value = "角色查询条件", paramType = "query", required = true, dataType = "SearchRole") })
    @GetMapping("/sysroles/allundeleted/search/pager")
    public ResponseEntity<PageList<SysRole>> getAllUndeletedRolesBySearchRole(@RequestParam("page") int page,
                                                                              @RequestParam("limit") int limit,
                                                                              @RequestParam(required = false) String sort,
                                                                              SearchRole search) {
        PageBounds pageBounds = new PageBounds(page, limit, Order.formString(sort));
        @SuppressWarnings("unchecked")
        ResponseEntity<PageList<SysRole>> entity = (ResponseEntity<PageList<SysRole>>) createOKResponse(
                getService().findPagerUndeletedRolesBySearchRole(pageBounds, search));
        return entity;
    }

    @ApiOperation(value = "查询角色", notes = "根据角色主键查询角色信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "角色主键", paramType = "path", required = true, dataType = "Integer") })
    @GetMapping("/sysrole/id/{id}")
    public ResponseEntity<SysRole> getRoleById(@PathVariable("id") int id) {
        @SuppressWarnings("unchecked")
        ResponseEntity<SysRole> entity = (ResponseEntity<SysRole>) createOKResponse(
                getService().findOne(id));
        return entity;
    }

    @ApiOperation(value = "新增角色", notes = "新增角色信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "sysRole", value = "角色实体SysRole", required = true, dataType = "SysRole") })
    @PostMapping("/sysrole")
    public ResponseEntity<SysRole> addRole(@RequestBody @Validated SysRole sysRole,
                                           BindingResult result) {
        processValidations(result);
        @SuppressWarnings("unchecked")
        ResponseEntity<SysRole> entity = (ResponseEntity<SysRole>) createCreatedResponse(
                getService().add(sysRole));
        return entity;
    }

    @ApiOperation(value = "修改角色", notes = "根据主键修改角色信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "sysRole", value = "角色实体SysRole", required = true, dataType = "SysRole") })
    @PatchMapping("/sysrole")
    public ResponseEntity<SysRole> modifyRole(@RequestBody @Validated SysRole sysRole,
                                              BindingResult result) {
        processValidations(result);
        @SuppressWarnings("unchecked")
        ResponseEntity<SysRole> entity = (ResponseEntity<SysRole>) createCreatedResponse(
                getService().updatePartial(sysRole));
        return entity;
    }

    @ApiOperation(value = "删除角色", notes = "根据主键删除角色信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "角色主键", required = true, dataType = "Integer") })
    @DeleteMapping("/sysrole/id/{id}")
    public ResponseEntity<?> deleteRoleById(@PathVariable("id") Integer id) {
        getService().delete(id);
        return createNoContentReponse();
    }
}
