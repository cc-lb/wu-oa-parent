package com.wu.vo.system;

import java.io.Serializable;

/**
 * @Classname SysRoleQueryVo
 * @Description
 * @Date 2023/4/17 20:03
 * @Created by cc
 */

/**
 * 角色查询实体
 */
public class SysRoleQueryVo implements Serializable {
private  static  final long serialVersionUID=1L;
private String roleName;
public String getRoleName(){
    return  roleName;
}
public void  setRoleName(String roleName){
    this.roleName=roleName;
}
}
