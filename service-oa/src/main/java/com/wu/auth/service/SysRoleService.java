package com.wu.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wu.model.system.SysRole;
import com.wu.vo.system.AssignRoleVo;

import java.util.Map;

/**
 * @Classname SysRoleService
 * @Description
 * @Date 2023/3/28 20:34
 * @Created by cc
 */

public interface SysRoleService extends IService<SysRole> {

    //1 查询所有角色 和 当前用户所属角色
    Map<String, Object> findRoleByAdminId(Long userId);

    //2 为用户分配角色
    void doAssign(AssignRoleVo assignRoleVo);
}
