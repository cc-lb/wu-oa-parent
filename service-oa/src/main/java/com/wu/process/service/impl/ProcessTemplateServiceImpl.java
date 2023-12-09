package com.wu.process.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wu.model.process.ProcessTemplate;
import com.wu.process.service.ProcessService;
import com.wu.process.service.ProcessTemplateService;
import com.wu.process.mapper.ProcessTemplateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
* @author 起选
* @description 针对表【oa_process_template(审批模板)】的数据库操作Service实现
* @createDate 2023-05-30 20:31:23
*/
@Service
public class ProcessTemplateServiceImpl extends ServiceImpl<ProcessTemplateMapper, ProcessTemplate>
    implements ProcessTemplateService {

    @Resource
  private   ProcessTemplateMapper processTemplateMapper;


    @Autowired
    ProcessService processService;

    @Override
    public IPage<ProcessTemplate> selectPage(Page<ProcessTemplate> pageParam) {

        //目的：为不在数据库中的流程类型名赋值
        //1. 获得其数据库中数据,由小到大
         Page<ProcessTemplate> page = processTemplateMapper.selectPage(pageParam,
                new LambdaQueryWrapper<ProcessTemplate>().orderByDesc(ProcessTemplate::getId));

        //2. 为数据中table中不存在属性赋值
        List<ProcessTemplate> processTemplateList = page.getRecords();
        for(ProcessTemplate template: processTemplateList){
            template.setProcessTypeName(template.getName());
        }


        return page;
    }


    @Transactional
    @Override
    public void publish(Long id) {

        ProcessTemplate processTemplate = this.getById(id);
        processTemplate.setStatus(1);
        int update = processTemplateMapper.updateById(processTemplate);


        //优先发布在线流程设计
        if (!StringUtils.isEmpty(processTemplate.getProcessDefinitionPath())) {
            processService.deployByZip(processTemplate.getProcessDefinitionPath());
        }


    }
}




