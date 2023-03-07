package com.xxx.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProcessFlow {
    private long processId;
    private long formId;
    private long operatorId;
    private String action;
    private String result;
    private String reason;
    private LocalDateTime createTime;
    private LocalDateTime auditTime;
    private Integer orderNo;
    private String state;
    private Integer isLast;
}
