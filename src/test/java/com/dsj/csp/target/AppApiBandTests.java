package com.dsj.csp.target;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dsj.csp.manage.biz.AbilityApiBizService;
import com.dsj.csp.manage.entity.AppApiBand;
import com.dsj.csp.manage.service.AppApiBandService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AppApiBandTests {
    @Autowired
    AbilityApiBizService abilityApiBizService;
    @Autowired
    AppApiBandService appApiBandService;

    @Test
    void testSaveAppApiBand() {
        AppApiBand appApiBand = new AppApiBand();
        appApiBand.setAppId(11L);
        appApiBand.setApiId(22L);
        appApiBand.setAbilityId(33L);
        appApiBand.setUserId("44");
        System.out.println(appApiBandService.save(appApiBand));
    }

    @Test
    void testGetAppApiBand() {
        System.out.println(appApiBandService.getOne(Wrappers.lambdaQuery(AppApiBand.class)
                .eq(AppApiBand::getAppId, 11L)
                .eq(AppApiBand::getApiId, 22L)
        ));
    }

    @Test
    void testDeleteAppApiBand() {
        System.out.println(appApiBandService.remove(Wrappers.lambdaQuery(AppApiBand.class)
                .eq(AppApiBand::getAppId, 11L)
                .eq(AppApiBand::getApiId, 22L)
        ));
    }

}
