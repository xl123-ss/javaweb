package com.xxx.mapper;

import com.xxx.entity.LeaveForm;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface LeaveFormMapper {
    int insert(LeaveForm leaveForm);

    void update(LeaveForm form);

    @Select("select * from adm_leave_form where form_id = #{formId}")
    LeaveForm selectById(Long formId);

    List<Map<String, Object>> selectParams(Map<String, Object> map);
}
