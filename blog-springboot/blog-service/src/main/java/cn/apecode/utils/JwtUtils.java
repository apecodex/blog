package cn.apecode.utils;

import cn.apecode.dto.UserDetailsDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: JWT工具类
 * @author: apecode
 * @date: 2022-05-28 18:40
 **/
@Component
public class JwtUtils {

    public static final String CLAIM_KEY_USERNAME = "sub";
    public static final String CLAIM_KEY_CREATED = "created";
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * @description: 根据用户信息生成token
     * @param userDetails
     * @return {@link String}
     * @auther apecode
     * @date 2022/5/28 18:40
    */
    public String generateToken(UserDetailsDto userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    /**
     * @description: 从JWT TOKEN中获取登录用户名
     * @param token
     * @return {@link String}
     * @auther apecode
     * @date 2022/5/28 18:41
    */
    public String getUserNameFromToken(String token) {
        String username;
        try {
            Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    /**
     * @description: 验证JWT TOKEN是否有效
     * @param token
     * @param userDetails
     * @return {@link boolean}
     * @auther apecode
     * @date 2022/5/28 18:41
    */
    public boolean validateToken(String token, UserDetailsDto userDetails) {
        String username = getUserNameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * @description: 判断JWT TOKEN是否可以被刷新
     * @param token
     * @return {@link boolean}
     * @auther apecode
     * @date 2022/5/28 18:41
    */
    public boolean canRefresh(String token) {
        return !isTokenExpired(token);
    }

    /**
     * @description: 刷新JWT TOKEN
     * @param token
     * @return {@link String}
     * @auther apecode
     * @date 2022/5/28 18:41
    */
    public String refreshToken(String token) {
        Claims claims = getClaimsFromToken(token);
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    /**
     * @description: 判断JWT TOKEN是否失效
     * @param token
     * @return {@link boolean}
     * @auther apecode
     * @date 2022/5/28 18:41
    */
    private boolean isTokenExpired(String token) {
        Date expireDate = getExpiredDateFromToken(token);
        return expireDate.before(new Date());
    }

    /**
     * @description: 从JWT TOKEN中获取过期时间
     * @param token
     * @return {@link Date}
     * @auther apecode
     * @date 2022/5/28 18:42
    */
    private Date getExpiredDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }

    /**
     * @description: 从JWT TOKEN获取荷载
     * @param token
     * @return {@link Claims}
     * @auther apecode
     * @date 2022/5/28 18:42
    */
    private Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            claims = e.getClaims();
        }
        return claims;
    }

    /**
     * @description: 根据荷载生成JWT TOKEN
     * @param claims
     * @return {@link String}
     * @auther apecode
     * @date 2022/5/28 18:42
    */
    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * @description: 生成JWT TOKEN失效时间
     * @param
     * @return {@link Date}
     * @auther apecode
     * @date 2022/5/28 18:42
    */
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

}
