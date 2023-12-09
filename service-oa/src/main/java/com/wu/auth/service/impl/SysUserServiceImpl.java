package  com.wu.auth.service.impl;



import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wu.auth.mapper.SysRoleMapper;
import com.wu.auth.mapper.SysUserMapper;
import com.wu.auth.mapper.SysUserRoleMapper;
import com.wu.auth.service.SysMenuService;
import com.wu.auth.service.SysUserRoleService;
import com.wu.auth.service.SysUserService;
import com.wu.model.system.SysMenu;
import com.wu.model.system.SysRole;
import com.wu.model.system.SysUser;
import com.wu.model.system.SysUserRole;
import com.wu.result.Result;
import com.wu.security.hlper.LoginInfoHelper;
import com.wu.vo.system.AssignRoleVo;
import com.wu.vo.system.RouterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
* @author 起选
* @description 针对表【sys_user(用户表)】的数据库操作Service实现
* @createDate 2023-05-09 14:35:13
*/
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
    implements SysUserService {


     @Autowired
    SysMenuService sysMenuService;
     @Autowired
     SysUserMapper sysUserMapper;


    @Transactional
    @Override
    public void updateStatus(Long id, Integer status) {
        SysUser sysUser = this.getById(id);
        if(status.intValue() == 1) {
            sysUser.setStatus(status);
        } else {
            sysUser.setStatus(0);
        }
        this.updateById(sysUser);
    }

    @Override
    public Map<String, Object> getUserInfo(String username) {
        //由用户名得到用户个人信息
        SysUser user= this.getOne(new  LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, username));
        //获得其菜单权限
        List<RouterVo> routerVoList = sysMenuService.findUserMenuList(user.getId());
        //获得菜单权限中的按钮权限
        List<String> userPermsList = sysMenuService.findUserPermsList(user.getId());
        HashMap<String, Object> result = new HashMap<>();



        result.put("name", user.getName());
        result.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        //当前权限控制使用不到，我们暂时忽略
        result.put("roles",  new HashSet<>());
        result.put("buttons", userPermsList);
        result.put("routers", routerVoList);
        return result;
    }

    @Override
    public Map<String, Object> getCurrentUser() {
        SysUser sysUser = sysUserMapper.selectById(LoginInfoHelper.getUserId());
        //SysDept sysDept = sysDeptService.getById(sysUser.getDeptId());
        //SysPost sysPost = sysPostService.getById(sysUser.getPostId());
        Map<String, Object> map = new HashMap<>();
        map.put("name", sysUser.getName());
        map.put("phone", sysUser.getPhone());
        //map.put("deptName", sysDept.getName());
        //map.put("postName", sysPost.getName());
        return map;
    }
}




