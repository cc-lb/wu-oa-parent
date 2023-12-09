package com.wu.process.mapper;




 import com.baomidou.mybatisplus.core.mapper.BaseMapper;
 import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wu.model.process.Process;
import com.wu.vo.process.ProcessQueryVo;
import com.wu.vo.process.ProcessVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
* @author 起选
* @description 针对表【oa_process(审批类型)】的数据库操作Mapper
* @createDate 2023-06-04 14:37:56
* @Entity com.wu.process.domain._process
*/

@Mapper
public interface ProcessMapper extends BaseMapper<Process> {

    IPage<ProcessVo> selectPage1(Page<ProcessVo> page, @Param("vo") ProcessQueryVo processQueryVo);
}




