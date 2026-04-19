package com.rqzb.renqing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("gift_records")
public class GiftRecord {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long personId;

    private String eventType;

    private Integer amount;

    private String direction;

    private LocalDateTime eventDate;

    private String note;

    private String location;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
