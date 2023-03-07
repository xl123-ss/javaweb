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
public class Notice {

    private long noticeId;
    private long receiverId;
    private String content;
    private LocalDateTime createTime;


}
