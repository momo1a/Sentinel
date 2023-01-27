package com.alibaba.csp.sentinel.dashboard.rule.apollo;

import com.ctrip.framework.apollo.openapi.client.ApolloOpenApiClient;
import org.apache.commons.lang.StringUtils;

import java.util.concurrent.ConcurrentHashMap;

public final class ApolloConfigUtil {

    private static final String FLOW_RULE_TYPE = "flow";

    private static final String DEGRADE_RULE_TYPE = "degrade";

    private static ConcurrentHashMap<String,ApolloOpenApiClient> APOLLOOPENAPICLIENTMAP = new ConcurrentHashMap<>();

    private static final String FLOW_DATA_ID_POSTFIX = "-" + FLOW_RULE_TYPE + "-rules";

    private static final String DEGRADE_DATA_ID_POSTFIX = "-" + DEGRADE_RULE_TYPE + "-rules";

    public static String getFlowDataId(String appName){
        return String.format("%s%s",appName,FLOW_DATA_ID_POSTFIX);
    }

    public static String getDegradeDataId(String appName){
        return String.format("%s%s",appName,DEGRADE_DATA_ID_POSTFIX);
    }

    public static String getAppIdWithAppName(String appName) {
        return ApolloConfig.appIdMap.get(appName);
    }

    public static ApolloOpenApiClient  createApolloOpenApiClient(String appName) {
        ApolloOpenApiClient client = APOLLOOPENAPICLIENTMAP.get(appName);
        if (client != null) {
            return client;
        } else {
            String token = ApolloConfig.tokenMap.get(appName);
            if (StringUtils.isNotBlank(token)) {
                client = ApolloOpenApiClient.newBuilder()
                        .withPortalUrl(ApolloConfig.URL)
                        .withToken(token)
                        .build();
                APOLLOOPENAPICLIENTMAP.putIfAbsent(appName,client);
                return client;
            }else {
                System.err.println("当前 token 为 null！");
                return null;
            }
        }
    }


}
