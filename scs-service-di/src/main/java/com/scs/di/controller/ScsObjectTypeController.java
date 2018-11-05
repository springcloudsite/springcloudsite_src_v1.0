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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.scs.di.domainmodel.ScsObjectType;
import com.scs.di.service.ScsObjectTypeService;
import com.scs.di.vo.SearchObjectType;
import com.scs.framework.core.web.controller.AbstractRestController;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 分析对象类型管理
 * 
 *
 * @version Scs-DI v1.0
 * @author Sun Yunxu, 2018年10月22日
 */
@RestController
public class ScsObjectTypeController extends AbstractRestController<ScsObjectType, Integer> {

    @Autowired
    private ScsObjectTypeService scsObjectTypeService;

    @Override
    protected ScsObjectTypeService getService() {
        return scsObjectTypeService;
    }

    @ApiOperation(value = "查询对象类型", notes = "查询所有对象类型信息")
    @GetMapping("/scsobjecttypes/all")
    public ResponseEntity<List<ScsObjectType>> getAllObjectTypes() {
        @SuppressWarnings("unchecked")
        ResponseEntity<List<ScsObjectType>> entity = (ResponseEntity<List<ScsObjectType>>) createOKResponse(
                getService().findAll());
        return entity;
    }

    @ApiOperation(value = "查询对象类型", notes = "根据查询条件查询所有对象类型信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "当前页", paramType = "query", required = true, dataType = "Integer"),
        @ApiImplicitParam(name = "limit", value = "每页显示条数", paramType = "query", required = true, dataType = "Integer"),
        @ApiImplicitParam(name = "sort", value = "排序条件", paramType = "query", required = false, dataType = "String"),
        @ApiImplicitParam(name = "search", value = "对象类型查询条件", paramType = "query", required = true, dataType = "SearchObjectType") })
    @GetMapping("/scsobjecttypes/all/search")
    public ResponseEntity<List<ScsObjectType>> getAllObjectTypesBySearchObjectType(SearchObjectType search) {
        @SuppressWarnings("unchecked")
        ResponseEntity<List<ScsObjectType>> entity = (ResponseEntity<List<ScsObjectType>>) createOKResponse(
                getService().findObjectTypesBySearchObjectType(search));
        return entity;
    }

    @ApiOperation(value = "查询对象类型", notes = "根据查询条件查询所有对象类型信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "当前页", paramType = "query", required = true, dataType = "Integer"),
        @ApiImplicitParam(name = "limit", value = "每页显示条数", paramType = "query", required = true, dataType = "Integer"),
        @ApiImplicitParam(name = "sort", value = "排序条件", paramType = "query", required = false, dataType = "String"),
        @ApiImplicitParam(name = "search", value = "对象类型查询条件", paramType = "query", required = true, dataType = "SearchObjectType") })
    @GetMapping("/scsobjecttypes/all/search/pager")
    public ResponseEntity<PageList<ScsObjectType>> getAllObjectTypesBySearchObjectType(@RequestParam("page") int page,
                                                                                       @RequestParam("limit") int limit,
                                                                                       @RequestParam(required = false) String sort,
                                                                                       SearchObjectType search) {
        PageBounds pageBounds = new PageBounds(page, limit, Order.formString(sort));
        @SuppressWarnings("unchecked")
        ResponseEntity<PageList<ScsObjectType>> entity = (ResponseEntity<PageList<ScsObjectType>>) createOKResponse(
                getService().findPagerObjectTypesBySearchObjectType(pageBounds, search));
        return entity;
    }

    @ApiOperation(value = "查询对象类型", notes = "根据对象类型主键查询对象类型信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "对象类型主键", paramType = "path", required = true, dataType = "Integer") })
    @GetMapping("/scsobjecttype/id/{id}")
    public ResponseEntity<ScsObjectType> getObjectTypeById(@PathVariable("id") int id) {
        @SuppressWarnings("unchecked")
        ResponseEntity<ScsObjectType> entity = (ResponseEntity<ScsObjectType>) createOKResponse(
                getService().findOne(id));
        return entity;
    }

    @ApiOperation(value = "新增对象类型", notes = "新增对象类型信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "scsObjectType", value = "对象类型实体ScsObjectType", required = true, dataType = "ScsObjectType") })
    @PostMapping("/scsobjecttype")
    public ResponseEntity<ScsObjectType> addObjectType(@RequestBody @Validated ScsObjectType scsObjectType,
                                                       BindingResult result) {
        processValidations(result);
        @SuppressWarnings("unchecked")
        ResponseEntity<ScsObjectType> entity = (ResponseEntity<ScsObjectType>) createCreatedResponse(
                getService().addObjectType(scsObjectType));
        return entity;
    }

    @ApiOperation(value = "修改对象类型", notes = "根据主键修改对象类型信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "scsObjectType", value = "对象类型实体ScsObjectType", required = true, dataType = "ScsObjectType") })
    @PatchMapping("/scsobjecttype")
    public ResponseEntity<ScsObjectType> modifyObjectType(@RequestBody @Validated ScsObjectType scsObjectType,
                                                          BindingResult result) {
        processValidations(result);
        @SuppressWarnings("unchecked")
        ResponseEntity<ScsObjectType> entity = (ResponseEntity<ScsObjectType>) createCreatedResponse(
                getService().updatePartial(scsObjectType));
        return entity;
    }

    @ApiOperation(value = "删除对象类型", notes = "根据主键删除对象类型信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "对象类型主键", required = true, dataType = "Integer") })
    @DeleteMapping("/scsobjecttype/id/{id}")
    public ResponseEntity<?> deleteObjectTypeById(@PathVariable("id") Integer id) {
        getService().deleteObjectTypeById(id);
        return createNoContentReponse();
    }
}
