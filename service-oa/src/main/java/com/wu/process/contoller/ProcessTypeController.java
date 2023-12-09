package com.wu.process.contoller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wu.model.process.ProcessType;
import com.wu.process.service.ProcessTemplateService;
import com.wu.process.service.ProcessTypeService;
import com.wu.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @Classname ProcessTypeContoller
 * @Description
 * @Date 2023/5/30 20:52
 * @Created by cc
 */


//@CrossOrigin //跨域
@Api(value = "审批类型")
@RestController
@RequestMapping("admin/process/processType")
public class ProcessTypeController {
 @Autowired
    private ProcessTypeService processTypeService;



 @ApiOperation("获取全部审批分类")
    @GetMapping("findAll")
    public Result findAll(){
        return Result.ok(processTypeService.list());
    }


@ApiOperation(value = "分页查询")
    @GetMapping("{page}/{limit}")
    public Result index(@PathVariable long page,@PathVariable long limit){
    Page<ProcessType> pageParam= new Page<ProcessType>(page, limit);
    IPage<ProcessType> pageModel = processTypeService.page(pageParam);
    return Result.ok(pageModel);
}

    @ApiOperation(value = "获取")
@GetMapping("get/{id}")
    public Result get(@PathVariable long id){

    ProcessType type = processTypeService.getById(id);
    return Result.ok(type);
}


    @ApiOperation(value = "增加")
@PostMapping("save")
    public Result save(@RequestBody ProcessType processType){
    boolean save = processTypeService.save(processType);
    return Result.ok();

}

    @ApiOperation(value = "删除")
@DeleteMapping("remove/{id}")
public Result del(@PathVariable long id){
    boolean b = processTypeService.removeById(id);

    return Result.ok();

}

    @ApiOperation(value = "修改")
@PutMapping("update")
public Result update(@RequestBody ProcessType processType){
     processTypeService.updateById(processType);

    return Result.ok();

}

}
