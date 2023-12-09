package com.wu.wechat.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.wu.model.wechat.WechatMenu;
import com.wu.vo.wechat.MenuVo;

import java.util.List;

/**
* @author 起选
* @description 针对表【wechat_menu(菜单)】的数据库操作Service
* @createDate 2023-06-11 13:54:47
*/
public interface WechatMenuService extends IService<WechatMenu> {
    List<MenuVo> findMenuInfo();

    void syncMenu();

    void removeMenu();
}
