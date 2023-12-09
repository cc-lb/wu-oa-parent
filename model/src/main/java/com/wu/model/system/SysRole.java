package com.wu.model.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wu.model.base.BaseEntity;
import lombok.Data;

import java.util.Base64;

/**
 * @Classname SysRole
 * @Description
 * @Date 2023/3/28 20:42
 * @Created by cc
 */

@Data
@TableName("sys_role")
public class SysRole extends BaseEntity {
    private static final long serialVersionUID = 1L;

    //角色名称
    @TableField("role_name")
    private String roleName;

    //角色编码
    @TableField("role_code")
    private String roleCode;

    //描述
    @TableField("description")
    private String description;
}
