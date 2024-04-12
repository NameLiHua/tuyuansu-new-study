package com.example.server1.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("service1") // nacos 服务 id
public interface TestFeign {
    @GetMapping("/server/{name}")
    public String getServer(@PathVariable(value = "name") String name);
}
