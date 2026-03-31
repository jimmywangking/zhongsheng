package com.git;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Spring Boot 启动类
 */
@EnableScheduling
@SpringBootApplication
@RestController
@MapperScan("com.git.mapper")
public class ScheduleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScheduleApplication.class, args);
    }

    /**
     * 健康检查接口
     */
    @GetMapping({"/index", "/health"})
    public String index() {
        return "CRM Schedule Service is running!";
    }
}
