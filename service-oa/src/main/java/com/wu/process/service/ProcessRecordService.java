package com.wu.process.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wu.model.process.ProcessRecord;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
* @author 起选
* @description 针对表【oa_process_record(审批记录)】的数据库操作Service
* @createDate 2023-06-06 20:22:00
*/
public interface ProcessRecordService extends IService<ProcessRecord> {


        //跨域
    void record(Long processId, Integer status, String description);
}
