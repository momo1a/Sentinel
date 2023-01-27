package com.alibaba.csp.sentinel.dashboard.rule.apollo;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.FlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRuleProvider;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.ctrip.framework.apollo.openapi.client.ApolloOpenApiClient;
import com.ctrip.framework.apollo.openapi.dto.OpenItemDTO;
import com.ctrip.framework.apollo.openapi.dto.OpenNamespaceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component("FlowRuleApolloProvider")
public class FlowRuleApolloProvider implements DynamicRuleProvider<List<FlowRuleEntity>> {

    @Autowired
    private Converter<String,List<FlowRuleEntity>> converter;

    @Override
    public List<FlowRuleEntity> getRules(String appName) throws Exception {
        ApolloOpenApiClient client = ApolloConfigUtil.createApolloOpenApiClient(appName);
        if (null != client){
            String flowDataId = ApolloConfigUtil.getFlowDataId(appName);
            String appId = ApolloConfigUtil.getAppIdWithAppName(appName);
            OpenNamespaceDTO dto = client.getNamespace(appId, ApolloConfig.ENV, ApolloConfig.CLUSTERNAME, ApolloConfig.NAMESPACE);
            String rules = dto.getItems()
                    .stream()
                    .filter(item -> item.getKey().equals(flowDataId))
                    .map(OpenItemDTO::getValue)
                    .findFirst()
                    .orElse("");
            return converter.convert(rules);
        }else {
            return Collections.emptyList();
        }
    }
}
