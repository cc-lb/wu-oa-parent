package com.wu.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wu.model.system.SysMenu;
import com.wu.model.system.SysRoleMenu;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* @author 起选
* @description 针对表【sys_role_menu(角色菜单)】的数据库操作Mapper
* @createDate 2023-05-10 13:12:52
* @Entity com.wu.auth.domain.SysRoleMenu
*/
@Repository
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {


}




