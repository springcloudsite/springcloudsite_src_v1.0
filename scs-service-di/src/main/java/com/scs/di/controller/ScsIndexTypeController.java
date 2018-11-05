package com.scs.di.controller;

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

import com.scs.di.domainmodel.ScsIndexType;
import com.scs.di.service.ScsIndexTypeService;
import com.scs.di.vo.ScsIndexTypeVO;
import com.scs.framework.core.web.controller.AbstractRestController;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 指标类型管理
 * 
 *
 * @version Scs-DI v1.0
 * @author Sun Yunxu, 2018年10月24日
 */
@RestController
public class ScsIndexTypeController extends AbstractRestController<ScsIndexType, Integer> {

    @Autowired
    private ScsIndexTypeService scsIndexTypeService;

    @Override
    protected ScsIndexTypeService getService() {
        return scsIndexTypeService;
    }

    @ApiOperation(value = "查询指标类型", notes = "查询所有指标类型信息")
    @GetMapping("/scsindextypes/all")
    public ResponseEntity<List<ScsIndexType>> getAllIndexTypes() {
        @SuppressWarnings("unchecked")
        ResponseEntity<List<ScsIndexType>> entity = (ResponseEntity<List<ScsIndexType>>) createOKResponse(
                getService().findAll());
        return entity;
    }

    @ApiOperation(value = "查询指标类型", notes = "根据对象类型id查询所有指标类型信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "typeid", value = "对象类型主键", paramType = "path", required = true, dataType = "Integer") })
    @GetMapping("/scsindextypes/all/typeid/{typeid}")
    public ResponseEntity<List<ScsIndexType>> getAllIndexTypes(@PathVariable("typeid") Integer typeId) {
        @SuppressWarnings("unchecked")
        ResponseEntity<List<ScsIndexType>> entity = (ResponseEntity<List<ScsIndexType>>) createOKResponse(
                getService().findAllByTypeid(typeId));
        return entity;
    }

    @ApiOperation(value = "查询指标类型", notes = "根据指标类型主键查询指标类型信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "指标类型主键", paramType = "path", required = true, dataType = "Integer") })
    @GetMapping("/scsindextype/id/{id}")
    public ResponseEntity<ScsIndexTypeVO> getIndexTypeById(@PathVariable("id") int id) {
        @SuppressWarnings("unchecked")
        ResponseEntity<ScsIndexTypeVO> entity = (ResponseEntity<ScsIndexTypeVO>) createOKResponse(
                getService().findIndexTypeVOById(id));
        return entity;
    }

    @ApiOperation(value = "新增指标类型", notes = "新增指标类型信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "scsIndexType", value = "指标类型实体ScsIndexType", required = true, dataType = "ScsIndexType") })
    @PostMapping("/scsindextype")
    public ResponseEntity<ScsIndexType> addIndexType(@RequestBody @Validated ScsIndexType scsIndexType,
                                                     BindingResult result) {
        processValidations(result);
        @SuppressWarnings("unchecked")
        ResponseEntity<ScsIndexType> entity = (ResponseEntity<ScsIndexType>) createCreatedResponse(
                getService().addIndexType(scsIndexType));
        return entity;
    }

    @ApiOperation(value = "修改指标类型", notes = "根据主键修改指标类型信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "scsIndexType", value = "指标类型实体ScsIndexType", required = true, dataType = "ScsIndexType") })
    @PatchMapping("/scsindextype")
    public ResponseEntity<ScsIndexType> modifyIndexType(@RequestBody @Validated ScsIndexType scsIndexType,
                                                        BindingResult result) {
        processValidations(result);
        @SuppressWarnings("unchecked")
        ResponseEntity<ScsIndexType> entity = (ResponseEntity<ScsIndexType>) createCreatedResponse(
                getService().updatePartial(scsIndexType));
        return entity;
    }

    @ApiOperation(value = "删除指标类型", notes = "根据主键删除指标类型信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "指标类型主键", required = true, dataType = "Integer") })
    @DeleteMapping("/scsindextype/id/{id}")
    public ResponseEntity<?> deleteIndexTypeById(@PathVariable("id") Integer id) {
        getService().deleteIndexTypeById(id);
        return createNoContentReponse();
    }
}
