package com.wu.auth.controller;

import com.wu.auth.service.SysMenuService;
import com.wu.model.system.SysMenu;
import com.wu.result.Result;
import com.wu.vo.system.AssignMenuVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Classname SysMenuController
 * @Description
 * @Date 2023/5/10 13:16
 * @Created by cc
 */

@Api(tags = "菜单管理")
@RestController
@RequestMapping("/admin/system/sysMenu")
public class SysMenuController {
    @Autowired
    SysMenuService sysMenuService;


    @ApiOperation("获取角色的权限")
    @GetMapping("toAssign/{roleId}")
    public Result toAssign(@PathVariable Long roleId){
      List<SysMenu> list= sysMenuService.toAssign(roleId);
    return Result.ok(list);
    }

    @ApiOperation("修改角色权限")
    @PostMapping("doAssign")
    public Result doAssign(@RequestBody AssignMenuVo assignMenuVo){
        sysMenuService.doAssign(assignMenuVo);
    return Result.ok();
    }
    @ApiOperation("获取菜单")
    @GetMapping("findNodes")
    public Result  findNodes(){

         List<SysMenu> list = sysMenuService.findNodes();

        return Result.ok(list);

    }
    @PreAuthorize("hasAuthority('bnt.sysMenu.add')")
    @ApiOperation("增加菜单")
    @PostMapping("save")
    public Result  save(@RequestBody SysMenu sysMenu){
        boolean save = sysMenuService.save(sysMenu);
        return Result.ok();

    }

    @PreAuthorize("hasAuthority('bnt.sysMenu.remove')")
    @ApiOperation("删除菜单")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id){
        boolean removeById = sysMenuService.removeById(id);

        return Result.ok();

    }

    @PreAuthorize("hasAuthority('bnt.sysMenu.update')")
    @ApiOperation("修改菜单")
    @PutMapping("update")
    public Result update(@RequestBody SysMenu sysMenu){
        boolean update = sysMenuService.updateById(sysMenu);

        return Result.ok();

    }




}
