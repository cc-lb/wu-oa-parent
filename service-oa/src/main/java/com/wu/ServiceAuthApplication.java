package com.wu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @Classname ServiceApplication
 * @Description
 * @Date 2023/4/10 20:05
 * @Created by cc
 */
@SpringBootApplication
//@MapperScan("com.wu.auth.mapper")
//@ComponentScan (  {"com.wu.common","com.wu"})

public class ServiceAuthApplication {
    public static void main(String[] args){
        SpringApplication.run(ServiceAuthApplication.class,args);
    }
}
