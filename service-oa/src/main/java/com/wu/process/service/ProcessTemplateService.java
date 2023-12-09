package com.wu.process.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wu.model.process.ProcessTemplate;

/**
* @author 起选
* @description 针对表【oa_process_template(审批模板)】的数据库操作Service
* @createDate 2023-05-30 20:31:23
*/
public interface ProcessTemplateService extends IService<ProcessTemplate> {

    IPage<ProcessTemplate> selectPage(Page<ProcessTemplate> pageParam);

    void publish(Long id);
}
