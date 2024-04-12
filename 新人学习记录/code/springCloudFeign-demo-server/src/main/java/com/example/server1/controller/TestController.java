package com.example.server1.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/server")
@RefreshScope
@RestController
public class TestController {
    @GetMapping("/{name}")
    public String testController1(@PathVariable(value = "name") String name){
        return name+"--成功获取服务";
    }
}
