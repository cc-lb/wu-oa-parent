package com.wu.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wu.model.system.SysMenu;
import com.wu.vo.system.AssignMenuVo;
import com.wu.vo.system.RouterVo;

import java.io.Serializable;
import java.util.List;

/**
* @author 起选
* @description 针对表【sys_menu(菜单表)】的数据库操作Service
* @createDate 2023-05-10 13:12:26
*/
public interface SysMenuService extends IService<SysMenu> {

    List<SysMenu> findNodes();


      boolean removeById(Serializable id);

    //获取角色菜单权限
    List<SysMenu> toAssign(Long id);
        //给角色分配菜单权限
    void doAssign(AssignMenuVo assignMenuVo);

    //用户登录，由用户id得到他的菜单权限
    List<RouterVo> findUserMenuList(Long userId);
    //用户登录，获取他的按钮权限
    List<String> findUserPermsList(Long userId);
}
