package com.ming.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ming
 * @date 2022/2/4 15:43
 */
@Setter
@Getter
@Component
@ConfigurationProperties("sso")
public class SsoProperties {

    private Boolean enabled = true;

    private String ssoServerUrl;

    private List<String> whiteList;
}
