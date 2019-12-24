package com.groupten.online_music.common.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.groupten.online_music.entity.User;
import com.groupten.online_music.entity.entityEnum.UserType;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtils {
    public static final String SECRET = "onlinemusic";

    /**
     * 生成token
     * @param user 自定义声明信息
     * @return 返回token
     */
    public static String createToken(User user) {
        //签发时间
        Date iaData = new Date();
        //过期时间-7天过期
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DAY_OF_WEEK, 7);
        Date expiresDate = now.getTime();
        //设置jwt header
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");
        //生成token
        String token = JWT.create()
                .withHeader(map)
                .withClaim("uid", user.getUid())
                .withClaim("name", user.getName())
                .withClaim("isAdmin", user.getType() == UserType.ADMIN)
                .withClaim("web", "groupten")
                .withExpiresAt(expiresDate)//设置过期时间
                .withIssuedAt(iaData)//设置签发时间
                .sign(Algorithm.HMAC256(SECRET));//加密

        return token;
    }
    /**
     * 刷新token
     * @return 返回token
     */
    public static String refreshToken(int uid, String name, boolean isAdmin, String web) {
        //签发时间
        Date niaData = new Date();
        //过期时间-7天过期
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DAY_OF_WEEK, 7);
        Date expiresDate = now.getTime();
        //设置jwt header
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");
        //生成token
        String token = JWT.create()
                .withHeader(map)
                .withClaim("uid", uid)
                .withClaim("name", name)
                .withClaim("isAdmin", isAdmin)
                .withClaim("web", web)
                .withExpiresAt(expiresDate)//设置过期时间
                .withIssuedAt(niaData)//设置签发时间
                .sign(Algorithm.HMAC256(SECRET));//加密

        return token;
    }
    public static Map<String, Claim> verifyToken(String token){
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
        DecodedJWT jwt = null;
        try{
            jwt = verifier.verify(token);
        } catch (Exception e){
            throw new RuntimeException("登录凭证已过期! 请重新登录");
        }

        return jwt.getClaims();
    }
}
