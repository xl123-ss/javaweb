package com.xxx.service;

import com.xxx.entity.Employee;
import com.xxx.entity.Node;
import com.xxx.mapper.EmployeeMapper;
import com.xxx.mapper.NodeMapper;
import com.xxx.utils.MyBatisUtils;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class NodeService {
    public List<Node> selectNodeByUserId(long userId){
        Object obj = MyBatisUtils.executeQueryMapper(sqlSession ->
                sqlSession.getMapper(NodeMapper.class).selectNodeByUserId(userId)
        );


        return (List<Node>) obj;
    }



    @Test
    public void selectNodeByUserId(){
        List<Node> nodes = selectNodeByUserId(1);
        List<Map<String,Object>> treeList = new ArrayList<>();
        Map<String,Object> module = null;
        for (Node node : nodes) {
            if (node.getNodeType() == 1){
                //1代表是模块
                module = new LinkedHashMap<>();
                module.put(node.getNodeName(),node);
                module.put("children",new ArrayList<>());
                treeList.add(module);
            }else {
                //2代表是功能
                assert module!= null;
                List<Node> children = (List<Node>) module.get("children");
                children.add(node);
            }
        }

    }


}
