package com.dsj.csp;

import com.dsj.csp.manage.biz.AbilityApiBizService;
import com.dsj.csp.manage.service.AbilityApiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ManageApplicationTests {


    @Test
    void contextLoads() {
    }

    @Autowired
    private AbilityApiBizService abilityApiBizService;

    @Test
    void testAppid(){

        abilityApiBizService.getAppApiList(1749358566426378242L);
    }
    @Test
    void testGetUserApiList(){
        abilityApiBizService.getUserApiList(56415082531L);
    }

}
