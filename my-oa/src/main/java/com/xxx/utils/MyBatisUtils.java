package com.xxx.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Function;

public class MyBatisUtils {
    private static SqlSessionFactoryBuilder builder;
    private static SqlSessionFactory factory;

    /**
     * 初始化SqlSessionFactory
     */
    static {
        try {
            builder = new SqlSessionFactoryBuilder();
            InputStream inputStream = Resources.getResourceAsStream("mybtisConfig.xml");
            factory = builder.build(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取sqlSession
     */
    public static SqlSession getSession(boolean flag) {
        return factory.openSession(flag);
    }

    /**
     * 执行查询语句操作
     *
     * @param function 需要执行查询语句的代码块
     * @return 查询结果
     */
    public static Object executeQuery(Function<SqlSession, Object> function) {
        SqlSession session = getSession(true);//自动回滚
        return function.apply(session);
    }

    public static Object executeUpdate(Function<SqlSession, Object> function) {
        SqlSession session = getSession(false);//手动提交
        try {
            Object obj = function.apply(session);
            //手动提交
            session.commit();
            return obj;
        } catch (Exception e) {
            session.rollback();
            throw e;
        } finally {
            session.close();
        }
    }


    public static Object executeQueryMapper(Function<SqlSession, Object> function) {
        SqlSession session = getSession(true);//自动回滚
        return function.apply(session);
    }

    public static Object executeUpdateMapper(Function<SqlSession, Object> function) {
        SqlSession session = getSession(false);//手动提交
        try {
            Object obj = function.apply(session);
            //手动提交
            session.commit();
            return obj;
        } catch (Exception e) {
            session.rollback();
            throw e;
        } finally {
            session.close();
        }
    }


}