package com.wu.process.contoller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wu.model.process.ProcessTemplate;
import com.wu.process.service.ProcessTemplateService;
import com.wu.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Classname ProcessTemplateController
 * @Description
 * @Date 2023/6/2 14:23
 * @Created by cc
 */

//@CrossOrigin //跨域
@Api(value = "审批模板管理",tags = "审批模板管理")
@RequestMapping("/admin/process/processTemplate")
@RestController
public class ProcessTemplateController {


    @Autowired
    private ProcessTemplateService processTemplateService;



    //@PreAuthorize("hasAuthority('bnt.processTemplate.publish')")
    @ApiOperation(value = "发布")
    @GetMapping("/publish/{id}")
    public Result publish(@PathVariable Long id) {
        processTemplateService.publish(id);
        return Result.ok();
    }

    @ApiOperation(value = "分页查询",tags = "分页查询")
    @GetMapping("{page}/{limit}")
    public Result find(@PathVariable Long page,@PathVariable Long limit){

        Page<ProcessTemplate> pageParam = new Page<>(page, limit);
         IPage<ProcessTemplate> p= processTemplateService.selectPage(pageParam);
    return Result.ok(p);
    }

    //@PreAuthorize("hasAuthority('bnt.processTemplate.list')")
    @ApiOperation(value = "获取")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        ProcessTemplate processTemplate = processTemplateService.getById(id);
        return Result.ok(processTemplate);
    }

    //@PreAuthorize("hasAuthority('bnt.processTemplate.templateSet')")
    @ApiOperation(value = "新增")
    @PostMapping("save")
    public Result save(@RequestBody ProcessTemplate processTemplate) {
        processTemplateService.save(processTemplate);
        return Result.ok();
    }

    //@PreAuthorize("hasAuthority('bnt.processTemplate.templateSet')")
    @ApiOperation(value = "修改")
    @PutMapping("update")
    public Result updateById(@RequestBody ProcessTemplate processTemplate) {
        processTemplateService.updateById(processTemplate);
        return Result.ok();
    }

    //@PreAuthorize("hasAuthority('bnt.processTemplate.remove')")
    @ApiOperation(value = "删除")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        processTemplateService.removeById(id);
        return Result.ok();
    }


    @ApiOperation("上传流程定义")
    @PostMapping("uploadProcessDefinition")
    public Result uploadProcessDefinition(@RequestParam("file") MultipartFile multipartFile) throws FileNotFoundException {

        System.out.println("multipart:   "+multipartFile.toString());
        //得到存储路径
        String absolutePath = new File(ResourceUtils.getURL("").getPath()).getAbsolutePath();

        //上传目录
        File tempFile = new File(absolutePath + "/service-oa/target/classes/processes/");
        if (!tempFile.exists()) tempFile.mkdirs();

        System.out.println("tempFile.getAbsolutePath():  " + tempFile.getAbsolutePath());
        System.out.println("multipartFile.getOriginalFilename():  "+multipartFile.getOriginalFilename());
        //创建空文件用于写入文件
        String finalFileName=multipartFile.getOriginalFilename();
        File finalFile = new File(tempFile.getAbsolutePath() + "/" + multipartFile.getOriginalFilename());

        try {

            //将文件复制到指定文件
            multipartFile.transferTo(finalFile);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.fail().message("上传失败");
        }


        Map<String, Object> map = new HashMap<>();
        //根据上传地址后续部署流程定义，文件名称为流程定义的默认key
        map.put("processDefinitionPath", "processes/" + finalFileName);
        map.put("processDefinitionKey", finalFileName.substring(0, finalFileName.lastIndexOf(".")));
        return Result.ok(map);
    }


    /*
    public static void main(String[] args) throws FileNotFoundException {
        //得到存储路径
        String absolutePath = new File(ResourceUtils.getURL("").getPath()).getAbsolutePath();
        String path = new File(ResourceUtils.getURL("classpath:" ).getPath()).getAbsolutePath();

        System.out.println("new File(ResourceUtils.getURL().getPath()).getAbsolutePath():     " + absolutePath);
        System.out.println(path);
        //上传目录
       // File tempFile = new File(absolutePath + "/process/");
       // if (!tempFile.exists()) tempFile.mkdirs();

        //System.out.println("tempFile.getAbsolutePath():  " + tempFile.getAbsolutePath());

    }
*/

}
