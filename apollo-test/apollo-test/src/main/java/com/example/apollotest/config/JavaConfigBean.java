package com.example.apollotest.config;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class JavaConfigBean {

    @Value("${timeout:20}")
    private Integer timeout;

    @Value("${newKey:'hello'}")
    private String newKey;
}
