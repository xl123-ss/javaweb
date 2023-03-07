package com.xxx.service;

import com.xxx.entity.Department;
import com.xxx.entity.Employee;
import com.xxx.mapper.DepartmentMapper;
import com.xxx.mapper.EmployeeMapper;
import com.xxx.utils.MyBatisUtils;
import org.testng.annotations.Test;

public class DepartmentService {
    public Department selectById(long id){
        Object obj = MyBatisUtils.executeQueryMapper(sqlSession ->
                sqlSession.getMapper(DepartmentMapper.class).selectById(id)
        );
        return (Department) obj;
    }

    @Test
    public void t01(){
        Department obj = selectById(1);
        System.out.println(obj);
    }
}
