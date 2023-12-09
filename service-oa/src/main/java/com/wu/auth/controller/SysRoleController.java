package com.wu.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wu.auth.mapper.SysRoleMapper;
import com.wu.auth.service.SysRoleService;
import com.wu.model.system.SysRole;
import com.wu.result.Result;
import com.wu.vo.system.AssignRoleVo;
import com.wu.vo.system.SysRoleQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Classname SysRoleController
 * @Description
 * @Date 2023/4/3 21:32
 * @Created by cc
 */
@Api(tags = "角色管理")
@RestController
@RequestMapping("/admin/system/sysRole")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;




    //给用户分配角色，一个用户多个角色
    //1.前端弹框，后端查询角色数据

    @ApiOperation("根据用户获取角色数据")
    @GetMapping("toAssign/{id}")
    public  Result toAssign(@PathVariable Long id){
        Map<String, Object> roleByAdminId =sysRoleService.findRoleByAdminId(id);
        return Result.ok(roleByAdminId);
    }
    //2. 前端根据复选框修改用户角色，后端修改用户的角色
    @ApiOperation("根据用户分配角色")
    @PostMapping("doAssign")
    public Result doAssign(@RequestBody AssignRoleVo assignRoleVo){
        sysRoleService.doAssign(assignRoleVo);
        return Result.ok();
    }


    @ApiOperation(value = "获取全部角色列表")
    @GetMapping("findAll")
    public List<SysRole> findAll(){
        List<SysRole> roleList=sysRoleService.list();
   return roleList;
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.add')")
    @ApiOperation("增加角色")
    @PostMapping("save")
    public Result save(@RequestBody SysRole sysRole){

        if(sysRole==null) return Result.fail();
       // System.out.println("sysRole不为空");
     //   System.out.println(sysRole.toString());
        boolean save = sysRoleService.save(sysRole);
            if(save) return Result.ok();
            return  Result.fail();
    }


    //@PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @ApiOperation(value = "条件分页查询")
    @GetMapping("{page}/{limit}")
public Result pageQueryRole(@PathVariable long page, @PathVariable long limit
        , SysRoleQueryVo sysRoleQueryVo){
        //调用service的方法实现
        //1 创建Page对象，传递分页相关参数
        //page 当前页  limit 每页显示记录数
        Page<SysRole> pageParam = new Page<>(page, limit);

        //2 封装条件，判断条件是否为空，不为空进行封装
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        String roleName = sysRoleQueryVo.getRoleName();
        if(!StringUtils.isEmpty(roleName)){
            wrapper.like(SysRole::getRoleName,roleName);
        }

        //3 调用方法实现
     Page<SysRole> pageModel = sysRoleService.page(pageParam, wrapper);

        // System.out.println(pageModel);
            return  Result.ok(pageModel);

    }

   @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @ApiOperation("修改角色-根据id查询")
    @GetMapping("get/{id}")
    public Result<SysRole> get(@PathVariable Long id){
        SysRole byId = sysRoleService.getById(id);
    return  Result.ok(byId);
    }

   @PreAuthorize("hasAuthority('bnt.sysRole.update')")
    @ApiOperation("修改角色-最终修改")
    @PutMapping("update")
    public Result updateById(@RequestBody SysRole sysRole){
        sysRoleService.updateById(sysRole);
        return  Result.ok();
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.remove')")
    @ApiOperation(value = "删除角色")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        sysRoleService.removeById(id);
        return Result.ok();
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.remove')")
    @ApiOperation(value = "根据id列表批量删除")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList) {

        sysRoleService.removeByIds(idList);
        return  Result.ok();
    }


}
