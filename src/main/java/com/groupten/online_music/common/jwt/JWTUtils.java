package com.groupten.online_music.common.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.groupten.online_music.entity.User;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtils {
    public static String SECRET = "onlinemusic";

    /**
     * 生成token
     * @param user 自定义声明信息
     * @return 返回token
     */
    public static String createToken(User user) {
        //签发时间
        Date iaData = new Date();
        //过期时间-60分钟过期
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, 60);
        Date expiresDate = now.getTime();
        //设置jwt header
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");
        //生成token
        String token = JWT.create()
                .withHeader(map)
                .withClaim("uid", user.getUid())
                .withClaim("web", "groupten")
                .withExpiresAt(expiresDate)//设置过期时间
                .withIssuedAt(iaData)//设置签发时间
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
