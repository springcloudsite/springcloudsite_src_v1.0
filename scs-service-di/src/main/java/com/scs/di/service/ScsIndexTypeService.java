package com.scs.di.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scs.di.dao.ScsIndexTypeDao;
import com.scs.di.domainmodel.ScsIndexType;
import com.scs.di.domainmodel.ScsObjectType;
import com.scs.di.service.provider.SysUserService;
import com.scs.di.vo.LoginUserVO;
import com.scs.di.vo.ScsIndexTypeVO;
import com.scs.framework.core.exception.BusinessException;
import com.scs.framework.core.service.AbstractBaseService;
import com.scs.framework.core.web.context.RequestHeaderUtils;
import com.scs.framework.utils.ScsBeanUtils;

/**
 * 指标类型管理
 * 
 *
 * @version Scs-DI v1.0
 * @author Sun Yunxu, 2018年10月22日
 */
@Service
public class ScsIndexTypeService extends AbstractBaseService<ScsIndexType, Integer> {

    @Autowired
    private ScsIndexTypeDao scsIndexTypeDao;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private ScsObjectTypeService scsObjectTypeService;

    @Override
    protected ScsIndexTypeDao getDao() {
        return scsIndexTypeDao;
    }

    /**
     * 根据分析对象类型主键查询指标类型
     * 
     * @param typeid
     * @return
     */
    public List<ScsIndexType> findAllByTypeid(Integer typeid) {
        ScsIndexType search = new ScsIndexType();
        search.setTypeid(typeid);
        List<ScsIndexType> result = getDao().selectAll(search);
        return result;
    }

    /**
     * 根据当前用户添加指标类型数据
     * 
     * @param objectType
     * @return
     */
    public ScsIndexType addIndexType(ScsIndexType objectType) {
        String userId = RequestHeaderUtils.getUserId();

        LoginUserVO user = sysUserService.findLoginUserVOById(Integer.valueOf(userId));
        if (user == null || user.getUserId() == null) {
            throw new BusinessException("用户不存在");
        }
//        objectType.setFounder(user.getNickname());
        ScsIndexType result = super.add(objectType);
        return result;
    }

    /**
     * 根据主键删除指标类型
     * 
     * @param id
     */
    public void deleteIndexTypeById(Integer id) {
        super.delete(id);
    }

    /**
     * 根据ID查询指标类型
     * 
     * @param id
     * @return
     */
    public ScsIndexTypeVO findIndexTypeVOById(Integer id) {
        ScsIndexType index = getDao().selectByPrimaryKey(id);
        ScsIndexTypeVO result = new ScsIndexTypeVO();
        ScsBeanUtils.copyProperties(index, result);
        if (index.getParentId() != null) {
            ScsIndexType pIndex = getDao().selectByPrimaryKey(index.getParentId());
            result.setParentTypeName(pIndex.getName());
        }
        if (index.getTypeid() != null) {
            ScsObjectType objectType = scsObjectTypeService.findOne(index.getTypeid());
            result.setObjectType(objectType.getName());
        }
        return result;
    }
}
