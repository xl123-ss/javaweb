package com.xxx.service;

import com.xxx.entity.Notice;
import com.xxx.mapper.DepartmentMapper;
import com.xxx.mapper.NoticeMapper;
import com.xxx.utils.MyBatisUtils;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.util.List;

public class NoticeService {

    public List<Notice> getNoticeList(long receiverId) {
        Object obj = MyBatisUtils.executeQueryMapper(sqlSession ->
                sqlSession.getMapper(NoticeMapper.class).selectByReceiverId(receiverId)
        );
        return (List<Notice>) obj;
    }

    public void insert(Notice notice){
        MyBatisUtils.executeUpdateMapper(sqlSession ->
                sqlSession.getMapper(NoticeMapper.class).insert(notice)
        );


    }
    @Test
    public void t01selectByReceiverId() {
        getNoticeList(3).forEach(System.out::println);
    }
    @Test
    public void t02insert(){
        Notice notice = Notice.builder()
                .receiverId(3)
                .createTime(LocalDateTime.now())
                .content("测试插入内容")
                .build();
        insert(notice);
        System.out.println(notice.getNoticeId());
    }
}
