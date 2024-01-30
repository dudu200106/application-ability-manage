package com.dsj.csp.manage.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 用户信息表（个人信息，认证信息）
 * @TableName GXYYZC_YHB
 */
@TableName(value ="GXYYZC_YHB")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserApproveEntity implements Serializable {
    /**
     * 用户ID
     */
    @TableId(value = "YH_XH")
    @Schema(description = "用户ID")
    @NotBlank(message = "用户ID不能为空")
    private String userId;

    /**
     * 用户账号
     */
    @TableField(value = "YH_MC")
    @Schema(description = "用户账号名")
    private String userName;

    /**
     * 用户手机号
     */
    @TableField(value = "DHHM")
    @Schema(description = "用户手机号")
    private String phone;

    /**
     * 邮箱
     */
    @TableField(value = "DZYX")
    @Schema(description = "邮箱")
    private String email;

    /**
     * 认证状态（（0未实名 1待审核 2审核通过 3审核不通过））
     */
    @TableField(value = "ZT")
    @Schema(description = "认证状态（0未实名（默认） 1待审核 2审核通过 3审核不通过）")
    private Integer status;

    /**
     * 企业名称
     */
    @TableField(value = "QY_MC")
    @Schema(description = "企业名称")
    @NotBlank(message = "企业名称不能为空")
    private String companyName;

    /**
     * 营业执照注册号
     */
    @TableField(value = "QY_ZCH")
    @Schema(description = "企业营业执照注册号")
    @NotBlank(message = "营业执照注册号不能为空")
    private String companyNum;

    /**
     * 企业法定代表人
     */
    @TableField(value = "QY_FDDBR")
    @Schema(description = "企业法定代表人")
    @NotBlank(message = "企业法定代表人不能为空")
    private String companyRepresent;

    /**
     * 营业执照扫描件
     */
    @TableField(value = "QY_TP")
    @Schema(description = "企业营业执照扫描件")
    @NotBlank(message = "企业营业执照扫描件不能为空")
    private String companyImage;

    /**
     * 授权函
     */
    @TableField(value = "SQH")
    @Schema(description = "政府授权函")
    @NotBlank(message = "政府授权函不能为空")
    private String authorization;

    /**
     * 企业联系人姓名
     */
    @TableField(value = "QY_LXRXM")
    @Schema(description = "企业联系人姓名")
    @NotBlank(message = "企业联系人姓名不能为空")
    private String companyUser;

    /**
     * 企业联系人身份证号
     */
    @TableField(value = "QY_SFZH")
    @Schema(description = "企业联系人身份证号")
    @NotBlank(message = "企业联系人身份证号不能为空")
    private String companyIdnum;

    /**
     * 企业联系人手机号码
     */
    @TableField(value = "QY_DHHM")
    @Schema(description = "企业联系人手机号码")
    @NotBlank(message = "企业联系人手机号码不能为空")
    private String companyPhone;

    /**
     * 政府部门名称
     */
    @TableField(value = "ZF_MC")
    @Schema(description = "政府部门名称")
    @NotBlank(message = "政府部门名称不能为空")
    private String govName;

    /**
     * 营业执照注册号
     */
    @TableField(value = "ZF_ZCH")
    @Schema(description = "政府营业执照注册号")
    @NotBlank(message = "政府营业执照注册号不能为空")
    private String govNum;

    /**
     * 政府部门法定代表人
     */
    @TableField(value = "ZF_FDDBR")
    @Schema(description = "政府部门法定代表人")
    @NotBlank(message = "政府部门法定代表人不能为空")
    private String govRepresent;

    /**
     * 政府部门营业执照扫描件
     */
    @TableField(value = "ZF_TP")
    @Schema(description = "政府部门营业执照扫描件")
    @NotBlank(message = "政府部门营业执照扫描件不能为空")
    private String govImage;

    /**
     * 政府部门联系人姓名
     */
    @TableField(value = "ZF_LXRXM")
    @Schema(description = "政府部门联系人姓名")
    @NotBlank(message = "政府部门联系人姓名不能为空")
    private String govUser;

    /**
     * 政府部门身份证号
     */
    @TableField(value = "ZF_SFZH")
    @Schema(description = "政府部门联系人身份证号")
    @NotBlank(message = "政府部门联系人身份证号不能为空")
    private String govIdnum;

    /**
     * 政府部门联系人手机号码
     */
    @TableField(value = "ZF_DHHM")
    @Schema(description = "政府部门联系人手机号码")
    @NotBlank(message = "政府部门联系人手机号码不能为空")
    private String govPhone;

    /**
     * 账户是否可用（0可用(默认)  1已注销）
     */
    @TableField(value = "SFKY")
    @Schema(description = "账户是否可用（0可用(默认)  1已注销）")
    private Integer isDelete;

    /**
     * 创建时间
     */
    @TableField(value = "CJSJ")
    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 备注
     */
    @TableField(value = "BZ")
    @Schema(description = "备注")
    private String note;

    /**
     * 用户类型(0未实名游客 1企业用户 2政府用户)
     */
    @TableField(value = "YH_LX")
    @Schema(description = "用户类型（0未实名游客（默认） 1企业用户 2政府用户）")
    private Integer userType;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}