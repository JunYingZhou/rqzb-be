package com.rqzb.renqing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("persons")
public class Person {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String relation;

    private String phone;

    private String note;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
