package com.wu.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wu.auth.service.SysMenuService;
import com.wu.auth.service.SysUserService;
import com.wu.model.system.SysUser;
import com.wu.security.custom.CustomUser;
//import com.wu.security.interf.UserDetails;
import com.wu.system.service.UserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import java.util.List;

/**
 * @Classname UserDetailServiceImpl
 * @Description
 * @Date 2023/5/15 21:26
 * @Created by cc
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    SysUserService sysUserService;
    @Autowired
    SysMenuService sysMenuService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        SysUser user = sysUserService.getOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, username));
        if(user==null) throw new UsernameNotFoundException( "用户名不存在");
        if(user.getStatus().intValue()==0)
            throw new RuntimeException("该用户已停用");
        List<String> userPermsList = sysMenuService.findUserPermsList(user.getId());
        ArrayList<SimpleGrantedAuthority> authorityArrayList = new ArrayList<>();
        for (String userPerms:userPermsList) {
            authorityArrayList.add(new SimpleGrantedAuthority(userPerms.trim()));
        }


        return   new CustomUser(user,authorityArrayList);
    }
}
