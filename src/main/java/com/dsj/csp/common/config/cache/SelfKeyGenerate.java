package com.dsj.csp.common.config.cache;

import com.alibaba.fastjson.JSON;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;


/**
 * @author SeanDu
 * @version 1.0.0
 * @date 2024-02-28
 */
@Component("selfKeyGenerate")
public class SelfKeyGenerate implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        return target.getClass().getSimpleName() + "#" + method.getName() + "(" + JSON.toJSONString(params) + ")";
    }
}
