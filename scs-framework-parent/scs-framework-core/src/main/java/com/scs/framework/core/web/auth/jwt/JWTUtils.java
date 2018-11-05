/*
 * Copyright (c) Pactera Corp.
 * All Rights Reserved.
 */
package com.scs.framework.core.web.auth.jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.scs.framework.utils.JacksonHelper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * 
 *
 * @version ScsDI v1.0
 * @author Zhang Ran, 2018年5月8日
 */
@Component
public class JWTUtils {
    public static final String ROLE_REFRESH_TOKEN = "ROLE_REFRESH_TOKEN";

    private static final String CLAIM_KEY_USER_ID = "user_id";
    private static final String CLAIM_KEY_AUTHORITIES = "scope"; // 权限码
    private static final String CLAIM_KEY_ROLES = "role"; // 角色
    private static final String CLAIM_KEY_ACCOUNT_ENABLED = "enabled";
    private static final String CLAIM_KEY_ACCOUNT_NON_LOCKED = "non_locked";
    private static final String CLAIM_KEY_ACCOUNT_NON_EXPIRED = "non_expired";

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access_token.expiration}")
    private Long accessTokenExpiration;

    @Value("${jwt.refresh_token.expiration}")
    private Long refreshTokenExpiration;

    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

    private static Logger logger = LoggerFactory.getLogger(JWTUtils.class);

    public JWTUserDetails getUserFromToken(String token) {
        JWTUserDetails user = null;
        try {
            final Claims claims = getClaimsFromToken(token);
            if (claims != null) {

                int userId = getUserIdFromToken(token);
                String username = claims.getSubject();

                @SuppressWarnings("unchecked")
                List<String> roles = (List<String>) claims.get(CLAIM_KEY_ROLES);

//            List<String> scope = (List<String>) claims.get(CLAIM_KEY_AUTHORITIES);
                // TODO 获取是为字符串 ，需做转换，暂时屏蔽
                List<String> scope = new ArrayList<>();
                Collection<? extends GrantedAuthority> authorities = parseArrayToAuthorities(scope);

                boolean accountEnabled = (Boolean) claims.get(CLAIM_KEY_ACCOUNT_ENABLED);
                boolean accountNonLocked = (Boolean) claims.get(CLAIM_KEY_ACCOUNT_NON_LOCKED);
                boolean accountNonExpired = (Boolean) claims.get(CLAIM_KEY_ACCOUNT_NON_EXPIRED);

                user = new JWTUserDetails(userId, username, "password", accountEnabled,
                        accountNonExpired, true, accountNonLocked, authorities, roles);
            }
        } catch (Exception e) {
            logger.error("通过token获取用户对象异常", e);
        }
        return user;
    }

    public int getUserIdFromToken(String token) {
        int userId = 0;
        try {
            final Claims claims = getClaimsFromToken(token);
            if (claims != null) {
                userId = (Integer) claims.get(CLAIM_KEY_USER_ID);
            }
        } catch (Exception e) {
            logger.warn("通过token获取userId异常", e);
        }
        return userId;
    }

    public String getUsernameFromToken(String token) {
        String username = null;
        try {
            final Claims claims = getClaimsFromToken(token);
            if (claims != null) {
                username = claims.getSubject();
            }
        } catch (Exception e) {
            logger.warn("通过token获取Username异常", e);
        }
        return username;
    }

    public String getUserRoleFromToken(String token) {
        String userRole = null;
        try {
            final Claims claims = getClaimsFromToken(token);
            if (claims != null) {
                userRole = JacksonHelper.entityToJson(claims.get(CLAIM_KEY_ROLES));
            }
        } catch (Exception e) {
            logger.warn("通过token获取UserRole异常", e);
        }
        return userRole;
    }

    public Date getCreatedDateFromToken(String token) {
        Date created = null;
        try {
            final Claims claims = getClaimsFromToken(token);
            if (claims != null) {
                created = claims.getIssuedAt();
            }
        } catch (Exception e) {
            logger.warn("通过token获取CreatedDate异常", e);
        }
        return created;
    }

    public Date getExpirationDateFromToken(String token) {
        Date expiration = null;
        try {
            final Claims claims = getClaimsFromToken(token);
            if (claims != null) {
                expiration = claims.getExpiration();
            }
        } catch (Exception e) {
            logger.warn("通过token获取ExpirationDate异常", e);
        }
        return expiration;
    }

    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    /**
     * 生成失效时间
     * 
     * @param expiration
     * @return
     */
    private Date generateExpirationDate(long expiration) {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    /**
     * 判断token是否过期
     * 
     * @param token
     * @return
     */
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return (expiration == null || expiration.before(new Date()));
    }

    /**
     * 判断token生成时间是否在刷新时间之前
     * 
     * @param created
     * @param lastPasswordReset
     * @return
     */
    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }

    /**
     * 根据用户对象生成token
     * 
     * @param userDetails
     * @return
     */
    public String generateAccessToken(UserDetails userDetails) {
        JWTUserDetails user = (JWTUserDetails) userDetails;
        Map<String, Object> claims = generateClaims(user);
        claims.put(CLAIM_KEY_AUTHORITIES,
                JacksonHelper.entityToJson(authoritiesToArray(user.getAuthorities())));
        claims.put(CLAIM_KEY_ROLES, user.getRoles());
        return generateAccessToken(user.getUsername(), claims);
    }

    /**
     * 根据用户对象封装消息体
     * 
     * @param user
     * @return
     */
    private Map<String, Object> generateClaims(JWTUserDetails user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USER_ID, user.getUserId());
        claims.put(CLAIM_KEY_ACCOUNT_ENABLED, user.isEnabled());
        claims.put(CLAIM_KEY_ACCOUNT_NON_LOCKED, user.isAccountNonLocked());
        claims.put(CLAIM_KEY_ACCOUNT_NON_EXPIRED, user.isAccountNonExpired());
        return claims;
    }

    /**
     * 生成access_token
     * 
     * @param subject
     * @param claims
     * @return
     */
    private String generateAccessToken(String subject, Map<String, Object> claims) {
        return generateToken(subject, claims, accessTokenExpiration);
    }

    private List<String> authoritiesToArray(Collection<? extends GrantedAuthority> authorities) {
        List<String> list = new ArrayList<>();
        if (authorities != null) {
            authorities.forEach(a -> list.add(a.getAuthority()));
        }
        return list;
    }

    private Collection<? extends GrantedAuthority> parseArrayToAuthorities(List<String> roles) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        SimpleGrantedAuthority authority;

        for (String role : roles) {
            authority = new SimpleGrantedAuthority(role);
            authorities.add(authority);
        }
        return authorities;
    }

    /**
     * 根据用户创建refreshToken
     * 
     * @param userDetails
     * @return
     */
    public String generateRefreshToken(UserDetails userDetails) {
        JWTUserDetails user = (JWTUserDetails) userDetails;
        Map<String, Object> claims = generateClaims(user);
        // 只授于更新 token 的权限
        String[] roles = new String[] { JWTUtils.ROLE_REFRESH_TOKEN };
        claims.put(CLAIM_KEY_AUTHORITIES, JacksonHelper.entityToJson(roles));
        return generateRefreshToken(user.getUsername(), claims);
    }

    /**
     * 创建refresh_token
     * 
     * @param subject
     * @param claims
     * @return
     */
    private String generateRefreshToken(String subject, Map<String, Object> claims) {
        return generateToken(subject, claims, refreshTokenExpiration);
    }

    /**
     * 判断token是否可被刷新
     * 
     * @param token
     * @param lastPasswordReset
     * @return
     */
    public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
        final Date created = getCreatedDateFromToken(token);
        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
                && (!isTokenExpired(token));
    }

    /**
     * 刷新access_token
     * 
     * @param token
     * @return
     */
    public String refreshToken(String token) {
        String refreshedToken = null;
        try {
            final Claims claims = getClaimsFromToken(token);
            if (claims != null) {
                refreshedToken = generateAccessToken(claims.getSubject(), claims);
            }
        } catch (Exception e) {
            logger.warn("刷新token异常", e);
        }
        return refreshedToken;
    }

    /**
     * 初始化token
     * 
     * @param subject
     * @param claims
     * @param expiration
     * @return
     */
    private String generateToken(String subject, Map<String, Object> claims, long expiration) {
        return Jwts.builder().setClaims(claims).setSubject(subject)
                .setId(UUID.randomUUID().toString()).setIssuedAt(new Date())
                .setExpiration(generateExpirationDate(expiration))
                .compressWith(CompressionCodecs.DEFLATE).signWith(SIGNATURE_ALGORITHM, secret)
                .compact();
    }

    /**
     * 根据用户信息校验token是否有效
     * 
     * @param token
     * @param userDetails
     * @return
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        JWTUserDetails user = (JWTUserDetails) userDetails;
        final long userId = getUserIdFromToken(token);
        final String username = getUsernameFromToken(token);
        // final Date created = getCreatedDateFromToken(token);
        // final Date expiration = getExpirationDateFromToken(token);
        return (userId == user.getUserId() && username.equals(user.getUsername())
                && !isTokenExpired(token)
        /* && !isCreatedBeforeLastPasswordReset(created, userDetails.getLastPasswordResetDate()) */
        );
    }

}
