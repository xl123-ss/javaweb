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
public class LeaveForm {
    private long formId;
    private long employeeId;
    private Integer formType;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String reason;
    private LocalDateTime createTime;
    private String state;
}
