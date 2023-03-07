package com.xxx.controller;

import com.xxx.entity.LeaveForm;
import com.xxx.service.LeaveFormService;
import com.xxx.utils.DateUtils;
import com.xxx.vto.R;
import org.testng.annotations.Test;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

@WebServlet("/api/leave/*")
public class LeaveServlet extends HttpServlet {
    LeaveFormService leaveFormService = new LeaveFormService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        String methodName = uri.substring(uri.lastIndexOf("/") + 1);
        switch (methodName) {
            //创建表单
            case "create":
                this.create(req, resp);
                break;
            //查看表单集合
            case "list":
                list(req, resp);
                break;
            //审批
            case "audit":
                audit(req, resp);
                break;
            default:
                System.out.println("请求错误");
        }

    }

    private void list(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String eid = req.getParameter("eid");
        R r = null;
        try {
            List<Map> formList = leaveFormService.getLeaveFormList("process", Long.valueOf(eid));
            r = R.ok().data("list",formList);
        }catch (Exception e){
            e.printStackTrace();
            r = R.error().data("smg",e.getMessage());
        }
        resp.getWriter().println(r.toJsonStr());
    }

    private void create(HttpServletRequest req, HttpServletResponse resp) {
        //从前端获取数据
        String eid = req.getParameter("eid");
        String formType = req.getParameter("formType");
        String startTime = req.getParameter("startTime");
        String endTime = req.getParameter("endTime");
        String reason = req.getParameter("reason");


        //生成请假表单
        LeaveForm form = LeaveForm.builder()
                .employeeId(Long.parseLong(eid))
                .startTime(DateUtils.timestamToDatetime(Long.parseLong(startTime)))
                .endTime(DateUtils.timestamToDatetime(Long.parseLong(endTime)))
                .formType(Integer.valueOf(formType))
                .reason(reason)
                .createTime(LocalDateTime.now())
                .build();

        String res = null;
        try {
            leaveFormService.createLeaveForm(form);
            res = R.ok().toJsonStr();
        } catch (Exception e) {
            res = R.error().data("msg", e.getMessage()).toJsonStr();
        }
        try {
            resp.getWriter().println(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void audit(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long formId = Long.valueOf(req.getParameter("formId"));
        String result = req.getParameter("result");
        String reason = req.getParameter("reason");
        String eid = req.getParameter("eid");
        R r = null;
        try {
            leaveFormService.audit(formId, Long.parseLong(eid), result, reason);
            r = R.ok();
        } catch (Exception e) {
            e.printStackTrace();
            r = R.error().data("msg", e.getMessage());
        }
        resp.getWriter().println(r.toJsonStr());
    }

    @Test
    public void test01() {

//获取秒数
        Long second = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
        long l = LocalDateTime.now().plusHours(73).toEpochSecond(ZoneOffset.of("+8"));
        System.out.println(second);
        System.out.println(l);
    }

}
