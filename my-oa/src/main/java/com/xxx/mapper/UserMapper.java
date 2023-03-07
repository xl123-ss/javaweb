package com.xxx.mapper;

import com.xxx.entity.User;
import com.xxx.utils.MyBatisUtils;

public class UserMapper {
    public User selectByUserName(String username){
        Object user = MyBatisUtils.executeQuery(sqlSession -> sqlSession.selectOne("com.xxx.mapper.UserMapper.findUserByName",username));
        return (User) user;
    }
}
