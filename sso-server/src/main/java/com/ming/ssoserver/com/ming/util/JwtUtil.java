package com.ming.ssoserver.com.ming.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;

/**
 * @author ming
 * @date 2022/2/6 16:54
 */
public class JwtUtil {


    private static final String KEY = "SLDFJA*90AS^$$423&FAADJFL@8";

    private static final int TOKEN_EXPIRY = 30 * 60;

    public static String getToken(String username) {
        Calendar calendar = Calendar.getInstance();
        return JWT.create()
                .withClaim("username", username)
                .withClaim("time", calendar.getTime())
                .sign(Algorithm.HMAC256(KEY));
    }

    public static String getUsername(String token) {
        DecodedJWT verify = decoding(token);
        return verify.getClaim("username").asString();
    }

    public static DecodedJWT decoding(String token) {
        return JWT.require(Algorithm.HMAC256(KEY)).build().verify(token);
    }
}