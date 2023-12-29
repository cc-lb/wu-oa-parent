package com.wu.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wu.auth.service.SysUserService;
import com.wu.common.exception.WuException;
import com.wu.jwt.JwtHelper;
import com.wu.md5.MD5;
import com.wu.model.system.SysUser;
import com.wu.result.Result;
import com.wu.vo.system.LoginVo;
import com.wu.vo.system.RegisterVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @Classname IndexController
 * @Description
 * @Date 2023/5/4 14:25
 * @Created by cc
 */

@Api(tags = "后台登录管理")
@RestController
@RequestMapping("/admin/system/index")
public class IndexController {
        @Autowired
    SysUserService sysUserService;
    /**
     * 登录
     * @return
     */

    @ApiOperation("注册")
    @PostMapping("register")
    public Result login(@RequestBody RegisterVo registerVo){

        return Result.ok();
    }

   @ApiOperation("登录")
    @PostMapping("login")
    public Result login(@RequestBody LoginVo loginVo){
       //登录，通过换来的身份信息密码是否可以登录
       //若可以登录，则传回一个token代表其身份，分配其身份可以看见的权限，利用工具加密


       //1.判断其是否可以登录
       SysUser user  = sysUserService.getOne(new LambdaQueryWrapper<SysUser>()
               .eq(SysUser::getUsername, loginVo.getUsername()));
       if(user ==null)
           throw  new WuException(201,"此用户不存在");

       String passwordInput = MD5.encrypt(loginVo.getPassword());
        if(!passwordInput.equals(user.getPassword()))
            throw  new WuException(201,"密码错误");

       if(user.getStatus().intValue() == 0) {
           throw new WuException(201,"用户被禁用");
       }
       Map<String, Object> map= new HashMap<>();
        map.put("token", JwtHelper.createToken(user.getId(),user.getUsername()));
        return Result.ok(map);

    }

    @ApiOperation("信息")
    @GetMapping("info")
    public Result  info(HttpServletRequest httpServletRequest){
       //从请求头中获取到有JWT加密过的token
        String token = httpServletRequest.getHeader("token");
        System.out.println("=======token信息====="+token);

        //由token中获取到用户的登录信息，从这个信息当中可以得知用户的菜单权限和按钮权限
        String username = JwtHelper.getUsername(token);
       Map<String,Object>map= sysUserService.getUserInfo(username);
        /*
        map.put("roles","[admin]");
        map.put("name","admin");
        map.put("avatar","https://oss.aliyuncs.com/aliyun_id_photo_bucket/default_handsome.jpg");
        map.put("buttons", new ArrayList<>());
        map.put("routers", new ArrayList<>());

         */
        return Result.ok(map);

    }


   @ApiOperation("退出")
    @PostMapping("logout")
    public Result  logout(){


        return Result.ok();

    }

}
