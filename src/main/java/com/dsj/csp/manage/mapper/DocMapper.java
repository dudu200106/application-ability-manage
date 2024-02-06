package com.dsj.csp.manage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dsj.csp.manage.entity.DocEntity;
import org.apache.ibatis.annotations.Mapper;

/**
* @author SeanDu
* @description 针对表【GXYYZC_WD(文档表)】的数据库操作Mapper
* @createDate 2024-02-06 11:42:52
* @Entity com.dsj.csp.manage.entity.DocEntity
*/
@Mapper
public interface DocMapper extends BaseMapper<DocEntity> {

}
