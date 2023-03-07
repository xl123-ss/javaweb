package com.xxx.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Employee {
    private long employeeId;
    private String name;
    private long departmentId;
    private String title;
    private Integer level;
}
