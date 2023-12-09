package com.wu.process.contoller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wu.model.process.Process;
import com.wu.model.process.ProcessTemplate;
import com.wu.process.service.ProcessService;
import com.wu.process.service.ProcessTemplateService;
import com.wu.process.service.ProcessTypeService;
import com.wu.result.Result;
import com.wu.vo.process.ApprovalVo;
import com.wu.vo.process.ProcessFormVo;
import com.wu.vo.process.ProcessQueryVo;
import com.wu.vo.process.ProcessVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

/**
 * @Classname ProcessController
 * @Description
 * @Date 2023/6/4 14:54
 * @Created by cc
 */


 //@CrossOrigin(allowCredentials = "true") //跨域
@Api(tags="审批流管理")
@RestController
@RequestMapping("admin/process")
@SuppressWarnings({"unchecked", "rawtypes"})
//@SuppressWarnings({"unchecked", "rawtypes"})

public class ProcessController {
    @Autowired
    ProcessService processService;
    @Autowired
    ProcessTypeService processTypeService;
    @Autowired
    ProcessTemplateService processTemplateService;



    @ApiOperation(value = "待处理")
    @GetMapping("/findPending/{page}/{limit}")
    public Result findPending(@PathVariable Long page,@PathVariable Long limit){
        Page<Process> processPageParam = new Page<>(page,limit);

        IPage<ProcessVo> pending = processService.findPending(processPageParam);

        return Result.ok(pending );


    }
            
    @ApiOperation(value = "获取分页列表")
    @GetMapping("{page}/{limit}")
    public Result index(@PathVariable Long page
            , @PathVariable Long limit
    , @RequestBody ProcessQueryVo processQueryVo){

            Page<ProcessVo> pageParam = new Page<>(page, limit);
        IPage<ProcessVo> pageModel = processService.selectPage(pageParam,processQueryVo);
        return Result.ok(pageModel);

    }


    /**
     * 员工端使用流程申请时，页面出现的分类和模板
     * @return
     */
    @ApiOperation(value = "获取全部审批分类及模板")
    @GetMapping("findProcessType")
    public Result findProcessType() {
        return Result.ok(processTypeService.findProcessType());
    }



    @ApiOperation(value = "获取审批模板")
    @GetMapping("getProcessTemplate/{processTemplateId}")
    public Result get(@PathVariable Long processTemplateId) {
        ProcessTemplate processTemplate = processTemplateService.getById(processTemplateId);
        return Result.ok(processTemplate);
    }

    @ApiOperation("启动流程")
    @PostMapping("startUp")
    public Result start(@RequestBody ProcessFormVo processFormVo){

        processService.startUp(processFormVo);
    return  Result.ok();
    }

    @ApiOperation(value = "获取审批详情")
    @GetMapping("show/{id}")
    public Result show(@PathVariable Long id) {
        return Result.ok(processService.show(id));
    }

    @ApiOperation(value = "审批")
    @PostMapping("approve")
    public Result approve(@RequestBody ApprovalVo approvalVo) {
        processService.approve(approvalVo);
        return Result.ok();
    }


    @ApiOperation(value = "已处理")
    @GetMapping("/findProcessed/{page}/{limit}")
    public Result findProcessed(
            @PathVariable Long page,
             @PathVariable Long limit) {
        Page<Process> pageParam = new Page<>(page, limit);
        return Result.ok(processService.findProcessed(pageParam));
    }

    @ApiOperation(value = "已发起")
    @GetMapping("/findStarted/{page}/{limit}")
    public Result findStarted(
             @PathVariable Long page,
            @PathVariable Long limit) {
        Page<ProcessVo> pageParam = new Page<>(page, limit);
        return Result.ok(processService.findStarted(pageParam));
    }



}
