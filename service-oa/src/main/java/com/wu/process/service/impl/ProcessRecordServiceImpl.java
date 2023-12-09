package com.wu.process.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wu.auth.service.SysUserService;
import com.wu.model.process.ProcessRecord;
import com.wu.model.system.SysUser;
import com.wu.process.service.ProcessRecordService;
import com.wu.process.mapper.ProcessRecordMapper;
import com.wu.security.hlper.LoginInfoHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author 起选
* @description 针对表【oa_process_record(审批记录)】的数据库操作Service实现
* @createDate 2023-06-06 20:22:00
*/
@Service
public class ProcessRecordServiceImpl extends ServiceImpl<ProcessRecordMapper, ProcessRecord>
    implements ProcessRecordService{

    @Autowired
    SysUserService sysUserService;
    @Autowired
    ProcessRecordMapper processRecordMapper;

    @Override
    public void record(Long processId, Integer status, String description) {
        SysUser sysUser = sysUserService.getById(LoginInfoHelper.getUserId());
        ProcessRecord processRecord = new ProcessRecord();
        processRecord.setProcessId(processId);
        processRecord.setStatus(status);
        processRecord.setDescription(description);
        processRecord.setOperateUserId(sysUser.getId());
        processRecord.setOperateUser(sysUser.getName());
        processRecordMapper.insert(processRecord);
    }
}




