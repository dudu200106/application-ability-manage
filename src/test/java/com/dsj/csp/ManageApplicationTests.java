package com.dsj.csp;

import com.dsj.csp.manage.biz.AbilityApiBizService;
import com.dsj.csp.manage.biz.impl.AbilityApiBizServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ManageApplicationTests {

    @Autowired
    AbilityApiBizService abilityApiBizService;

    @Test
    void contextLoads() {
    }


    @Test
    void testGetUserApiList() {
        System.out.println(abilityApiBizService.getUserApiList(56415082538L));
    }



}
