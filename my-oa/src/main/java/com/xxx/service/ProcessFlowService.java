package com.xxx.service;

import com.xxx.entity.ProcessFlow;
import com.xxx.mapper.ProcessFlowMapper;
import com.xxx.utils.MyBatisUtils;
import org.testng.annotations.Test;

import java.util.List;

public class ProcessFlowService {
    Integer insert(ProcessFlow admProcessFlow) {

        Object res = MyBatisUtils.executeUpdateMapper(sqlSession ->
                sqlSession.getMapper(ProcessFlowMapper.class).insert(admProcessFlow)
        );
        return (Integer) res;
    }

    public void update(ProcessFlow processFlow) {
        MyBatisUtils.executeUpdateMapper(sqlSession -> {
                    sqlSession.getMapper(ProcessFlowMapper.class).update(processFlow);
                    return null;
                }

        );
    }

    public List<ProcessFlow> selectByFormId(long formId) {
        Object res = MyBatisUtils.executeQueryMapper(sqlSession ->
                sqlSession.getMapper(ProcessFlowMapper.class).selectByFormId(formId)
        );
        return (List<ProcessFlow>) res;
    }

    @Test
    public void t01selectByFormId(){
        selectByFormId(1).forEach(System.out::println);
    }

    @Test
    public void t01update(){
        ProcessFlow flow = ProcessFlow.builder()
                .reason("测试aaaaaaa")
                .processId(1)
                .build();
        update(flow);
    }
}
