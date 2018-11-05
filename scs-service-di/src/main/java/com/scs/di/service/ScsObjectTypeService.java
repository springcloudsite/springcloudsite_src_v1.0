package com.scs.di.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.scs.di.dao.ScsObjectTypeDao;
import com.scs.di.domainmodel.ScsObject;
import com.scs.di.domainmodel.ScsObjectType;
import com.scs.di.service.provider.SysUserService;
import com.scs.di.vo.LoginUserVO;
import com.scs.di.vo.SearchObjectType;
import com.scs.framework.core.exception.BusinessException;
import com.scs.framework.core.service.AbstractBaseService;
import com.scs.framework.core.web.context.RequestHeaderUtils;

/**
 * 分析对象类型管理
 * 
 *
 * @version Scs-DI v1.0
 * @author Sun Yunxu, 2018年10月22日
 */
@Service
public class ScsObjectTypeService extends AbstractBaseService<ScsObjectType, Integer> {

    @Autowired
    private ScsObjectTypeDao scsObjectTypeDao;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private ScsObjectService scsObjectService;

    @Override
    protected ScsObjectTypeDao getDao() {
        return scsObjectTypeDao;
    }

    /**
     * 根据查询条件查询对象类型信息（分页）
     * 
     * @param pageBounds
     * @param search
     * @return
     */
    public PageList<ScsObjectType> findPagerObjectTypesBySearchObjectType(PageBounds pageBounds,
                                                                          SearchObjectType search) {
        PageList<ScsObjectType> list = getDao().selectPageBySearchObjectType(pageBounds, search);
        return list;
    }

    /**
     * 根据查询条件查询对象类型信息
     * 
     * @param search
     * @return
     */
    public List<ScsObjectType> findObjectTypesBySearchObjectType(SearchObjectType search) {
        List<ScsObjectType> list = getDao().selectBySearchObjectType(search);
        return list;
    }

    /**
     * 根据当前用户添加对象类型数据
     * 
     * @param objectType
     * @return
     */
    public ScsObjectType addObjectType(ScsObjectType objectType) {
        String userId = RequestHeaderUtils.getUserId();

        LoginUserVO user = sysUserService.findLoginUserVOById(Integer.valueOf(userId));
        if (user == null || user.getUserId() == null) {
            throw new BusinessException("用户不存在");
        }
        objectType.setFounder(user.getNickname());
        ScsObjectType result = super.add(objectType);
        return result;
    }

    /**
     * 根据主键删除分析对象类型
     * 
     * @param id
     */
    public void deleteObjectTypeById(Integer id) {
        List<ScsObject> list = scsObjectService.findObjectsByTypeId(id);
        if (!list.isEmpty()) {
            throw new BusinessException("以被分析对象应用，需清除引用关系，方能删除。");
        }
        super.delete(id);
    }
}
