package com.dsj.csp;

import com.dsj.csp.manage.biz.AbilityApiBizService;
import com.dsj.csp.manage.entity.AbilityApiApplyEntity;
import com.dsj.csp.manage.service.AbilityApiApplyService;
import com.dsj.csp.manage.service.AbilityApplyService;
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
    @Autowired
    private AbilityApiApplyService abilityApiApplyService;

    @Autowired
    private AbilityApplyService abilityApplyService;

    @Test
    void testAppid(){

        abilityApiBizService.getAppApiList(1749358566426378242L);
    }
    @Test
    void testGetUserApiList(){
        abilityApiBizService.getUserApiList(56415082531L);
    }

    @Test
    void testApiApply(){

        AbilityApiApplyEntity apply =  abilityApiApplyService.getById(1);
        System.out.println(apply);
    }

    @Test
    void testDeleteApply(){

        abilityApplyService.deleteApplyByAppId(1750354280476332033L);
    }
    @Test
    void testPageApis(){
        abilityApiBizService.pageApiCatalog(true, null,  null, null, 100, 1, null, null);
    }

    @Test
    void testPageApplyApi(){
        abilityApiBizService.pagePassedApis(null, null,  1749356491904835586L, null, 100, 1, null, null);
    }

}
