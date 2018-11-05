package com.scs.di.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.scs.di.dao.ScsObjectDao;
import com.scs.di.domainmodel.ScsObject;
import com.scs.di.service.provider.SysUserService;
import com.scs.di.vo.LoginUserVO;
import com.scs.di.vo.SearchObject;
import com.scs.di.vo.TableObjectVO;
import com.scs.framework.core.exception.BusinessException;
import com.scs.framework.core.service.AbstractBaseService;
import com.scs.framework.core.web.context.RequestHeaderUtils;

/**
 * 分析对象管理
 * 
 *
 * @version Scs-DI v1.0
 * @author Sun Yunxu, 2018年10月23日
 */
@Service
public class ScsObjectService extends AbstractBaseService<ScsObject, Integer> {

    private static final String FIELD_BUSINESS_ID = "businessid";

    @Autowired
    private ScsObjectDao scsObjectDao;

    @Autowired
    private SysUserService sysUserService;

    @Override
    protected ScsObjectDao getDao() {
        return scsObjectDao;
    }

    /**
     * 根据查询条件查询对象信息（分页）
     * 
     * @param pageBounds
     * @param search
     * @return
     */
    public PageList<TableObjectVO> findPagerObjectsBySearchObject(PageBounds pageBounds,
                                                                  SearchObject search) {
        PageList<TableObjectVO> list = getDao().selectPageBySearchObject(pageBounds, search);
        return list;
    }

    /**
     * 根据查询条件查询对象信息
     * 
     * @param search
     * @return
     */
    public List<TableObjectVO> findObjectsBySearchObject(SearchObject search) {
        List<TableObjectVO> list = getDao().selectBySearchObject(search);
        return list;
    }

    /**
     * 根据分析对象类型查询对象信息
     * 
     * @param search
     * @return
     */
    public List<ScsObject> findObjectsByTypeId(int typeId) {
        ScsObject object = new ScsObject();
        object.setTypeid(typeId);
        List<ScsObject> list = getDao().selectAll(object);
        return list;
    }

    /**
     * 根据当前用户添加对象数据
     * 
     * @param object
     * @return
     */
    public ScsObject addObject(ScsObject object) {
        String userId = RequestHeaderUtils.getUserId();

        LoginUserVO user = sysUserService.findLoginUserVOById(Integer.valueOf(userId));
        if (user == null || user.getUserId() == null) {
            throw new BusinessException("用户不存在");
        }
        object.setFounder(user.getNickname());
        ScsObject result = super.add(object);
        return result;
    }

    @Override
    protected void addCheck(ScsObject object) {
        super.addCheck(object);
        List<FieldError> errors = new ArrayList<>();
        List<ScsObject> list = getDao().selectByBusinessId(object.getBusinessid());
        if (!list.isEmpty()) {
            errors.add(new FieldError(FIELD_BUSINESS_ID, FIELD_BUSINESS_ID, "业务主键已被使用"));
        }
        super.processValidations(errors);
    }

    @Override
    protected void updateCheck(ScsObject object) {
        super.updateCheck(object);
        List<FieldError> errors = new ArrayList<>();
        List<ScsObject> list = getDao().selectByBusinessId(object.getBusinessid());
        if (!list.isEmpty() && isDuplicateBusinessId(list, object.getId())) {
            errors.add(new FieldError(FIELD_BUSINESS_ID, FIELD_BUSINESS_ID, "业务主键已被使用"));
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
    private boolean isDuplicateBusinessId(List<ScsObject> list, Integer id) {
        return list.size() > 1 || !list.get(0).getId().equals(id);
    }
}
