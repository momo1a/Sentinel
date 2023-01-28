package com.alibaba.csp.sentinel.dashboard.rule.apollo;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.FlowRuleEntity;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@ConfigurationProperties(prefix = "apollo.portal")
@ComponentScan("com.alibaba.csp.sentinel.dashboard.rule.apollo.*")
public class ApolloConfig implements InitializingBean {

    public static String URL = "http://localhost:8070";
    public static String USERID = "apollo";

    public static String ENV = "DEV";

    public static String CLUSTERNAME = "default";

    public static String NAMESPACE = "application";

    private String url;

    private List<String> appNameConfigList = new ArrayList<String>();

    public List<String> getAppNameConfigList() {
        return appNameConfigList;
    }

    public void setAppNameConfigList(List<String> appNameConfigList) {
        this.appNameConfigList = appNameConfigList;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    private String userId = USERID;

    private String env = ENV;

    private String clusterName = CLUSTERNAME;

    private String nameSpace = NAMESPACE;

    public static volatile ConcurrentHashMap<String /* appId */,String /* thirdId */> thirdIdMap = new ConcurrentHashMap();

    public static volatile ConcurrentHashMap<String /* applicationName */,String /* appId */> appIdMap = new ConcurrentHashMap();

    public static volatile ConcurrentHashMap<String /* applicationName */,String /* token */> tokenMap = new ConcurrentHashMap();

    @Bean
    public Converter<List<FlowRuleEntity>, String> flowRuleEntityEncoder() {
        return JSON::toJSONString;
    }

    @Bean
    public Converter<String, List<FlowRuleEntity>> flowRuleEntityDecoder() {
        return s -> JSON.parseArray(s, FlowRuleEntity.class);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ApolloConfig.ENV = env;
        ApolloConfig.USERID = userId;
        ApolloConfig.CLUSTERNAME = clusterName;
        ApolloConfig.NAMESPACE = nameSpace;
        ApolloConfig.URL = url;
        //System.err.println(appNameConfigList);
        appNameConfigList.forEach(item -> {
            String[] items = item.split(":");
            if (items.length == 4) {
                String applicationName = items[0];
                String token = items[1];
                String appId = items[2];
                String thirdId = items[3];
                thirdIdMap.putIfAbsent(appId, thirdId);
                appIdMap.putIfAbsent(applicationName, appId);
                tokenMap.putIfAbsent(applicationName, token);
            }
        });

    }
}
