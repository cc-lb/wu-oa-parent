package com.wu.process.service;

import com.wu.model.process.ProcessType;

import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 起选
* @description 针对表【oa_process_type(审批类型)】的数据库操作Service
* @createDate 2023-05-30 20:31:08
*/
public interface ProcessTypeService extends IService<ProcessType> {
    List<ProcessType> findProcessType();
}
