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
import com.scs.usermanagement.domainmodel.SysRoleFunctionRelationship;
import com.scs.usermanagement.service.SysRoleFunctionRelationshipService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 资源与角色关联
 * 
 *
 * @version Scs-DI v1.0
 * @author Sun Yunxu, 2018年10月11日
 */
@RestController
public class SysRoleFunctionRelationshipController
        extends AbstractRestController<SysRoleFunctionRelationship, Integer> {

    @Autowired
    private SysRoleFunctionRelationshipService sysRoleFunctionRelationshipService;

    @Override
    protected SysRoleFunctionRelationshipService getService() {
        return sysRoleFunctionRelationshipService;
    }

    @ApiOperation(value = "新增资源与角色关系", notes = "新增资源与角色关系信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "sysRoleFunctionRelationship", value = "资源与角色关系实体SysRoleFunctionRelationship", required = true, dataType = "SysRoleFunctionRelationship") })
    @PostMapping("/sysrolefunction")
    public ResponseEntity<SysRoleFunctionRelationship> addRoleFunctionRelationship(@RequestBody @Validated SysRoleFunctionRelationship sysRoleFunctionRelationship,
                                                                                   BindingResult result) {
        processValidations(result);
        @SuppressWarnings("unchecked")
        ResponseEntity<SysRoleFunctionRelationship> entity = (ResponseEntity<SysRoleFunctionRelationship>) createCreatedResponse(
                getService().add(sysRoleFunctionRelationship));
        return entity;
    }

    @ApiOperation(value = "修改资源与角色关系", notes = "根据主键修改资源与角色关系信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "sysRoleFunctionRelationship", value = "资源与角色关系实体SysRoleFunctionRelationship", required = true, dataType = "SysRoleFunctionRelationship") })
    @PatchMapping("/sysrolefunction")
    public ResponseEntity<SysRoleFunctionRelationship> modifyRoleFunctionRelationship(@RequestBody @Validated SysRoleFunctionRelationship sysRoleFunctionRelationship,
                                                                                      BindingResult result) {
        processValidations(result);
        @SuppressWarnings("unchecked")
        ResponseEntity<SysRoleFunctionRelationship> entity = (ResponseEntity<SysRoleFunctionRelationship>) createCreatedResponse(
                getService().updatePartial(sysRoleFunctionRelationship));
        return entity;
    }

    @ApiOperation(value = "删除资源与角色关系", notes = "根据主键删除资源与角色关系信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "资源与角色关系主键", required = true, dataType = "Integer") })
    @DeleteMapping("/sysrolefunction/id/{id}")
    public ResponseEntity<?> deleteRoleFunctionRelationshipById(@PathVariable("id") Integer id) {
        getService().delete(id);
        return createNoContentReponse();
    }
}
