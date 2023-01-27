package com.example.apollotest.controller;

import com.example.apollotest.config.JavaConfigBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class Controller {
    @Autowired
    private JavaConfigBean javaConfigBean;

    @RequestMapping("/index")
    public String index(){
        Integer timeout = javaConfigBean.getTimeout();
        String newKey = javaConfigBean.getNewKey();
        log.info("==================  " + newKey + " @@@ " + timeout + " ======");
        return timeout + " === " + newKey;
    }
}
