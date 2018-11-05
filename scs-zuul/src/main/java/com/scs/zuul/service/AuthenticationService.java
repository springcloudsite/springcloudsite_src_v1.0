package com.scs.zuul.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.scs.framework.core.dao.RedisDao;
import com.scs.framework.core.web.auth.jwt.JWTUserDetails;
import com.scs.framework.core.web.auth.jwt.JWTUtils;
import com.scs.framework.utils.GeneralUtil;
import com.scs.framework.utils.MD5Util;
import com.scs.framework.utils.ScsUtil;
import com.scs.zuul.service.provider.SysUserService;
import com.scs.zuul.vo.LoginUserVO;

/**
 * 权限处理
 * 
 *
 * @version Scs-DI v1.0
 * @author Sun Yunxu, 2018年10月22日
 */
@Service
public class AuthenticationService {

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private RedisDao redisDao;

    @Autowired
    private SysUserService sysUserService;

    @Value("${jwt.access_token.expiration:900}")
    private long tokenExpiration;

    @Value("${jwt.refresh_token.expiration:7200}")
    private long refreshExpiration;

    /**
     * 检验当前token是否有用
     * 
     * @param token
     * @return
     */
    public boolean checkAuthorization(String token) {
        Integer userId = jwtUtils.getUserIdFromToken(token);
        if (userId != 0) {
            return true;
        }
        String redisKey = MD5Util.MD5Encode(token); // redis记录refreshtoken的key
        String refreshToken = ScsUtil.objectToString(redisDao.get(redisKey)); // 获取refreshtoken
        return (StringUtils.isNotBlank(refreshToken)
                && jwtUtils.getUserIdFromToken(refreshToken) != 0);
    }

    /**
     * 校验当前token并生成新的token
     * 
     * @param token
     * @return
     */
    public String getNewToken(String token) {
        Integer userId = jwtUtils.getUserIdFromToken(token);
        if (userId != 0) {
            return token;
        }
        String redisKey = MD5Util.MD5Encode(token); // redis记录refreshtoken的key
        String refreshToken = ScsUtil.objectToString(redisDao.get(redisKey)); // 获取refreshtoken
        if (StringUtils.isNotBlank(refreshToken)
                && jwtUtils.canTokenBeRefreshed(refreshToken, null)) {
            userId = jwtUtils.getUserIdFromToken(refreshToken);
            String curToken = ScsUtil
                    .objectToString(redisDao.get(RedisDao.PERFIX_KEY_TOKEN + userId));
            if (StringUtils.isNotBlank(curToken) && !token.equals(curToken)) {
                return curToken;
            }
        } else {
            return null;
        }

        LoginUserVO user = sysUserService.findLoginUserVOById(userId);
        refreshToken = createJWTTokenByUser(user);
        if (StringUtils.isNotBlank(refreshToken)) {
            redisDao.set(redisKey, refreshToken, 20l);
        }
        return refreshToken;
    }

    /**
     * 根据登录用户生成token
     * 
     * @param user
     * @return
     */
    public String getNewTokenByUser(LoginUserVO user) {
        if (user == null || user.getUserId() == null) {
            return "";
        }
        String token = ScsUtil
                .objectToString(redisDao.get(RedisDao.PERFIX_KEY_TOKEN + user.getUserId()));
        if (StringUtils.isBlank(token)) {
            token = this.createJWTTokenByUser(user);
        } else {
            int userId = jwtUtils.getUserIdFromToken(token);
            if (!user.getUserId().equals(userId)) {
                token = this.createJWTTokenByUser(user);
            }
        }
        return token;
    }

    /**
     * 根据登录用户生成token
     * 
     * @param user
     * @return
     */
    private String createJWTTokenByUser(LoginUserVO user) {
        if (user == null || user.getUserId() == null) {
            return "";
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ANYONES")); // 默认权限
        JWTUserDetails details = new JWTUserDetails(user.getUserId(), user.getLoginName(),
                GeneralUtil.getUUID32(), authorities, user.getRoleCodes());
        String token = jwtUtils.generateAccessToken(details);
        String redisKey = MD5Util.MD5Encode(token);
        redisDao.set(RedisDao.PERFIX_KEY_TOKEN + user.getUserId(), token, tokenExpiration);
        String refreshToken = jwtUtils.generateRefreshToken(details);
        redisDao.set(redisKey, refreshToken, refreshExpiration);
        return token;
    }
}
