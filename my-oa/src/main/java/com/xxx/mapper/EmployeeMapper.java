package com.xxx.mapper;

import com.xxx.entity.Department;
import com.xxx.entity.Employee;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface EmployeeMapper {
    @Select("select * from adm_employee where employee_id = #{id}")
    public Employee selectByEmployeeId(long id);

    List<Employee> selectParams(Map params);

}
