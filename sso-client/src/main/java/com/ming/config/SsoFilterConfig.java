package com.ming.config;

import com.ming.filter.SsoFilter;
import com.ming.property.SsoProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author ming
 * @date 2022/2/4 16:27
 */
@Slf4j
@Configuration
public class SsoFilterConfig {

    @Resource
    private SsoProperties ssoProperties;

    @Bean
    @Order(Integer.MIN_VALUE)
    @ConditionalOnProperty(name = "sso.enabled", havingValue = "true", matchIfMissing = true)
    public SsoFilter ssoFilter() {
        SsoFilter ssoFilter = new SsoFilter();
        ssoFilter.setAntPathMatcher(new AntPathMatcher());
        List<String> whiteList = Optional.ofNullable(ssoProperties.getWhiteList()).orElse(new ArrayList<>());
        ssoFilter.setWhiteList(whiteList);
        String ssoServerUrl = ssoProperties.getSsoServerUrl();
        Assert.notNull(ssoServerUrl, "注册地址不能为空");
        log.info("sso登录地址为：" + ssoServerUrl);
        ssoFilter.setSsoServerUrl(ssoServerUrl);
        return ssoFilter;
    }
}
