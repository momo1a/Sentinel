package com.example.apollotest.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.stereotype.Service;

@Service
public class FlowService {

    @SentinelResource(
            value = "com.example.apollotest.service.FlowService:test",
            blockHandler = "testBlockHandler"
    )
    public String test(){
        System.err.println("正常执行！！---");
        return "正常执行！！---";
    }

    public String testBlockHandler(BlockException exception){
        System.err.println("触碰 sentinel 的规则底线了 老铁 ！！---");
        System.err.println(exception.getMessage());
        return "触碰 sentinel 的规则底线了 老铁---";
    }
}
