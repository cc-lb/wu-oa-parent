package com.wu.process.mapper;

import com.wu.model.process.ProcessType;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
* @author 起选
* @description 针对表【oa_process_type(审批类型)】的数据库操作Mapper
* @createDate 2023-05-30 20:31:08
* @Entity com.wu.process.domain.OaProcessType
*/
@Repository
public interface ProcessTypeMapper extends BaseMapper<ProcessType> {

}




