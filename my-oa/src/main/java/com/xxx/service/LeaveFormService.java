package com.xxx.service;

import com.xxx.entity.*;
import com.xxx.mapper.LeaveFormMapper;
import com.xxx.utils.MyBatisUtils;
import com.xxx.utils.ThreadPoolManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class LeaveFormService {

    EmployeeService employeeService = new EmployeeService();
    ProcessFlowService admProcessFlowService = new ProcessFlowService();


    /**
     * 创建请假表单
     *
     * @param leaveForm
     * @return
     */
    public LeaveForm createLeaveForm(LeaveForm leaveForm) {
        //找到员工本人
        Employee employee = employeeService.selectByEmployeeId(leaveForm.getEmployeeId());
        //找到上街领导
        Employee leader = employeeService.selectLeaderByEmployeeId(employee.getEmployeeId());
        int level = employee.getLevel();
        /**
         * 总经理是直接默认完成请见但的
         */
        if (level == 8) {
            leaveForm.setState("complete");
        } else {
            leaveForm.setState("processing");
        }
        //持久化数据
        Object res = MyBatisUtils.executeQueryMapper(sqlSession ->
                sqlSession.getMapper(LeaveFormMapper.class).insert(leaveForm)
        );
        //1.新增流程数据
        {
            ProcessFlow flow1 = ProcessFlow.builder()
                    .formId(leaveForm.getFormId())
                    .operatorId(employee.getEmployeeId())
                    .action("apply")
                    .createTime(LocalDateTime.now())
                    .state("complete")
                    .orderNo(1)
                    .isLast(0)
                    .build();
            admProcessFlowService.insert(flow1);
        }
        //2.分情况创建其余流程
        switch (level) {
            /**
             * 8级员工的，生成总经理的审批业务，系统通过执行
             */
            case 8: {

                ProcessFlow flow2 = ProcessFlow.builder()
                        .formId(leaveForm.getFormId())
                        .operatorId(employee.getEmployeeId())
                        .action("aduit")
                        .result("approved")
                        .reason("自动通过")
                        .createTime(LocalDateTime.now())
                        .auditTime(LocalDateTime.now())
                        .state("complete")
                        .orderNo(2)
                        .isLast(1)
                        .build();
                admProcessFlowService.insert(flow2);
                break;
            }
            //7级员工，仅生成总经理的审批任务
            case 7: {

                ProcessFlow flow2 = ProcessFlow.builder()
                        .formId(leaveForm.getFormId())
                        .operatorId(leader.getEmployeeId())
                        .action("aduit")
                        .createTime(LocalDateTime.now())
                        .state("process")
                        .orderNo(2)
                        .isLast(1)
                        .build();
                admProcessFlowService.insert(flow2);
                break;
            }
            //其他普通员工
            default: {
                //查找部门经理
                ProcessFlow flow2 = ProcessFlow.builder()
                        .formId(leaveForm.getFormId())
                        .operatorId(leader.getEmployeeId())
                        .action("aduit")
                        .createTime(LocalDateTime.now())
                        .state("process")
                        .orderNo(2)
                        .build();

                //调用工具类，求解请假时间
                LocalDateTime startTime = leaveForm.getStartTime();
                LocalDateTime endTime = leaveForm.getEndTime();
                Duration between = Duration.between(startTime, endTime);
                long hours = between.toHours();
                //超过72小时
                if (hours >= 72) {
                    //设置为不是最后一步
                    flow2.setIsLast(0);
                    admProcessFlowService.insert(flow2);
                    //找出到项目的leader(总经理)
                    Employee leader2 = employeeService.selectLeaderByEmployeeId(leader.getEmployeeId());
                    //生成第三条数据流，并插入数据库
                    ProcessFlow flow3 = ProcessFlow.builder()
                            .formId(leaveForm.getFormId())
                            .operatorId(leader2.getEmployeeId())
                            .action("aduit")
                            .createTime(LocalDateTime.now())
                            .state("ready")
                            .orderNo(3)
                            .isLast(1)
                            .build();
                    admProcessFlowService.insert(flow3);
                } else {
                    //小于72小时的,是最后一步，由部门经理审批
                    flow2.setIsLast(1);
                    admProcessFlowService.insert(flow2);
                }
            }
        }
        return leaveForm;
    }

    LeaveForm selectById(Long formId) {
        Object res = MyBatisUtils.executeQueryMapper(sqlSession ->
                sqlSession.getMapper(LeaveFormMapper.class).selectById(formId)
        );
        return (LeaveForm) res;
    }

    public void update(LeaveForm form) {
        MyBatisUtils.executeUpdateMapper(sqlSession -> {
                    sqlSession.getMapper(LeaveFormMapper.class).update(form);
                    return null;
                }
        );

    }

    /**
     * 获取指定任务状态及指定经办人对应的请假单列表
     * @param state
     * @param operatorId
     * @return
     */
    public List<Map> getLeaveFormList(String state, Long operatorId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("state", state);
        map.put("operatorId", operatorId);
        Object o = MyBatisUtils.executeQueryMapper(sqlSession ->
                sqlSession.getMapper(LeaveFormMapper.class).selectParams(map)
        );
        return (List<Map>) o;
    }


    /**
     * 审批意见
     * @param formId
     * @param operatorId
     * @param result
     * @param reason
     * @throws Exception
     */
    public void audit(Long formId, long operatorId, String result, String reason) throws Exception {
        //无论是同时，驳回，当前任务变更为complete
        List<ProcessFlow> flowList = admProcessFlowService.selectByFormId(formId);
        if (flowList.size() == 0) {
            throw new Exception("无效的审批流程");
        }
        /**
         * 所有的任务流程中，是这个operatorId操作的且是process状态
         */
        Stream<ProcessFlow> stream = flowList.stream().filter(p ->
                Objects.equals(p.getOperatorId(), operatorId) && "process".equals(p.getState()));
        List<ProcessFlow> processList = stream.toList();
        ProcessFlow process;
        if (processList.size() == 0) {
            throw new Exception("未找到处理理节点");
        } else {
            //处理待处理任务节点的一条数据
            process = processList.get(0);
            process.setState("complete");
            process.setReason(reason);
            process.setResult(result);
            process.setAuditTime(LocalDateTime.now());
            admProcessFlowService.update(process);
        }
        //找到相关表单
        LeaveForm form = selectById(formId);
        //如果当前节点是最后一个节点，代表流程结束
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String strResult = null;
        //发送申请人的通知
        Employee operator = employeeService.selectByEmployeeId(process.getOperatorId());
        Employee employee = employeeService.selectByEmployeeId(form.getEmployeeId());
        NoticeService noticeService = new NoticeService();
        if ("approved".equals(result)) {
            strResult = "批准";
        } else if ("refused".equals(result)) {
            strResult = "驳回";
        }else {
            strResult = "";
        }
        if (process.getIsLast() == 1) {
            form.setState(result);
            update(form);
            /**
             * 开始发送消息通知
             */
            String finalStrResult = strResult;
            ThreadPoolManager.getsInstance().execute(()->{
                //发送给申请人
                String noticel = String.format("您的请假申请[%s-%s]%s%s已%s,审批意见:%s,审批流程已结束",
                        dtf.format(form.getStartTime()),
                        dtf.format(form.getEndTime()),
                        operator.getTitle(),
                        operator.getName(),
                        finalStrResult,
                        reason
                );

                Notice build = Notice.builder()
                        .receiverId(form.getEmployeeId())
                        .content(noticel)
                        .createTime(LocalDateTime.now())
                        .build();
                noticeService.insert(build);
                //发送给审批人的通知
                String noticel2 = String.format("%s-%s提起请假申请[%s-%s]，您已%s,审批意见:%s,审批流程已结束",
                        employee.getTitle(),
                        employee.getName(),
                        dtf.format(form.getStartTime()),
                        dtf.format(form.getEndTime()),
                        finalStrResult,
                        reason);
                Notice build2 = Notice.builder()
                        .receiverId(operator.getEmployeeId())
                        .content(noticel2)
                        .createTime(LocalDateTime.now())
                        .build();
                noticeService.insert(build2);
            });

        } else {
            //没有结束
            List<ProcessFlow> readyList = flowList.stream().filter(p -> "ready".equals(p.getState())).toList();
            //如果当前任务不是最后一个节点，且审批通过，那么下个节点从ready变为process
            if ("approved".equals(result)) {
                ProcessFlow readyProcess = readyList.get(0);
                readyProcess.setState("process");
                readyProcess.setReason(reason);
                admProcessFlowService.update(readyProcess);

                /**
                 * 开始发送消息通知
                 */
                String finalStrResult1 = strResult;
                ThreadPoolManager.getsInstance().execute(()->{
                    //消息1,请假表单提交人，部门经理已经审批，交由上级继续审批
                    String noticel1 = String.format("您的请假申请[%s-%s]%s%s已%s,审批意见:%s,请继续等待上级审批",
                            dtf.format(form.getStartTime()),
                            dtf.format(form.getEndTime()),
                            operator.getTitle(),
                            operator.getName(),
                            finalStrResult1,
                            reason);
                    Notice build = Notice.builder()
                            .receiverId(form.getEmployeeId())
                            .content(noticel1)
                            .createTime(LocalDateTime.now())
                            .build();
                    noticeService.insert(build);
                    //消息2:通知部门经理（当前的经办人），员工的申请单已经批准，交由上级继续批准
                    String noticel2 = String.format("%s-%s提起请假申请[%s-%s]，您已批准,审批意见:%s,申请转至上级领导的继续审批",
                            employee.getTitle(),
                            employee.getName(),
                            dtf.format(form.getStartTime()),
                            dtf.format(form.getEndTime()),
                            reason);
                    Notice build2 = Notice.builder()
                            .receiverId(operator.getEmployeeId())
                            .content(noticel2)
                            .createTime(LocalDateTime.now())
                            .build();
                    noticeService.insert(build2);

                    Employee boss = employeeService.selectLeaderByEmployeeId(operatorId);
                    //消息3:通知总经理有新的审批任务
                    String notice3 = String.format("%s-%s提起请假申请[%s-%s],请尽快审批",
                            employee.getTitle(),
                            employee.getName(),
                            dtf.format(form.getStartTime()),
                            dtf.format(form.getEndTime()));
                    Notice build3 = Notice.builder()
                            .receiverId(boss.getEmployeeId())
                            .content(notice3)
                            .createTime(LocalDateTime.now())
                            .build();
                    noticeService.insert(build3);
                });


            } else if ("refused".equals(result)) {
                //如果当前不是最后一个节点且审批被驳回，那么后续所有的任务状态变更为cancel,将阿基状态变为refused
                for (ProcessFlow p : readyList) {
                    p.setState("cancel");
                    admProcessFlowService.update(p);
                }
                form.setState("refused");
                update(form);

                /**
                 * 开始发送消息通知
                 */
                ThreadPoolManager.getsInstance().execute(()->{
                    //消息1:通知申请人表单被驳回
                    //消息1,通知表单提交人，部门经理已经审批，交由上级继续审批
                    String noticel1 = String.format("您的请假申请[%s-%s]被%s%s驳回,审批意见:%s,审批流程已结束",
                            dtf.format(form.getStartTime()),
                            dtf.format(form.getEndTime()),
                            operator.getTitle(),
                            operator.getName(),
                            reason);
                    Notice build = Notice.builder()
                            .receiverId(form.getEmployeeId())
                            .content(noticel1)
                            .createTime(LocalDateTime.now())
                            .build();
                    noticeService.insert(build);
                    //消息2:通知部门经理（当前的经办人），员工的申请单已经批准，交由上级继续批准
                    String noticel2 = String.format("%s-%s提起请假申请[%s-%s]，您已驳回,审批意见:%s,审批流程已结束",
                            employee.getTitle(),
                            employee.getName(),
                            dtf.format(form.getStartTime()),
                            dtf.format(form.getEndTime()),
                            reason);
                    Notice build2 = Notice.builder()
                            .receiverId(operator.getEmployeeId())
                            .content(noticel2)
                            .createTime(LocalDateTime.now())
                            .build();
                    noticeService.insert(build2);
                });

            }
        }

    }


    @Test
    public void test01() {
        LeaveForm form = LeaveForm.builder()
                .employeeId(8)
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusHours(73))
                .formType(1)
                .reason("普通员工请假73小时")
                .createTime(LocalDateTime.now())
                .build();
        LeaveForm leaveForm = createLeaveForm(form);
        System.out.println(leaveForm);
    }

    @Test
    public void test02() {
        LeaveForm form = LeaveForm.builder()
                .employeeId(6)
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusHours(73))
                .formType(1)
                .reason("部门经理请假")
                .createTime(LocalDateTime.now())
                .build();
        LeaveForm leaveForm = createLeaveForm(form);
        System.out.println(leaveForm);
    }

    @Test
    public void test03createLeaveForm() {
        LeaveForm form = LeaveForm.builder()
                .employeeId(1)
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusHours(73))
                .formType(1)
                .reason("总经理请假")
                .createTime(LocalDateTime.now())
                .build();
        LeaveForm leaveForm = createLeaveForm(form);
        System.out.println(leaveForm);
    }

    @Test
    public void t04selectById() {


        System.out.println(selectById(1L));
    }

    @Test
    public void tt() {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        logger.debug("debug");
        logger.info("info");
        logger.warn("warn");
        logger.error("error");
    }

    @Test
    public void t05update() {
        LeaveForm form = LeaveForm.builder()
                .formId(1)
                .reason("测试修改aaaaa")
                .build();
        update(form);
    }

    @Test
    public void t06getLeaveFormList() {
        getLeaveFormList("1", 1L).forEach(System.out::println);
    }


}
