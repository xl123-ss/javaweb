package com.xxx.mapper;

import com.xxx.entity.Node;
import com.xxx.entity.Notice;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface NoticeMapper {
    Integer insert(Notice notice);
    @Select("select * from sys_notice where receiver_id = #{receiverId} order by create_time desc limit 0,30")
    List<Notice> selectByReceiverId(long receiverId);


}
