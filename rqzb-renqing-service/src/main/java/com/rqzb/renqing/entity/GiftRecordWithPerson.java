package com.rqzb.renqing.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("v_gift_records_with_person")
public class GiftRecordWithPerson {

    @TableId
    private Long id;

    private Long personId;

    private String personName;

    private String personRelation;

    private String eventType;

    private Integer amount;

    private String direction;

    private LocalDateTime eventDate;

    private String note;

    private String location;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
