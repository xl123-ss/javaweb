package com.xxx.mapper;

import com.xxx.entity.Node;

import java.util.List;

public interface NodeMapper {

    public List<Node> selectNodeByUserId(long userId);
}
