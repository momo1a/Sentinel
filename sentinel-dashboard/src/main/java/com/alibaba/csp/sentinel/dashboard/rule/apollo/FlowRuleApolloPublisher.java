package com.alibaba.csp.sentinel.dashboard.rule.apollo;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.FlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRulePublisher;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.ctrip.framework.apollo.openapi.client.ApolloOpenApiClient;
import com.ctrip.framework.apollo.openapi.dto.NamespaceReleaseDTO;
import com.ctrip.framework.apollo.openapi.dto.OpenItemDTO;
import org.apache.commons.lang.time.FastDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;


@Component("FlowRuleApolloPublisher")

public class FlowRuleApolloPublisher implements DynamicRulePublisher<List<FlowRuleEntity>> {

    @Autowired
    private Converter<List<FlowRuleEntity>,String> converter;

    private FastDateFormat FDF = FastDateFormat.getInstance("yyyy-mm-dd HH:mm:ss");

    @Override
    public void publish(String appName, List<FlowRuleEntity> rules) throws Exception {
        if (rules == null)
            return;
        ApolloOpenApiClient client = ApolloConfigUtil.createApolloOpenApiClient(appName);
        if (client != null){
            String dateFormat = FDF.format(new Date());
            String flowDataId = ApolloConfigUtil.getFlowDataId(appName);
            String appId = ApolloConfigUtil.getAppIdWithAppName(appName);

            OpenItemDTO dto = new OpenItemDTO();
            dto.setKey(flowDataId);
            dto.setValue(converter.convert(rules));
            dto.setComment("modify: " + dateFormat);
            dto.setDataChangeLastModifiedBy(ApolloConfig.USERID);
            dto.setDataChangeCreatedBy(ApolloConfig.USERID);
            // 修改
            client.createOrUpdateItem(appId, ApolloConfig.ENV, ApolloConfig.CLUSTERNAME, ApolloConfig.NAMESPACE,dto);
            // 发布
            NamespaceReleaseDTO nrdto = new NamespaceReleaseDTO();
            nrdto.setEmergencyPublish(true);
            nrdto.setReleasedBy(ApolloConfig.USERID);
            nrdto.setReleaseComment("modify comment : " + dateFormat);
            nrdto.setReleaseTitle("release new attribute "+dateFormat);

            client.publishNamespace(appId, ApolloConfig.ENV, ApolloConfig.CLUSTERNAME, ApolloConfig.NAMESPACE,nrdto);

        } else {
            System.err.println("client is null");

        }
    }
}
