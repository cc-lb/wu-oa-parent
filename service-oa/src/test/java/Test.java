import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wu.ServiceAuthApplication;
import com.wu.auth.controller.SysRoleController;
import com.wu.auth.service.SysRoleService;
import com.wu.model.system.SysRole;
import com.wu.result.Result;
import com.wu.vo.system.SysRoleQueryVo;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * @Classname Test
 * @Description
 * @Date 2023/5/5 19:20
 * @Created by cc
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceAuthApplication.class)//这里的Application是springboot的启动类名

public class Test {
    @Autowired
    private SysRoleService sysRoleService;
    @Resource
    StringRedisTemplate redisTemplate;

    @org.junit.Test
    public void test(){

        /*
         Page<SysRole> pageParam = new Page<>(1, 1);
         SysRoleQueryVo sysRoleQueryVo = new SysRoleQueryVo();
         //2 封装条件，判断条件是否为空，不为空进行封装
         LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
         String roleName = sysRoleQueryVo.getRoleName();
         if(!StringUtils.isEmpty(roleName)){
             wrapper.like(SysRole::getRoleName,roleName);
         }

         //3 调用方法实现
         Page<SysRole> pageModel = sysRoleService.page(pageParam, wrapper);
        System.out.println("=========================");
        System.out.println(pageModel.toString());
        System.out.println(pageModel.getTotal());
        System.out.println(pageModel.getPages());
        System.out.println(pageModel.getRecords());

         */
        System.out.println(redisTemplate.opsForValue().get("admin"));

    }
}
