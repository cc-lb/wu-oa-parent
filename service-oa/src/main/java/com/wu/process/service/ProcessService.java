package com.wu.process.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wu.model.process.Process;
import com.wu.vo.process.ApprovalVo;
import com.wu.vo.process.ProcessFormVo;
import com.wu.vo.process.ProcessQueryVo;
import com.wu.vo.process.ProcessVo;

import java.util.List;
import java.util.Map;


/**
* @author 起选
* @description 针对表【oa_process(审批类型)】的数据库操作Service
* @createDate 2023-06-04 14:37:56
*/

public interface ProcessService  extends IService<Process> {

    IPage<ProcessVo> selectPage(Page<ProcessVo> pageParam, ProcessQueryVo processQueryVo);

    //部署zip文件
    void deployByZip(String deployPath);


    Process startUp(ProcessFormVo processFormVo);

    IPage<ProcessVo>  findPending(Page<Process> processPageParam);

    Map<String,Object> show(Long id);

    void approve(ApprovalVo approvalVo);

    IPage<ProcessVo> findProcessed(Page<Process> pageParam);

    IPage<ProcessVo> findStarted(Page<ProcessVo> pageParam);
}
