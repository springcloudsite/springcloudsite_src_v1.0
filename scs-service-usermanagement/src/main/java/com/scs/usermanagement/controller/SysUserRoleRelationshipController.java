package com.scs.usermanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.scs.framework.core.web.controller.AbstractRestController;
import com.scs.usermanagement.domainmodel.SysUserRoleRelationship;
import com.scs.usermanagement.service.SysUserRoleRelationshipService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 用户与角色关联
 * 
 *
 * @version Scs-DI v1.0
 * @author Sun Yunxu, 2018年10月11日
 */
@RestController
public class SysUserRoleRelationshipController
        extends AbstractRestController<SysUserRoleRelationship, Integer> {

    @Autowired
    private SysUserRoleRelationshipService sysUserRoleRelationshipService;

    @Override
    protected SysUserRoleRelationshipService getService() {
        return sysUserRoleRelationshipService;
    }

    @ApiOperation(value = "新增用户与角色关系", notes = "新增用户与角色关系信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "sysUserRoleRelationship", value = "用户与角色关系实体SysUserRoleRelationship", required = true, dataType = "SysUserRoleRelationship") })
    @PostMapping("/sysuserrole")
    public ResponseEntity<SysUserRoleRelationship> addUserRoleRelationship(@RequestBody @Validated SysUserRoleRelationship sysUserRoleRelationship,
                                                                           BindingResult result) {
        processValidations(result);
        @SuppressWarnings("unchecked")
        ResponseEntity<SysUserRoleRelationship> entity = (ResponseEntity<SysUserRoleRelationship>) createCreatedResponse(
                getService().add(sysUserRoleRelationship));
        return entity;
    }

    @ApiOperation(value = "修改用户与角色关系", notes = "根据主键修改用户与角色关系信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "sysUserRoleRelationship", value = "用户与角色关系实体SysUserRoleRelationship", required = true, dataType = "SysUserRoleRelationship") })
    @PatchMapping("/sysuserrole")
    public ResponseEntity<SysUserRoleRelationship> modifyUserRoleRelationship(@RequestBody @Validated SysUserRoleRelationship sysUserRoleRelationship,
                                                                              BindingResult result) {
        processValidations(result);
        @SuppressWarnings("unchecked")
        ResponseEntity<SysUserRoleRelationship> entity = (ResponseEntity<SysUserRoleRelationship>) createCreatedResponse(
                getService().updatePartial(sysUserRoleRelationship));
        return entity;
    }

    @ApiOperation(value = "删除用户与角色关系", notes = "根据主键删除用户与角色关系信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "用户与角色关系主键", required = true, dataType = "Integer") })
    @DeleteMapping("/sysuserrole/id/{id}")
    public ResponseEntity<?> deleteUserRoleRelationshipById(@PathVariable("id") Integer id) {
        getService().delete(id);
        return createNoContentReponse();
    }
}
