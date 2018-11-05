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
import org.springframework.web.bind.annotation.RestController;

import com.scs.framework.core.web.controller.AbstractRestController;
import com.scs.usermanagement.domainmodel.SysFunction;
import com.scs.usermanagement.service.SysFunctionService;
import com.scs.usermanagement.vo.FunctionRoleVO;
import com.scs.usermanagement.vo.MenuFunctionVO;
import com.scs.usermanagement.vo.SearchFunctionRole;
import com.scs.usermanagement.vo.SysFunctionVO;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 资源管理
 * 
 *
 * @version Scs-DI v1.0
 * @author Sun Yunxu, 2018年9月27日
 */
@RestController
public class SysFunctionController extends AbstractRestController<SysFunction, Integer> {

    @Autowired
    private SysFunctionService sysFunctionService;

    @Override
    protected SysFunctionService getService() {
        return sysFunctionService;
    }

    @ApiOperation(value = "查询资源", notes = "查询所有资源信息")
    @GetMapping("/sysfunctions/all")
    public ResponseEntity<List<SysFunction>> getAllFunctions() {
        @SuppressWarnings("unchecked")
        ResponseEntity<List<SysFunction>> entity = (ResponseEntity<List<SysFunction>>) createOKResponse(
                getService().findAll());
        return entity;
    }

    @ApiOperation(value = "查询资源", notes = "根据资源类型查询所有资源信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "functionType", value = "资源类型", paramType = "path", required = true, dataType = "String") })
    @GetMapping("/sysfunctions/all/functiontype/{functionType}")
    public ResponseEntity<List<SysFunction>> getAllFunctionsByType(@PathVariable("functionType") String functionType) {
        @SuppressWarnings("unchecked")
        ResponseEntity<List<SysFunction>> entity = (ResponseEntity<List<SysFunction>>) createOKResponse(
                getService().findFunctionsByType(functionType));
        return entity;
    }

    @ApiOperation(value = "查询资源", notes = "根据资源类型查询所有可用资源信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "functionType", value = "资源类型", paramType = "path", required = true, dataType = "String") })
    @GetMapping("/sysfunctions/allenable/functiontype/{functionType}")
    public ResponseEntity<List<SysFunction>> getAllEnableFunctionsByType(@PathVariable("functionType") String functionType) {
        @SuppressWarnings("unchecked")
        ResponseEntity<List<SysFunction>> entity = (ResponseEntity<List<SysFunction>>) createOKResponse(
                getService().findEnableFunctionsByType(functionType));
        return entity;
    }

    @ApiOperation(value = "查询资源", notes = "根据资源类型和资源主键查询所有资源信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "functionType", value = "资源类型", paramType = "path", required = true, dataType = "String"),
        @ApiImplicitParam(name = "parentId", value = "资源父级ID", paramType = "path", required = true, dataType = "Integer") })
    @GetMapping("/sysfunctions/all/functiontype/{functionType}/parentid/{parentId}")
    public ResponseEntity<List<SysFunction>> getAllFunctionsByTypeAndId(@PathVariable("functionType") String functionType,
                                                                        @PathVariable("parentId") Integer parentId) {
        @SuppressWarnings("unchecked")
        ResponseEntity<List<SysFunction>> entity = (ResponseEntity<List<SysFunction>>) createOKResponse(
                getService().findFunctionsByTypeAndParentId(functionType, parentId));
        return entity;
    }

    @ApiOperation(value = "查询资源", notes = "根据资源类型和资源主键查询所有可用资源信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "functionType", value = "资源类型", paramType = "path", required = true, dataType = "String"),
        @ApiImplicitParam(name = "parentId", value = "资源父级ID", paramType = "path", required = true, dataType = "Integer") })
    @GetMapping("/sysfunctions/allenable/functiontype/{functionType}/parentid/{parentId}")
    public ResponseEntity<List<SysFunction>> getAllEnableFunctionsByTypeAndId(@PathVariable("functionType") String functionType,
                                                                              @PathVariable("parentId") Integer parentId) {
        @SuppressWarnings("unchecked")
        ResponseEntity<List<SysFunction>> entity = (ResponseEntity<List<SysFunction>>) createOKResponse(
                getService().findEnableFunctionsByTypeAndParentId(functionType, parentId));
        return entity;
    }

    @ApiOperation(value = "查询资源", notes = "根据当前登录用户角色身份查询菜单信息")
    @GetMapping("/sysfunctions/menu/origin")
    public ResponseEntity<List<MenuFunctionVO>> getMenuByOrigin() {
        @SuppressWarnings("unchecked")
        ResponseEntity<List<MenuFunctionVO>> entity = (ResponseEntity<List<MenuFunctionVO>>) createOKResponse(
                getService().findMenuByOrigin());
        return entity;
    }

    @ApiOperation(value = "查询资源", notes = "根据资源ID查询资源信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "资源主键", paramType = "path", required = true, dataType = "String") })
    @GetMapping("/sysfunction/id/{id}")
    public ResponseEntity<SysFunctionVO> getFunctionById(@PathVariable("id") Integer id) {
        @SuppressWarnings("unchecked")
        ResponseEntity<SysFunctionVO> entity = (ResponseEntity<SysFunctionVO>) createOKResponse(
                getService().findFunctionById(id));
        return entity;
    }

    @ApiOperation(value = "查询资源与角色关联", notes = "根据查询条件查询资源与角色关联信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "search", value = "资源与角色查询条件", required = true, dataType = "SearchFunctionRole") })
    @GetMapping("/sysfunctions/functionrole/search")
    public ResponseEntity<List<FunctionRoleVO>> getFunctionRoleBySearchFunctionRole(SearchFunctionRole search) {
        @SuppressWarnings("unchecked")
        ResponseEntity<List<FunctionRoleVO>> entity = (ResponseEntity<List<FunctionRoleVO>>) createOKResponse(
                getService().findFunctionRolesBySearchFunctionRole(search));
        return entity;
    }

    @ApiOperation(value = "新增资源", notes = "新增资源信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "sysFunction", value = "资源实体SysFunction", required = true, dataType = "SysFunction") })
    @PostMapping("/sysfunction")
    public ResponseEntity<SysFunction> addFunction(@RequestBody @Validated SysFunction sysFunction,
                                                   BindingResult result) {
        processValidations(result);
        @SuppressWarnings("unchecked")
        ResponseEntity<SysFunction> entity = (ResponseEntity<SysFunction>) createCreatedResponse(
                getService().add(sysFunction));
        return entity;
    }

    @ApiOperation(value = "修改资源", notes = "根据主键修改资源信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "sysFunction", value = "资源实体SysFunction", required = true, dataType = "SysFunction") })
    @PatchMapping("/sysfunction")
    public ResponseEntity<SysFunction> modifyFunction(@RequestBody @Validated SysFunction sysFunction,
                                                      BindingResult result) {
        processValidations(result);
        @SuppressWarnings("unchecked")
        ResponseEntity<SysFunction> entity = (ResponseEntity<SysFunction>) createCreatedResponse(
                getService().updatePartial(sysFunction));
        return entity;
    }

    @ApiOperation(value = "删除资源", notes = "根据主键删除资源信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "资源主键", required = true, dataType = "Integer") })
    @DeleteMapping("/sysfunction/id/{id}")
    public ResponseEntity<?> deleteFunctionById(@PathVariable("id") Integer id) {
        getService().delete(id);
        return createNoContentReponse();
    }
}
