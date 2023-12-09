package com.wu.process.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wu.model.process.ProcessTemplate;
import com.wu.model.process.ProcessType;

import com.wu.process.service.ProcessTemplateService;
import com.wu.process.service.ProcessTypeService;
import com.wu.process.mapper.ProcessTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 起选
* @description 针对表【oa_process_type(审批类型)】的数据库操作Service实现
* @createDate 2023-05-30 20:31:08
*/
@Service
public class ProcessTypeServiceImpl extends ServiceImpl<ProcessTypeMapper, ProcessType>
    implements ProcessTypeService {


    @Autowired
    ProcessTypeMapper processTypeMapper;
    @Autowired
    ProcessTemplateService processTemplateService;
    @Override
    public List<ProcessType> findProcessType() {
        List<ProcessType> processTypeList = processTypeMapper.selectList(null);

        for(ProcessType processType:processTypeList){

            Long idType = processType.getId();
            System.out.println(idType);
            LambdaQueryWrapper<ProcessTemplate> lambdaQueryWrapper = new LambdaQueryWrapper<ProcessTemplate>()
                    .eq(ProcessTemplate::getProcessTypeId, idType);
            List<ProcessTemplate> processTemplateList = processTemplateService.list(lambdaQueryWrapper);
            processType.setProcessTemplateList(processTemplateList);


        }



        return processTypeList;
    }
}




