package com.xxx.mapper;

import com.xxx.entity.Department;
import org.apache.ibatis.annotations.Select;

public interface DepartmentMapper {

    @Select("select * from adm_department where department_id = #{id}")
    Department selectById(long id);


}
