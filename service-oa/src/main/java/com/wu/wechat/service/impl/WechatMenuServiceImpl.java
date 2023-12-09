package com.wu.wechat.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.wu.auth.service.SysMenuService;
import com.wu.model.wechat.WechatMenu;
import com.wu.vo.wechat.MenuVo;
import com.wu.wechat.mapper.WechatMenuMapper;
import com.wu.wechat.service.WechatMenuService;

import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author 起选
* @description 针对表【wechat_menu(菜单)】的数据库操作Service实现
* @createDate 2023-06-11 13:54:47
*/
@Service
public class WechatMenuServiceImpl  extends ServiceImpl<WechatMenuMapper,WechatMenu>
    implements WechatMenuService{
    @Autowired
     private WxMpService wxMpService;

    @Override
    public List<MenuVo> findMenuInfo() {

         //可以采用另一种方法：首先查询全部信息，对查询的信息使用stream流集合操作
        //这种方式好处：不用多次查询数据库节约性能。
        /*
         List<MenuVo> list = new ArrayList<>();
        List<Menu> menuList = menuMapper.selectList(null);
        List<Menu> oneMenuList = menuList.stream().filter(menu -> menu.getParentId().longValue() == 0).collect(Collectors.toList());
        for (Menu oneMenu : oneMenuList) {
            MenuVo oneMenuVo = new MenuVo();
            BeanUtils.copyProperties(oneMenu, oneMenuVo);

            List<Menu> twoMenuList = menuList.stream()
                    .filter(menu -> menu.getParentId().longValue() == oneMenu.getId())
                    .sorted(Comparator.comparing(Menu::getSort))
                    .collect(Collectors.toList());
            List<MenuVo> children = new ArrayList<>();
            for (Menu twoMenu : twoMenuList) {
                MenuVo twoMenuVo = new MenuVo();
                BeanUtils.copyProperties(twoMenu, twoMenuVo);
                children.add(twoMenuVo);
            }
            oneMenuVo.setChildren(children);
            list.add(oneMenuVo);
        }
        return list;
         */



        //下面操作多次查询有点浪费性能
        List<WechatMenu> wechatMenuList = this.list(new LambdaQueryWrapper<WechatMenu>()
                .eq(WechatMenu::getParentId, 0));
                //一级菜单类型转换 WeChatMenu===》WeChatMenuVo
        ArrayList<MenuVo> oneWechatMenuList = new ArrayList<>();
        for(WechatMenu wechatMenu :wechatMenuList){
            MenuVo wechatMenuVo = new MenuVo();
            BeanUtils.copyProperties(wechatMenu, wechatMenuVo);
            List<WechatMenu> twoMenuList = this.list(new LambdaQueryWrapper<WechatMenu>()
                    .eq(WechatMenu::getParentId, wechatMenu.getId())
                    .orderByDesc(WechatMenu::getSort));

                 //二级菜单类型转换 WeChatMenu===》WeChatMenuVo
            ArrayList<MenuVo> twoWechatMenuVoList= new ArrayList<>();
            for(WechatMenu twoWechatMenu:twoMenuList){
                        MenuVo twoMenuVo=new MenuVo();
                        BeanUtils.copyProperties(twoWechatMenu, twoMenuVo);
                        twoWechatMenuVoList.add(twoMenuVo);
            }

            wechatMenuVo.setChildren(twoWechatMenuVoList);
            oneWechatMenuList.add(wechatMenuVo);

        }

        return oneWechatMenuList;
    }



    @Override
    public void syncMenu() {
        List<MenuVo> menuVoList = this.findMenuInfo();
        //菜单
        JSONArray buttonList = new JSONArray();
        for(MenuVo oneMenuVo : menuVoList) {
            JSONObject one = new JSONObject();
            one.put("name", oneMenuVo.getName());
            if(CollectionUtils.isEmpty(oneMenuVo.getChildren())) {
                one.put("type", oneMenuVo.getType());
                one.put("url", "http://ggkt1.vipgz1.91tunnel.com/#"+oneMenuVo.getUrl());
            } else {
                JSONArray subButton = new JSONArray();
                for(MenuVo twoMenuVo : oneMenuVo.getChildren()) {
                    JSONObject view = new JSONObject();
                    view.put("type", twoMenuVo.getType());
                    if(twoMenuVo.getType().equals("view")) {
                        view.put("name", twoMenuVo.getName());
                        //H5页面地址
                        view.put("url", "http://ggkt1.vipgz1.91tunnel.com#"+twoMenuVo.getUrl());
                    } else {
                        view.put("name", twoMenuVo.getName());
                        view.put("key", twoMenuVo.getMeunKey());
                    }
                    subButton.add(view);
                }
                one.put("sub_button", subButton);
            }
            buttonList.add(one);
        }
        //菜单
        JSONObject button = new JSONObject();
        button.put("button", buttonList);
        try {
            wxMpService.getMenuService().menuCreate(button.toJSONString());
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeMenu() {
        try {
            wxMpService.getMenuService().menuDelete();
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
    }
}




