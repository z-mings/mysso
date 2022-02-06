package com.ming.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ming.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author ming
 * @date 2022/2/4 15:05
 */
@Slf4j
public class SsoFilter implements Filter {

    private AntPathMatcher antPathMatcher;
    private List<String> whiteList;
    private String ssoServerUrl;
    private static final String KEY = "SLDFJA*90AS^$$423&FAADJFL@8";


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //排除自定义的白名单
        if (isExcludeUrl(request.getServletPath())) {
            chain.doFilter(request, response);
        }
        //校验token
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                String name = cookie.getName();
                if ("token".equals(name)) {
                    //解析token
                    String token = cookie.getValue();
                    try {
                        String username = getUsername(token);
                        UserUtil.setUsername(username);
                    } catch (JWTVerificationException e) {
                        log.error("token校验失败", e);
                        break;
                    }
                    chain.doFilter(request, response);
                    return;
                }
            }
        }
        String queryString = request.getQueryString() == null ? "" : "?" + request.getQueryString();
        response.sendRedirect(ssoServerUrl + "/login?redirectUri=" + request.getRequestURI() + queryString);
    }

    @Override
    public void destroy() {
        UserUtil.removeUsername();
        Filter.super.destroy();
    }

    public String getUsername(String token) {
        DecodedJWT verify = JWT.require(Algorithm.HMAC256(KEY)).build().verify(token);
        return verify.getClaim("username").asString();
    }

    public void setAntPathMatcher(AntPathMatcher antPathMatcher) {
        this.antPathMatcher = antPathMatcher;
    }

    public void setWhiteList(List<String> whiteList) {
        this.whiteList = whiteList;
    }

    public void setSsoServerUrl(String ssoServerUrl) {
        this.ssoServerUrl = ssoServerUrl;
    }

    public boolean isExcludeUrl(String url) {
        for (String whiteUrl : whiteList) {
            if (antPathMatcher.match(whiteUrl, url)) {
                return true;
            }
        }
        return false;
    }
}
