package com.rqzb.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dict_data")
public class SysDictData extends BaseEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Integer dictSort;

    private String dictLabel;

    private String dictValue;

    private String dictType;

    private String cssClass;

    private String listClass;

    private Integer isDefault;

    private Integer status;
}
