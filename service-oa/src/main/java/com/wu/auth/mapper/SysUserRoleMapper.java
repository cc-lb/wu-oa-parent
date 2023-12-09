package com.wu.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wu.model.system.SysUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* @author 起选
* @description 针对表【sys_user_role(用户角色)】的数据库操作Mapper
* @createDate 2023-05-09 16:21:01
* @Entity com.wu.auth.domain.SysUserRole
*/
@Repository
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

}




