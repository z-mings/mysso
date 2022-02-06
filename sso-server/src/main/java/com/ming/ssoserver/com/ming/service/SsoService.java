package com.ming.ssoserver.com.ming.service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.ming.ssoserver.com.ming.constant.Mark;
import com.ming.ssoserver.com.ming.constant.SsoConstant;
import com.ming.ssoserver.com.ming.util.CookieUtil;
import com.ming.ssoserver.com.ming.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ming
 * @date 2022/2/6 16:51
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SsoService {

    private final HttpServletRequest request;
    private final HttpServletResponse response;

    private static final Map<String, String> USER;

    static {
        USER = new HashMap<>(2);
        USER.put("admin", "123456");
    }

    public String login(String redirectUri) throws UnsupportedEncodingException {
        String token = CookieUtil.getCookie(request, "token");
        request.setAttribute(SsoConstant.REDIRECT_URI, redirectUri);
        if (!StringUtils.hasLength(token)) {
            //没有携带token跳转到登录页面
            return SsoConstant.LOGIN_URL;
        }
        try {
            //校验token
            JwtUtil.decoding(token);
        } catch (JWTVerificationException e) {
            log.error("token校验失败：" + token);
            return SsoConstant.LOGIN_URL;
        }
        return "redirect:" + authRedirectUri(redirectUri, token);
    }

    public String logIn(String username, String password, String redirectUri) throws UnsupportedEncodingException {
        request.setAttribute("redirectUri", redirectUri);
        String s = USER.get(username);
        if (s != null && s.equals(password)) {
            String token = JwtUtil.getToken(username);
            return "redirect:" + authRedirectUri(redirectUri, token);
        } else {
            log.error("用户名或密码错误---" + username + ":" + password);
            request.setAttribute("errorMessage", "用户名或密码错误");
            return "/login";
        }
    }

    private String authRedirectUri(String redirectUri, String token) throws UnsupportedEncodingException {
        StringBuilder sbf = new StringBuilder(redirectUri);
        if (redirectUri.contains(Mark.QUESTION)) {
            sbf.append(Mark.AND);
        } else {
            sbf.append(Mark.QUESTION);
        }
        sbf.append("token").append("=").append(token);
        return URLDecoder.decode(sbf.toString(), "utf-8");
    }


}
