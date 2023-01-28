package com.example.apollotest.config;


import com.alibaba.csp.sentinel.annotation.aspectj.SentinelResourceAspect;
import com.example.apollotest.listener.ApolloDataSourceListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Sentinel4ApolloConfig {

    @Value("${spring.application.name}")
    private String applicationName = "";

    @Bean
    @ConditionalOnMissingBean
    public SentinelResourceAspect sentinelResourceAspect(){
        return new SentinelResourceAspect();
    }

    @Bean
    @ConditionalOnMissingBean
    public ApolloDataSourceListener apolloDataSourceListener(){
        return new ApolloDataSourceListener(applicationName);
    }

}
