package com.example.apollotest.listener;

import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.apollo.ApolloDataSource;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;

public class ApolloDataSourceListener implements InitializingBean {

    private static final String FLOW_RULE_TYPE = "flow";

    private static final String DEGRADE_RULE_TYPE = "degrade";

    private static final String FLOW_DATA_ID_POSTFIX = "-" + FLOW_RULE_TYPE + "-rules";

    private static final String DEGRADE_DATA_ID_POSTFIX = "-" + DEGRADE_RULE_TYPE + "-rules";

    private final String applicationName;
    public ApolloDataSourceListener(String applicationName){
        this.applicationName = applicationName;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initFlowRules();
    }

    private void initFlowRules() {
        String flowRuleKey = applicationName + FLOW_DATA_ID_POSTFIX;
        ReadableDataSource<String, List<FlowRule>>  flowRuleDataSource = new ApolloDataSource<>(
                "application", flowRuleKey, "[]",
                source -> JSON.parseObject(source,new TypeReference<List<FlowRule>>(){}));
        FlowRuleManager.register2Property(flowRuleDataSource.getProperty());
    }
}
