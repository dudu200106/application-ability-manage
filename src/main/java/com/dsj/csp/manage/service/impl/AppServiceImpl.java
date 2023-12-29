package com.dsj.csp.manage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dsj.csp.manage.entity.AppEntity;
import com.dsj.csp.manage.service.AppService;
import com.dsj.csp.manage.mapper.AppMapper;
import org.springframework.stereotype.Service;

/**
* @author lakkamaa
* @description 针对表【MANAGE_APP(应用表)】的数据库操作Service实现
* @createDate 2023-12-28 20:04:34
*/
@Service
public class AppServiceImpl extends ServiceImpl<AppMapper, AppEntity>
    implements AppService{

}




