package com.wu.process.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wu.auth.service.SysUserService;
import com.wu.model.process.Process;
import com.wu.model.process.ProcessRecord;
import com.wu.model.process.ProcessTemplate;
import com.wu.model.system.SysUser;
import com.wu.process.mapper.ProcessMapper;
import com.wu.process.service.ProcessRecordService;
import com.wu.process.service.ProcessService;
import com.wu.process.service.ProcessTemplateService;
import com.wu.security.hlper.LoginInfoHelper;
import com.wu.vo.process.ApprovalVo;
import com.wu.vo.process.ProcessFormVo;
import com.wu.vo.process.ProcessQueryVo;
import com.wu.vo.process.ProcessVo;
import com.wu.wechat.service.MessageService;
import io.netty.util.internal.StringUtil;
import org.activiti.bpmn.model.*;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;
import java.util.zip.ZipInputStream;

/**
 * @Classname ProcessServiceImpl
 * @Description
 * @Date 2023/6/4 15:12
 * @Created by cc
 */
@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class ProcessServiceImpl extends ServiceImpl<ProcessMapper, Process> implements ProcessService{
    @Autowired
    ProcessMapper processMapper;
    @Autowired
    RepositoryService repositoryService;

    @Lazy
    @Autowired
    private ProcessTemplateService processTemplateService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ProcessRecordService processRecordService;

    @Autowired
    private HistoryService historyService;
    @Autowired
    MessageService messageService;


        @Override
        public IPage<ProcessVo> selectPage (Page < ProcessVo > pageParam, ProcessQueryVo processQueryVo) {
            IPage<ProcessVo> page = processMapper.selectPage1(pageParam, processQueryVo);
            return page;
        }

    @Override
    public void deployByZip(String deployPath) {

            //通过部署地址找到zip文件，转为二进制文件
        //this.getClass().getClassLoader().getResourceAsStream(deployPath);
        /*
        String prePath=null;
        try {
             prePath= ResourceUtils.getURL("").getPath()+"/service-oa/src/main/java/processes/";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/
        InputStream inputStream  = this.getClass().getClassLoader().getResourceAsStream(deployPath);
        ZipInputStream zipInputStream
                = new ZipInputStream(inputStream);

        // 部署文件
        Deployment deployment = repositoryService.createDeployment()
                .addZipInputStream(zipInputStream).deploy();

         System.out.println("流程部署id：" + deployment.getId());
        System.out.println("流程部署名称：" + deployment.getName());


    }

    @Override
    public Process startUp(ProcessFormVo processFormVo) {



        SysUser sysUser = sysUserService.getById(LoginInfoHelper.getUserId());
        System.out.println("存在threadLocal中的用户id"+LoginInfoHelper.getUserId());
        ProcessTemplate processTemplate = processTemplateService.getById(processFormVo.getProcessTemplateId());

        //创建业务，保存到数据库中
        Process process = new Process();
        BeanUtils.copyProperties(processFormVo, process);
        String workNo = System.currentTimeMillis() + "";
        process.setProcessCode(workNo);
        process.setUserId(LoginInfoHelper.getUserId());
        process.setFormValues(processFormVo.getFormValues());//当申请人填写表单后提交，表单化作JSON字符串formValue，
        process.setTitle(sysUser.getName() + "发起" + processTemplate.getName() + "申请");
        process.setStatus(1);
        processMapper.insert(process);


        //运行流程,并且关联业务
        HashMap<String, Object> variablesMap = new HashMap<>();//参数
          //process.getFormValues(),是动态表单的数据拆分为json字符串。分为formData和formShowData
        JSONObject jsonObject = JSON.parseObject(process.getFormValues());
        System.out.println("JOSN将字符串反序列化成为Object：" + jsonObject);
        JSONObject formData = jsonObject.getJSONObject("formData");


        Map<String, Object> map = new HashMap<>();//formData的数据，转换为map
        for (Map.Entry<String, Object> entry : formData.entrySet()) {
            map.put(entry.getKey(), entry.getValue());
        }

        variablesMap.put("data", map);
        String businessKey = String.valueOf(process.getId());

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processTemplate.getProcessDefinitionKey()
                , businessKey, variablesMap);

        process.setProcessInstanceId(processInstance.getId());//关联业务


        //计算下一个审批人，可能有多个
        List<Task> taskList = this.getCurrentTaskList(String.valueOf(process.getId()));
        ArrayList<String> assigneeList = new ArrayList<>();
        for(Task task:taskList){
            SysUser user = sysUserService.getOne(
                    new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, task.getAssignee()));
          assigneeList.add(user.getName());
          //未完待续
            //推送消息给下一个审批人
            messageService.pushPendingMessage(process.getId(), sysUser.getId(), task.getId());
        }

        //记录操作行为
        processRecordService.record(process.getId(), 1, "发起申请");
      processMapper.updateById(process);


        return process;
    }

    /**
     * 查询待处理的任务，待处理的任务根据用户名查询，查询一个人有什么待处理任务。
     * 查出来为Task集合，而前端需要ProcessVo集合，因此先Task-》process—》processVo
     * @param processPageParam
     * @return
     */
    @Override
    public IPage<ProcessVo> findPending(Page<Process> processPageParam) {

        System.out.println("用户名; "+LoginInfoHelper.getUsername());
       //1.根据用户名查询其任务
        TaskQuery query = taskService.createTaskQuery()
                .taskAssignee(LoginInfoHelper.getUsername())
                .orderByTaskCreateTime()
                .desc();

         long size = processPageParam.getSize();
        long currentStart=(processPageParam.getCurrent()-1)*processPageParam.getSize();
                     //对于TaskService的分页查询，（当前条数开始，最大限制数）
        List<Task> taskList = query.listPage((int)currentStart,(int)size);
        System.out.println("待处理任务Task：  "+taskList.toString());

        ArrayList<ProcessVo> processVoList = new ArrayList<>();
        long count = query.count();//任务总数
        //2.转换类型Task-》process->processVo
        for(Task item: taskList){

            //直接从数据中获得业务process
            //但没有processid（业务id），但是有流程id可以找到流程实例进而找到关联的业务
               //找流程实例
            String processInstanceId = item.getProcessInstanceId();
            ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId);
            ProcessInstance processInstance = processInstanceQuery.singleResult();
            if(processInstance==null) continue;
               //找业务
            Process process = this.getById(processInstance.getBusinessKey());
            if(process==null ) continue;

            ProcessVo processVo = new ProcessVo();
            BeanUtils.copyProperties(process,processVo );
            processVo.setTaskId(item.getId());
            processVoList.add(processVo);

        }


        IPage<ProcessVo> processVoPage = new Page<>(processPageParam.getCurrent(), size, count);
        processVoPage.setRecords(processVoList);
        System.out.println("待处理任务：   "+processVoPage.getRecords());

        return processVoPage;
    }

    /**
     * 显示审批内容的详情
     * @param id
     * @return
     */
    @Override
    public Map<String, Object> show(Long id) {
        //前端需要流程，流程记录，模板

        Process process = this.getById(id);
        //通过流程id得到，流程记录
        List<ProcessRecord> processRecordList = processRecordService.list(new LambdaQueryWrapper<ProcessRecord>()
                .eq(ProcessRecord::getProcessId, id));

        ProcessTemplate processTemplate = processTemplateService.getById(process.getProcessTemplateId());
        HashMap<String, Object> map = new HashMap<>();
        map.put("process", process);
        map.put("processRecordList", processRecordList);
        map.put("processTemplate", processTemplate);


        //对于审批内容是否可以审批，通过一个审批变量判断
        boolean isApprove = false;
        List<Task> taskList = this.getCurrentTaskList(process.getProcessInstanceId());
        if(!CollectionUtils.isEmpty(taskList)){
        for(Task task:taskList) {

        if(task.getAssignee().equals(LoginInfoHelper.getUsername())){
            isApprove=true;
        }
        }
        }
            map.put("isApprove", isApprove);
        return map;
    }

    /**
     * 对于申请内容的审批，通过status判断是否同意审批内容
     * 过程：1为同意，记录审批结果到数据库，更新流程，传送审批内容到下一位审批者手中
     * 结束：通过是否还有下一位审批人和status判断整个审批是否结束（结果为：同意结束还是不同意结束）
     * @param approvalVo
     */
    @Override
    public void approve(ApprovalVo approvalVo) {
        //通过approve类的status判断是否通过审批，来推进审批任务的完成或结束
         Map<String, Object> variables1 = taskService.getVariables(approvalVo.getTaskId());

        for( Map.Entry<String,Object> entry:variables1.entrySet()){

                System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }

        if(approvalVo.getStatus()==1) {//同意
            //已通过
            Map<String, Object> variables = new HashMap<String, Object>();
            taskService.complete(approvalVo.getTaskId(), variables);
        }else {
            //驳回
            this.endTask(approvalVo.getTaskId());
        }


        //记录本次审批的结果
        String description = approvalVo.getStatus().intValue() == 1 ? "已通过" : "驳回";

        processRecordService.record(approvalVo.getProcessId(), approvalVo.getStatus(), description);


        Process process = this.getById(approvalVo.getProcessId());
        //推送给下一个审批人并且更新流程
        List<Task> currentTaskList = getCurrentTaskList(process.getProcessInstanceId());
        if(!CollectionUtils.isEmpty(currentTaskList)){
            List<String> assigneeList = new ArrayList<>();

            for (Task task:currentTaskList){
                String assignee = task.getAssignee();
                SysUser sysUser = sysUserService.getOne(new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getUsername, assignee));
                assigneeList.add(sysUser.getName());
                     //推送消息给下一个审批人

                process.setDescription("等待" + StringUtils.join(assigneeList.toArray(), ",") + "审批");
                process.setStatus(1);

            }
        }else {
            if(approvalVo.getStatus().intValue() == 1) {
                process.setDescription("审批完成（同意）");
                process.setStatus(2);
            } else {
                process.setDescription("审批完成（拒绝）");
                process.setStatus(-1);
            }
        }

        //推送消息给申请人
        messageService.pushProcessedMessage(process.getId(), process.getUserId(), approvalVo.getStatus());

        this.updateById(process);

    }

    @Override
    public IPage<ProcessVo> findProcessed(Page<Process> pageParam) {
        HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(LoginInfoHelper.getUsername())
                .finished()
                .orderByTaskCreateTime()
                .desc();
        long totalCount = historicTaskInstanceQuery.count();
         long  currentMany=(pageParam.getCurrent()-1)*pageParam.getSize();

        List<HistoricTaskInstance> historicTaskInstanceList = historicTaskInstanceQuery.listPage((int) currentMany, (int) pageParam.getSize());
        System.out.println("已处理任务分页："+historicTaskInstanceList.toString());
        ArrayList<ProcessVo> processArrayList = new ArrayList<>();
              if(!CollectionUtils.isEmpty(historicTaskInstanceList)){

                  for(HistoricTaskInstance historicTaskInstance: historicTaskInstanceList){
                      String processInstanceId = historicTaskInstance.getProcessInstanceId();
                      Process process = this.getOne(new LambdaQueryWrapper<Process>()
                              .eq(Process::getProcessInstanceId, processInstanceId));
                      ProcessVo processVo = new ProcessVo();
                      BeanUtils.copyProperties(process,processVo );
                      processVo.setTaskId("0");//为什么为0
                        processArrayList.add(processVo);
                  }

              }
         IPage<ProcessVo> page = new Page<ProcessVo>(pageParam.getCurrent(), pageParam.getSize(), totalCount);
        page.setRecords(processArrayList);
        return page;
    }

    @Override
    public IPage<ProcessVo> findStarted(Page<ProcessVo> pageParam) {
        ProcessQueryVo processQueryVo = new ProcessQueryVo();
        processQueryVo.setUserId(LoginInfoHelper.getUserId());
        System.out.println("id:   "+processQueryVo.getUserId());
        IPage<ProcessVo> page = processMapper.selectPage1(pageParam, processQueryVo);
        for (ProcessVo item : page.getRecords()) {
            item.setTaskId("0");
        }
        return page;
    }

    /**
     * 申请被拒绝
     * 在activiti中拒绝没有直接处理方法，我们对这个流程实例采取的方法是
     * 通过改变实例流向，直接将该节点通向下一个节点流向直接断掉，然后通向事件结束节点
     * @param taskId
     */
    private void endTask(String taskId) {
        //首先获得全部节点模型用来获取流程节点
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());//通过流程定义id

        //获取节点可以用定义id或者定义key
        //结束节点
        //EndEvent->Event-> FlowNode->FlowElement-(继承关系)
        List<EndEvent> endEventFlowElementsOfType = bpmnModel.getMainProcess().findFlowElementsOfType(EndEvent.class);
        EndEvent endEvent = endEventFlowElementsOfType.get(0);
        FlowNode endFlowNode=(FlowNode)endEvent;

        //当前节点
        FlowElement  currentFlowElement = bpmnModel.getMainProcess().getFlowElement(task.getTaskDefinitionKey());
        FlowNode currentFlowNode=(FlowNode) currentFlowElement;


        // ?为什么要改变类型
        // 因为这个类型才有获取流向这个方法，其他类型没有gg。 currentFlowNode.getOutgoingFlows();这个方法，


        //当前节点流向
        List<SequenceFlow> outgoingFlows = currentFlowNode.getOutgoingFlows();
        //  清理活动方向
        currentFlowNode.getOutgoingFlows().clear();
        //建立新方向
        SequenceFlow newSequenceFlow = new SequenceFlow();
        newSequenceFlow.setSourceFlowElement(currentFlowNode);
        newSequenceFlow.setTargetFlowElement(endFlowNode);
        List newSequenceFlowList = new ArrayList<>();
        newSequenceFlowList.add(newSequenceFlow);

        //  当前节点指向新的方向
        currentFlowNode.setOutgoingFlows(newSequenceFlowList);

        //  完成当前任务
        taskService.complete(task.getId());

    }

    /**
     * 获取当前任务列表
     * @param processInstanceId
     * @return
     */
    private List<Task> getCurrentTaskList(String processInstanceId) {
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
        return tasks;

    }


}