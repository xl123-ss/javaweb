package com.xxx.service;

import com.xxx.entity.Department;
import com.xxx.entity.Employee;
import com.xxx.mapper.EmployeeMapper;
import com.xxx.utils.MyBatisUtils;
import org.apache.ibatis.annotations.Select;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeService {


    public Employee selectByEmployeeId(long employeeId) {
        Object obj = MyBatisUtils.executeQueryMapper(sqlSession ->
                sqlSession.getMapper(EmployeeMapper.class).selectByEmployeeId(employeeId)
        );
        return (Employee) obj;
    }

    /**
     * 根据条件，查询员工
     * @param params
     * @return
     */
    public List<Employee> selectParams(Map params) {
        Object obj = MyBatisUtils.executeQueryMapper(sqlSession ->
                sqlSession.getMapper(EmployeeMapper.class).selectParams(params)
        );
        return (List<Employee>) obj;
    }

    /**
     * 查找员工领导
     * @param
     * @return
     */
    public Employee selectLeaderByEmployeeId(long employeeId) {
        HashMap<Object, Object> map = new HashMap<>();
        map.put("employeeId", employeeId);
        return selectLeader(map);
    }

    public Employee selectLeader(Map params) {
        Employee employee = selectParams(params).get(0);
        Employee leader = null;
        //普通员工，为部门经理
        params.remove("employeeId");//把employeeId值去掉
        if (employee.getLevel() < 7) {

            params.put("level", 7);
            params.put("departmentId", employee.getDepartmentId());
            leader = selectParams(params).get(0);
        } else if (employee.getLevel() == 7) {
            params.remove("departmentId");
            params.put("level", 8);
            leader = selectParams(params).get(0);
        } else if (employee.getLevel() == 8) {
            //就是自己
            leader = employee;
        }
        return leader;
    }


    @Test
    public void t01() {
        HashMap<Object, Object> map = new HashMap<>();
        Employee employee = new Employee();
//        employee.getEmployeeId()
        map.put("employeeId", 1);
        Employee employee1 = selectParams(map).get(0);
        System.out.println(employee1);
    }
}
