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
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/login/*")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        String methodName = uri.substring(uri.lastIndexOf("/") + 1);
        System.out.println(methodName);
        switch (methodName){
            case "login":
                Login(req,resp);
                break;
            case "signup":
                break;
        }


    }

    private void Login(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        R r;
        try {
            UserService userService = new UserService();
            User user = userService.login(username,password);
            user.setSalt(null);
            user.setPassword(null);
            r = R.ok().data("user",user);
            req.getSession().setAttribute("name",user.getUsername());
        }catch (Exception e){
            e.printStackTrace();
            r=R.error().data("message",e.getClass().getSimpleName()+"抛出了"+e.getMessage());
        }
        resp.getWriter().println(JSONArray.toJSONString(r));
    }
}
