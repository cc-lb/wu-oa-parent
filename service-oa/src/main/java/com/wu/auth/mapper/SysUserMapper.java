package  com.wu.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wu.model.system.SysUser;
import org.springframework.stereotype.Repository;

/**
* @author 起选
* @description 针对表【sys_user(用户表)】的数据库操作Mapper
* @createDate 2023-05-09 14:35:13
* @Entity com/wu/auth.domain.SysUser
*/
@Repository
public interface SysUserMapper extends BaseMapper<SysUser> {

}




