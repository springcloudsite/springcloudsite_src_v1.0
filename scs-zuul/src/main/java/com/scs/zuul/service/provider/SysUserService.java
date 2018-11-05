/*
 * Copyright (c) Pactera Corp.
 * All Rights Reserved.
 */
package com.scs.zuul.service.provider;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.scs.zuul.vo.LoginUserVO;

/**
 * 调用userManagement服务
 * 
 *
 * @version Scs-DI v1.0
 * @author Sun Yunxu, 2018年10月15日
 */
@FeignClient(name = "scs-service-usermanagement")
public interface SysUserService {

    /**
     * 根据用户主键获取登录对象
     * 
     * @param id
     * @return
     */
    @RequestMapping(value = "/sysuser/login/id/{id}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public LoginUserVO findLoginUserVOById(@PathVariable("id") int id);

}
