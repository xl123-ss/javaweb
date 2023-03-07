package com.xxx.service;

import com.xxx.entity.User;
import com.xxx.mapper.UserMapper;
import com.xxx.mapper.UserMapper2;
import com.xxx.utils.Md5Utils;
import com.xxx.utils.MyBatisUtils;
import org.testng.annotations.Test;

public class UserService {



    public User login(String username, String password){

        User user = (User) MyBatisUtils.executeQueryMapper(sqlSession ->
                sqlSession.getMapper(UserMapper2.class).selectByUserName(username)
        );

        if (user==null){
            throw new RuntimeException("用户名不存在");
        }
        //加密（MD5）对password 进行md5 加salt 加密得到密文
        String md5Password= Md5Utils.md5Digest(password,user.getSalt());
        if (!md5Password.equals(user.getPassword()))
            throw new RuntimeException("密码错误");
        return user;

    }

    @Test
    public void t01(){
//        login("m8","test");
        String s = Md5Utils.md5Digest("123456", 111);
        System.out.println(s);
    }
}
