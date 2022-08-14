package com.tm.resource.server.api;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tangming
 * @date 2022/8/11
 */
@RestController
@RequestMapping(value = "/test")
public class TestController {
    @GetMapping(value = "/hello")
    public String hello(){
        return "hello world!";
    }

    @PreAuthorize("hasAuthority('sys:dept:world')")
    @GetMapping(value = "/world")
    public String world(){
        return "hello world!";
    }
}
