package com.dsj.csp.common.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 功能说明：基础实体类
 *
 * @author 蔡云
 * 2023/12/29
 */
@Data
public class BaseEntity implements Serializable {

    /**
     * 逻辑删除（0未删除 1已删除 ）
     */
    @TableLogic
    @TableField(value = "IS_DELETE")
    private Integer isDelete;

    /**
     * 创建时间
     */
    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

}
