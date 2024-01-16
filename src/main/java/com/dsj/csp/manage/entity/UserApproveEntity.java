package com.dsj.csp.manage.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户信息表（个人信息，认证信息）
 * @TableName MANAGE_USER
 */
@TableName(value ="MANAGE_USER")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserApproveEntity implements Serializable {
    /**
     * 用户ID
     */
    @TableId(value = "USER_ID")
    @Schema(description = "用户ID")
    private String userId;

    /**
     * 用户账号
     */
    @TableField(value = "USER_NAME")
    @Schema(description = "用户账号名")
    private String userName;

    /**
     * 用户手机号
     */
    @TableField(value = "PHONE")
    @Schema(description = "用户手机号")
    private String phone;

    /**
     * 邮箱
     */
    @TableField(value = "EMAIL")
    @Schema(description = "邮箱")
    private String email;

    /**
     * 认证状态（（0未实名 1待审核 2审核通过 3审核不通过））
     */
    @TableField(value = "STATUS")
    @Schema(description = "认证状态（0未实名（默认） 1待审核 2审核通过 3审核不通过）")
    private Integer status;

    /**
     * 企业名称
     */
    @TableField(value = "COMPANY_NAME")
    @Schema(description = "企业名称")
    private String companyName;

    /**
     * 营业执照注册号
     */
    @TableField(value = "COMPANY_NUM")
    @Schema(description = "企业营业执照注册号")
    private String companyNum;

    /**
     * 企业法定代表人
     */
    @TableField(value = "COMPANY_REPRESENT")
    @Schema(description = "企业法定代表人")
    private String companyRepresent;

    /**
     * 营业执照扫描件
     */
    @TableField(value = "COMPANY_IMAGE")
    @Schema(description = "企业营业执照扫描件")
    private String companyImage;

    /**
     * 授权函
     */
    @TableField(value = "AUTHORIZATION")
    @Schema(description = "政府授权函")
    private String authorization;

    /**
     * 企业联系人姓名
     */
    @TableField(value = "COMPANY_USER")
    @Schema(description = "企业联系人姓名")
    private String companyUser;

    /**
     * 企业联系人身份证号
     */
    @TableField(value = "COMPANY_IDNUM")
    @Schema(description = "企业联系人身份证号")
    private String companyIdnum;

    /**
     * 企业联系人手机号码
     */
    @TableField(value = "COMPANY_PHONE")
    @Schema(description = "企业联系人手机号码")
    private String companyPhone;

    /**
     * 政府部门名称
     */
    @TableField(value = "GOV_NAME")
    @Schema(description = "政府部门名称")
    private String govName;

    /**
     * 营业执照注册号
     */
    @TableField(value = "GOV_NUM")
    @Schema(description = "政府营业执照注册号")
    private String govNum;

    /**
     * 政府部门法定代表人
     */
    @TableField(value = "GOV_REPRESENT")
    @Schema(description = "政府部门法定代表人")
    private String govRepresent;

    /**
     * 政府部门营业执照扫描件
     */
    @TableField(value = "GOV_IMAGE")
    @Schema(description = "政府部门营业执照扫描件")
    private String govImage;

    /**
     * 政府部门联系人姓名
     */
    @TableField(value = "GOV_USER")
    @Schema(description = "政府部门联系人姓名")
    private String govUser;

    /**
     * 政府部门身份证号
     */
    @TableField(value = "GOV_IDNUM")
    @Schema(description = "政府部门联系人身份证号")
    private String govIdnum;

    /**
     * 政府部门联系人手机号码
     */
    @TableField(value = "GOV_PHONE")
    @Schema(description = "政府部门联系人手机号码")
    private String govPhone;

    /**
     * 账户是否可用（0可用(默认)  1已注销）
     */
    @TableField(value = "IS_DELETE")
    @Schema(description = "账户是否可用（0可用(默认)  1已注销）")
    private Integer isDelete;

    /**
     * 创建时间
     */
    @TableField(value = "CREATE_TIME")
    @Schema(description = "创建时间")
    private Date createTime;

    /**
     * 备注
     */
    @TableField(value = "NOTE")
    @Schema(description = "备注")
    private String note;

    /**
     * 用户类型(0未实名游客 1企业用户 2政府用户)
     */
    @TableField(value = "USER_TYPE")
    @Schema(description = "用户类型（0未实名游客（默认） 1企业用户 2政府用户）")
    private Integer userType;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}