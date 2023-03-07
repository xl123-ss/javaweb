package com.xxx.controller;

import com.xxx.entity.Department;
import com.xxx.entity.Employee;
import com.xxx.entity.Node;
import com.xxx.service.DepartmentService;
import com.xxx.service.EmployeeService;
import com.xxx.service.NodeService;
import com.xxx.vto.R;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

@WebServlet("/nodeInfo")
public class GetNodeInfoServlet extends HttpServlet {

    EmployeeService employeeService = new EmployeeService();
    DepartmentService departmentService = new DepartmentService();
    NodeService nodeService = new NodeService();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uid = req.getParameter("uid");
        String eid = req.getParameter("eid");
        Employee employee = employeeService.selectByEmployeeId(Long.parseLong(eid));
        Department department = departmentService.selectById(employee.getDepartmentId());
        //查阅菜单树
        List<Node> nodes = nodeService.selectNodeByUserId(Long.parseLong(uid));
        List<Map<String,Object>> treeList = new ArrayList<>();
        Map<String,Object> module = null;
        for (Node node : nodes) {
            if (node.getNodeType() == 1){
                //1代表是模块
                module = new LinkedHashMap<>();
                module.put("node",node);
                module.put("children",new ArrayList<>());
                treeList.add(module);
            }else {
                //2代表是功能
                assert module!= null;
                List<Node> children = (List<Node>) module.get("children");
                children.add(node);
            }
        }

        String json = R.ok().data("employee", employee).
                data("department", department).
                data("nodeList", treeList).toJsonStr();
        System.out.println(json);
        resp.getWriter().println(json);

    }
}
