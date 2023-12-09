package com.wu.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wu.auth.mapper.SysRoleMapper;
import com.wu.auth.mapper.SysUserRoleMapper;
import com.wu.auth.service.SysRoleService;
import com.wu.model.system.SysRole;
import com.wu.model.system.SysUser;
import com.wu.model.system.SysUserRole;
import com.wu.vo.system.AssignRoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Classname SysRoleServiceImpl
 * @Description
 * @Date 2023/4/3 21:37
 * @Created by cc
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService  {
 @Autowired
    SysUserRoleMapper sysUserRoleMapper;

    /**
     * 通过用户id查到他的角色,一个用户多个角色
     * @param userId
     * @return
     */
    @Override
    public Map<String, Object> findRoleByAdminId(Long userId) {
          

        //查看所有角色
        List<SysRole> list = this.list(null);

        //通过用户id求出角色id
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, userId).select(SysUserRole::getRoleId);
        List<SysUserRole> sysUserRoles = sysUserRoleMapper.selectList(wrapper);
        System.out.println(sysUserRoles.toString());
        List<Long> roleIdList = sysUserRoles.stream().map(c -> c.getRoleId()).collect(Collectors.toList());

        //将角色id与查到的所有的角色的id相对比，相等存入一个集合
        ArrayList<SysRole> sysRoles = new ArrayList<>();
        for (SysRole sysRole:
             list) {
            if(roleIdList.contains(sysRole.getId()))
                sysRoles.add(sysRole);
        }

        //将用户的角色和所有的角色存入，所有的角色有需求，在前端复选框将所有角色列出来让人修改
        HashMap<String,Object> map = new HashMap<>();
        map.put("assginRoleList",sysRoles);
        map.put("allRolesList",list);
        System.out.println(list.toString());
        return map;


    }


    /**
     *t前端分配角色，传来用户id，用户角色封装为一个类。
     * 此方法删除sysUserRole表中原来的用户数据，增加新数据
     * @param assignRoleVo
     */
    @Override
    public void doAssign(AssignRoleVo assignRoleVo) {
            //删掉表中原有用户
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getUserId, assignRoleVo.getUserId());
        sysUserRoleMapper.delete(wrapper);
        //为表中存入新的用户和他的角色
        for (Long roleId:
             assignRoleVo.getRoleIdList()) {
            if(StringUtils.isEmpty(roleId)) continue;
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setRoleId(roleId);
            sysUserRole.setUserId(assignRoleVo.getUserId());
            int insert = sysUserRoleMapper.insert(sysUserRole);

        }
    }


}
