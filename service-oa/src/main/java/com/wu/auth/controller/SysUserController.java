package com.wu.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wu.auth.service.SysUserService;
import com.wu.md5.MD5;
import com.wu.model.system.SysUser;
import com.wu.result.Result;
import com.wu.vo.system.AssignRoleVo;
import com.wu.vo.system.SysUserQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Classname SysUserController
 * @Description
 * @Date 2023/5/9 14:50
 * @Created by cc
 */

@Api(tags = "用户管理")
@RestController
@RequestMapping("/admin/system/sysUser")
public class SysUserController {
    @Autowired
    SysUserService sysUserService;

    @ApiOperation(value = "获取当前用户基本信息")
    @GetMapping("getCurrentUser")
    public Result getCurrentUser() {
        return Result.ok(sysUserService.getCurrentUser());
    }

    @PreAuthorize("hasAuthority('bnt.sysUser.update')")
    @ApiOperation(value = "更新状态")
    @GetMapping("updateStatus/{id}/{status}")
    public Result updateStatus(@PathVariable Long id, @PathVariable Integer status) {

        sysUserService.updateStatus(id, status);
        return Result.ok();
    }

    @ApiOperation("用户条件分页查询")
     @GetMapping("{page}/{limit}")
    public Result index(@PathVariable Long page,
                        @PathVariable Long limit,
                        SysUserQueryVo sysUserQueryVo) {
        Page<SysUser> page1 = new Page<>(page, limit);
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        //获取条件值
        String username = sysUserQueryVo.getKeyword();
        String createTimeBegin = sysUserQueryVo.getCreateTimeBegin();
        String createTimeEnd = sysUserQueryVo.getCreateTimeEnd();
        //判断条件值不为空
        //like 模糊查询
        if(!StringUtils.isEmpty(username)) {
            wrapper.like(SysUser::getUsername,username);
        }
        //ge 大于等于
        if(!StringUtils.isEmpty(createTimeBegin)) {
            wrapper.ge(SysUser::getCreateTime,createTimeBegin);
        }
        //le 小于等于
        
        if(!StringUtils.isEmpty(createTimeEnd)) {
            wrapper.le(SysUser::getCreateTime,createTimeEnd);
        }
    //调用mp的方法实现条件分页查询
        Page<SysUser> pageModel = sysUserService.page(page1, wrapper);

        return Result.ok(pageModel);
    }


    @ApiOperation("获取用户信息")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id){
        SysUser byId = sysUserService.getById(id);
        return  Result.ok(byId);
    }


    @PreAuthorize("hasAuthority('bnt.sysUser.add')")
    @ApiOperation("保存用户")
    @PostMapping("save")
    public Result save(@RequestBody SysUser sysUser) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("md5");
        String password = sysUser.getPassword();
        String encrypt = MD5.encrypt(password);
        sysUser.setPassword(encrypt);
        boolean save = sysUserService.save(sysUser);
        return  Result.ok(save);
    }


    @PreAuthorize("hasAuthority('bnt.sysUser.update')")
    @ApiOperation(value = "修改用户信息")
    @PutMapping("update")
    public Result updateById(@RequestBody SysUser user) {
        sysUserService.updateById(user);
        return Result.ok();
    }


    @PreAuthorize("hasAuthority('bnt.sysUser.remove')")
    @ApiOperation(value = "删除用户")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
       sysUserService.removeById(id);
        return Result.ok();
    }
}
