package com.wu.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wu.common.helper.MenuHelper;
import com.wu.auth.mapper.SysRoleMenuMapper;
import com.wu.auth.service.SysMenuService;
import com.wu.auth.mapper.SysMenuMapper;
import com.wu.common.exception.WuException;
import com.wu.model.system.SysMenu;
import com.wu.model.system.SysRoleMenu;
import com.wu.vo.system.AssignMenuVo;
import com.wu.vo.system.MetaVo;
import com.wu.vo.system.RouterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author 起选
* @description 针对表【sys_menu(菜单表)】的数据库操作Service实现
* @createDate 2023-05-10 13:12:26
*/
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu>
    implements SysMenuService{

    @Autowired
    SysRoleMenuMapper sysRoleMenuMapper;
    @Autowired
    SysMenuMapper sysMenuMapper;
    /**
     * 将菜单表中的数据列成一个树状的数据存在SysMenu中，parentid=0的为顶点
     * 一个菜单包含了他的下级菜单，下级菜单包含了下级菜单的下级菜单形成树状结构
     * Sysmenu中有List<SysMenu>属性，这个属性连接它的下级菜单数据
     * @return
     */
    @Override
    public List<SysMenu> findNodes() {
       // 找到所有的菜单数据
        List<SysMenu> sysMenus = this.list(null);
       if(CollectionUtils.isEmpty(sysMenus)) return  null;

       //构建树状的数据
        MenuHelper menuHelper = new MenuHelper();
        List<SysMenu> menus =  menuHelper.buildTree(sysMenus);


        return menus;
    }

    public SysMenuServiceImpl() {
        super();
    }

    @Override
    public boolean removeById(Serializable id) {
        int count = this.count(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getParentId, id));
       if(count>0) throw new WuException(201,"菜单无法删除，请先删除他的下级菜单");
        baseMapper.deleteById(id);
        return false;
    }

    @Override
    public List<SysMenu> toAssign(Long roleId) {
        List<SysMenu> list = this.list(new LambdaQueryWrapper<SysMenu>()
                .eq(SysMenu::getStatus,1));

        List<SysRoleMenu> roleMenus = sysRoleMenuMapper.selectList(new LambdaQueryWrapper<SysRoleMenu>()
                .eq(SysRoleMenu::getRoleId, roleId)
                .select(SysRoleMenu::getMenuId)
        );
        List<Long> menuIdList = roleMenus.stream().map(c -> c.getMenuId()).collect(Collectors.toList());

        list.forEach(item->{
        if(menuIdList.contains(item.getId())){
            item.setSelect(true);
        }else item.setSelect(false);

        });

        List<SysMenu> tree = MenuHelper.buildTree(list);


        return tree;
    }

    @Override
    public void doAssign(AssignMenuVo assignMenuVo) {
        sysRoleMenuMapper.delete(new LambdaQueryWrapper<SysRoleMenu>()
                .eq(SysRoleMenu::getRoleId,assignMenuVo.getRoleId()));

        for (Long item:
             assignMenuVo.getMenuIdList()) {
            if(StringUtils.isEmpty(item)) continue;
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setMenuId(item);
            sysRoleMenu.setRoleId(assignMenuVo.getRoleId().longValue());
            sysRoleMenuMapper.insert(sysRoleMenu);
        }
    }

    @Override
    public List<RouterVo> findUserMenuList(Long userId) {
        //通过用户id得到其菜单权限，菜单权限分为超级管理者和普通用户
        //注意返回的菜单权限是前端Router路径格式。（前端由Router）
        List<SysMenu> menusList=null;
        if(userId.intValue()==1){
             menusList = this.list(null);
        }else {
            menusList=sysMenuMapper.getMenusListByUserId(userId);
        }
        //将查询到的菜单权限构建成树形结构，Router格式也是树形结构的变体
        List<SysMenu> sysMenuTreeList = MenuHelper.buildTree(menusList);//返回的是0，1,2级菜单
        //构建Router结构
        List<RouterVo> routerVoList = this.buildRouterMenus(sysMenuTreeList);
        return  routerVoList;
    }


    /**
     * 根据树形菜单权限建造一个前端Router格式的菜单权限格式
     * //@param sysMenuTreeList
     * @return
     */

    private List<RouterVo> buildRouterMenus(List<SysMenu> sysMenuTreeList) {
        //Router格式是树形格式，其中节点就是RouterVo
        List<RouterVo> routers = new LinkedList<RouterVo>();


        //菜单分为哪几种：
        // 第一是分类菜单（系统菜单，审批菜单只是分类而已），第二是功能类菜单（如角色管理，用户管理），第三类是功能类菜单下的增删改查等操作
        //而前端分配用户的菜单权限只需要管理类菜单的路径





        //递归遍历菜单,将每个菜单中的信息赋给RouterVo节点
        //最终：routers的信息分为三层，
        // 第一层分类菜单的信息（RouterVo），第二层功能类菜单的信息（RouterVo），
        // 第三层管理类菜单下的需要跳转页面的操作信息（RouterVo），注意没有增删改查这些操作信息（他们没跳转页面不需要额外设置）
        for (SysMenu menuTree:
             sysMenuTreeList) {
            RouterVo router= new RouterVo();
            //这是Router特有树形，菜单中没有，需要单独赋予
            router.setHidden(false);
            //这是Router特有树形，菜单中没有，需要单独赋予
            router.setAlwaysShow(false);

            router.setPath(getRouterPath(menuTree));
            router.setComponent(menuTree.getComponent());
            router.setMeta(new MetaVo(menuTree.getName(), menuTree.getIcon()));
            List<SysMenu> children = menuTree.getChildren();
            //判断是否管理类菜单
            if(menuTree.getType().intValue()==1){
                List<SysMenu> pathList = children.stream().filter(c -> !StringUtils.isEmpty(c.getComponent()))
                        .collect(Collectors.toList());
                for (SysMenu hiddenMenu:
                     pathList) {
                    RouterVo hiddenRouter = new RouterVo();
                    hiddenRouter.setHidden(true);
                    hiddenRouter.setAlwaysShow(false);
                    hiddenRouter.setPath(getRouterPath(hiddenMenu));
                    hiddenRouter.setComponent(hiddenMenu.getComponent());
                    hiddenRouter. setMeta(new MetaVo(hiddenMenu.getName(), hiddenMenu.getIcon()));
                    routers.add(hiddenRouter);
                }

            }else {
                    if(!CollectionUtils.isEmpty(children)) {
                            if (children.size() > 0) router.setAlwaysShow(true);
                        //递归
                        router.setChildren(buildRouterMenus(children));
                    }
            }

routers.add(router);
        }

        //System.out.println("=======routers信息==========");
        //System.out.println(routers);
        return routers;


    }



    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    public String getRouterPath(SysMenu menu) {
        String routerPath = "/" + menu.getPath();
        if(menu.getParentId().intValue() != 0) {
            routerPath = menu.getPath();
        }
        return routerPath;
    }

    /**
     * 获取菜单按钮权限
     *  管理其他表，限制其他表的增删改查
     * @param userId
     * @return
     */
    @Override
    public List<String> findUserPermsList(Long userId) {

        List<SysMenu> menus=null;
        if(userId.intValue()==1){
             menus = this.list(new LambdaQueryWrapper<SysMenu>()
                    .eq(SysMenu::getStatus, 1));
        }else {
            menus = sysMenuMapper.getMenusListByUserId(userId);
        }


        List<String> permsList= menus.stream().filter(c->c.getType()==2)
                .map(c -> c.getPerms()).collect(Collectors.toList());
        return permsList;
    }


}




