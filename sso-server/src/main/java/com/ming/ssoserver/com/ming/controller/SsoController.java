package com.ming.ssoserver.com.ming.controller;

import com.ming.ssoserver.com.ming.service.SsoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;

/**
 * @author ming
 * @date 2022/2/6 16:40
 */

@Controller
@RequiredArgsConstructor
public class SsoController {

    private final SsoService ssoService;

    @GetMapping("/login")
    public String login(@RequestParam String redirectUri) throws UnsupportedEncodingException {
        return ssoService.login(redirectUri);
    }

    @PostMapping("/login")
    public String logIn(@RequestParam String username,
                        @RequestParam String password,
                        @RequestParam String redirectUri) throws UnsupportedEncodingException {
        return ssoService.logIn(username, password, redirectUri);
    }
}