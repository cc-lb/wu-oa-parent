package com.wu.common.helper;



import com.wu.model.system.SysMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * @Classname MenuHelper
 * @Description
 * @Date 2023/5/10 16:08
 * @Created by cc
 */
public class MenuHelper {


    /**
     * 将sysMeanu表中数据构建成一个已parentId=0为顶点的树状结构
     * @param sysMenus
     * @return
     */
    public static List<SysMenu> buildTree(List<SysMenu> sysMenus) {
        ArrayList<SysMenu> sysMenuList = new ArrayList<>();
        //找到parent=0的顶点
        for (SysMenu item:
                sysMenus) {
            if(item.getParentId().longValue()==0){
                //获取它的子节点，也就是下级菜单
                SysMenu children = findChildren(item, sysMenus);
                sysMenuList.add(children);
            }

        }
        return sysMenuList;
    }

    /**
     * 递归查找子菜单数据
     * @param sysMenu
     * @param list
     * @return
     */
    public static SysMenu findChildren(SysMenu sysMenu, List<SysMenu> list){

        //为此时的传来的头结点设置子节点位置
        List<SysMenu> children = new ArrayList<>();
        sysMenu.setChildren(children);

        //递归遍历表中数据，找到头结点的子节点,dfs(深度遍历)
        for ( SysMenu it:
             list) {
            if(it.getParentId().longValue()==sysMenu.getId().longValue()){
                if (sysMenu.getChildren() == null) {
                    sysMenu.setChildren(new ArrayList<SysMenu>());
                }
                sysMenu.getChildren().add(findChildren(it,list));
            }

        }
        return sysMenu;

    }
}
