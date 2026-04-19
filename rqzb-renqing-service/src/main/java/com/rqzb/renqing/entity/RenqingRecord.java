package com.rqzb.renqing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("renqing_records")
public class RenqingRecord {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String type;

    private String name;

    private String relationship;

    private String relationshipNote;

    private String occasion;

    private Integer amount;

    private LocalDateTime recordDate;

    private String note;

    private LocalDateTime createdAt;
}
