package com.wu.security.custom;

import com.wu.model.system.SysUser;
//import com.wu.security.interf.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;



import java.util.Collection;

/**
 * @Classname CustomUser
 * @Description
 * @Date 2023/5/15 21:38
 * @Created by cc
 */
 public class CustomUser extends User  {
     SysUser sysUser;

    public CustomUser(SysUser sysUser, Collection<? extends GrantedAuthority> authorities) {
        super(sysUser.getUsername(), sysUser.getPassword(), authorities);
        this.sysUser = sysUser;
}
    public SysUser getSysUser() {
        return sysUser;
    }

    public void setSysUser(SysUser sysUser) {
        this.sysUser = sysUser;
    }
}
