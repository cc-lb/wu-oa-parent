package com.wu.auth.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.wu.model.system.SysUser;
import com.wu.result.Result;
import com.wu.vo.system.AssignRoleVo;

import java.util.Map;

/**
* @author 起选
* @description 针对表【sys_user(用户表)】的数据库操作Service
* @createDate 2023-05-09 14:35:13
*/
public interface SysUserService extends IService<SysUser> {

    void updateStatus(Long id, Integer status);

    Map<String, Object> getUserInfo(String username);

    Map<String,Object> getCurrentUser();
}
