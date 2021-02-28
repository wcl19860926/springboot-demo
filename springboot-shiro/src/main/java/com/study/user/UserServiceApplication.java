package com.study.user;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.study.user.mapper.**.*")
@ComponentScan({"com.study.common","com.study.user" })
public class UserServiceApplication {


    public static void main(String args[]) {
        SpringApplication.run(UserServiceApplication.class, args);
    }


}
