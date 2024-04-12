package com.example.server1.controller;

import com.example.server1.feign.TestFeign;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RequestMapping("/feign")
@RefreshScope
@RestController
public class TestController {
    @Resource
    private TestFeign feignServer; // 加载 openfeign client
    @GetMapping("/getServer")
    public String consumer(@RequestParam String name) {
        return feignServer.getServer(name);
    }
}
