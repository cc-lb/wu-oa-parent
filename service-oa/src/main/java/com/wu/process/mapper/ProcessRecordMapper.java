package com.wu.process.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wu.model.process.ProcessRecord;
import org.springframework.stereotype.Repository;

/**
* @author 起选
* @description 针对表【oa_process_record(审批记录)】的数据库操作Mapper
* @createDate 2023-06-06 20:22:00
* @Entity com.wu.process.domain.ProcessRecord
*/
@Repository
public interface ProcessRecordMapper extends BaseMapper<ProcessRecord> {

}




