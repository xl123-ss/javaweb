package com.xxx.mapper;

import com.xxx.entity.User;
import com.xxx.utils.MyBatisUtils;
import org.apache.ibatis.annotations.Select;

public interface UserMapper2 {
    @Select("select * from  sys_user   where username = #{name} ")
     public User selectByUserName(String username);
}
