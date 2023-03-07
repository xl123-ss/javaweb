package com.xxx.mapper;

import com.xxx.entity.ProcessFlow;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ProcessFlowMapper {

    public Integer insert(ProcessFlow admProcessFlow);

    void update(ProcessFlow processFlow);
    @Select("select * from adm_process_flow where form_id =  #{formId}")
    List<ProcessFlow> selectByFormId(long formId);
}
