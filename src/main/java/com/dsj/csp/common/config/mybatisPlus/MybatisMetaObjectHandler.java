package com.dsj.csp.common.config.mybatisPlus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 功能说明：mybatis自动填充处理类
 *
 * @author 蔡云
 * @date 2023/12/29
 */
@Component
public class MybatisMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
//        //创建时间
//        this.setFieldValByName("CREATE_TIME", LocalDateTime.now(), metaObject);
//        //更新时间
//        this.setFieldValByName("UPDATE_TIME", LocalDateTime.now(), metaObject);

        this.strictInsertFill(metaObject, "CREATE_TIME", Date.class, new Date());
        this.strictInsertFill(metaObject, "UPDATE_TIME", Date.class, new Date());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        //更新时间
//        this.setFieldValByName("UPDATE_TIME", LocalDateTime.now(), metaObject);
        this.strictUpdateFill(metaObject, "UPDATE_TIME", Date.class, new Date());
    }
}
