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
import com.scs.di.domainmodel.ScsObject;
import com.scs.di.service.ScsObjectService;
import com.scs.di.vo.SearchObject;
import com.scs.di.vo.TableObjectVO;
import com.scs.framework.core.web.controller.AbstractRestController;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 分析对象管理
 * 
 *
 * @version Scs-DI v1.0
 * @author Sun Yunxu, 2018年10月23日
 */
@RestController
public class ScsObjectController extends AbstractRestController<ScsObject, Integer> {

    @Autowired
    private ScsObjectService scsObjectService;

    @Override
    protected ScsObjectService getService() {
        return scsObjectService;
    }

    @ApiOperation(value = "查询对象", notes = "查询所有对象信息")
    @GetMapping("/scsobjects/all")
    public ResponseEntity<List<ScsObject>> getAllObjects() {
        @SuppressWarnings("unchecked")
        ResponseEntity<List<ScsObject>> entity = (ResponseEntity<List<ScsObject>>) createOKResponse(
                getService().findAll());
        return entity;
    }

    @ApiOperation(value = "查询对象", notes = "根据查询条件查询所有对象信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "当前页", paramType = "query", required = true, dataType = "Integer"),
        @ApiImplicitParam(name = "limit", value = "每页显示条数", paramType = "query", required = true, dataType = "Integer"),
        @ApiImplicitParam(name = "sort", value = "排序条件", paramType = "query", required = false, dataType = "String"),
        @ApiImplicitParam(name = "search", value = "对象查询条件", paramType = "query", required = true, dataType = "SearchObject") })
    @GetMapping("/scsobjects/all/search")
    public ResponseEntity<List<TableObjectVO>> getAllObjectsBySearchObject(SearchObject search) {
        @SuppressWarnings("unchecked")
        ResponseEntity<List<TableObjectVO>> entity = (ResponseEntity<List<TableObjectVO>>) createOKResponse(
                getService().findObjectsBySearchObject(search));
        return entity;
    }

    @ApiOperation(value = "查询对象", notes = "根据查询条件查询所有对象信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "当前页", paramType = "query", required = true, dataType = "Integer"),
        @ApiImplicitParam(name = "limit", value = "每页显示条数", paramType = "query", required = true, dataType = "Integer"),
        @ApiImplicitParam(name = "sort", value = "排序条件", paramType = "query", required = false, dataType = "String"),
        @ApiImplicitParam(name = "search", value = "对象查询条件", paramType = "query", required = true, dataType = "SearchObject") })
    @GetMapping("/scsobjects/all/search/pager")
    public ResponseEntity<PageList<TableObjectVO>> getAllObjectsBySearchObject(@RequestParam("page") int page,
                                                                               @RequestParam("limit") int limit,
                                                                               @RequestParam(required = false) String sort,
                                                                               SearchObject search) {
        PageBounds pageBounds = new PageBounds(page, limit, Order.formString(sort));
        @SuppressWarnings("unchecked")
        ResponseEntity<PageList<TableObjectVO>> entity = (ResponseEntity<PageList<TableObjectVO>>) createOKResponse(
                getService().findPagerObjectsBySearchObject(pageBounds, search));
        return entity;
    }

    @ApiOperation(value = "查询对象", notes = "根据对象主键查询对象信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "对象主键", paramType = "path", required = true, dataType = "Integer") })
    @GetMapping("/scsobject/id/{id}")
    public ResponseEntity<ScsObject> getObjectById(@PathVariable("id") int id) {
        @SuppressWarnings("unchecked")
        ResponseEntity<ScsObject> entity = (ResponseEntity<ScsObject>) createOKResponse(
                getService().findOne(id));
        return entity;
    }

    @ApiOperation(value = "新增对象", notes = "新增对象信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "scsObject", value = "对象实体ScsObject", required = true, dataType = "ScsObject") })
    @PostMapping("/scsobject")
    public ResponseEntity<ScsObject> addObject(@RequestBody @Validated ScsObject scsObject,
                                               BindingResult result) {
        processValidations(result);
        @SuppressWarnings("unchecked")
        ResponseEntity<ScsObject> entity = (ResponseEntity<ScsObject>) createCreatedResponse(
                getService().addObject(scsObject));
        return entity;
    }

    @ApiOperation(value = "修改对象", notes = "根据主键修改对象信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "scsObject", value = "对象实体ScsObject", required = true, dataType = "ScsObject") })
    @PatchMapping("/scsobject")
    public ResponseEntity<ScsObject> modifyObject(@RequestBody @Validated ScsObject scsObject,
                                                  BindingResult result) {
        processValidations(result);
        @SuppressWarnings("unchecked")
        ResponseEntity<ScsObject> entity = (ResponseEntity<ScsObject>) createCreatedResponse(
                getService().updatePartial(scsObject));
        return entity;
    }

    @ApiOperation(value = "删除对象", notes = "根据主键删除对象信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "对象主键", required = true, dataType = "Integer") })
    @DeleteMapping("/scsobject/id/{id}")
    public ResponseEntity<?> deleteObjectById(@PathVariable("id") Integer id) {
        getService().delete(id);
        return createNoContentReponse();
    }
}
