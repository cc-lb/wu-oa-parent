package com.wu.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wu.model.system.SysMenu;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* @author 起选
* @description 针对表【sys_menu(菜单表)】的数据库操作Mapper
* @createDate 2023-05-10 13:12:26
* @Entity com.wu.auth.domain.SysMenu
*/
@Repository
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    List<SysMenu> getMenusListByUserId(Long userId);
}




