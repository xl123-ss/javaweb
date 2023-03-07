package com.xxx.controller;

import com.alibaba.fastjson.JSONArray;
import com.xxx.entity.Employee;
import com.xxx.entity.User;
import com.xxx.service.EmployeeService;
import com.xxx.service.UserService;
import com.xxx.vto.R;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@WebServlet("/getemployinfo")
public class GetEmployeeServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String eid = req.getParameter("eid");
        R r;
        try {
            EmployeeService employeeService = new EmployeeService();
            Employee employee = employeeService.selectByEmployeeId(Long.parseLong(eid));
            r = R.ok().data("employee",employee).data("url","https://c.53326.com/d/file/20220903/5yu0tv0hvja.jpg");
        }catch (Exception e){
            e.printStackTrace();
            r=R.error().data("message",e.getClass().getSimpleName()+"抛出了"+e.getMessage());
        }
        resp.getWriter().println(JSONArray.toJSONString(r));
    }
}
