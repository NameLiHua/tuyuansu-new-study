package com.example.server1.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/config")
@RefreshScope
@RestController
public class TestController {
    @Value("${value:default}")
    private String value;
    @GetMapping("/value")
    public String testController1(){
        return value;
    }
}
