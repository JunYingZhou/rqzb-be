package com.rqzb.renqing.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("v_renqing_yearly_summary")
public class RenqingYearlySummary {

    @TableId
    private Integer recordYear;

    private BigDecimal incomeAmount;

    private BigDecimal expenseAmount;

    private BigDecimal balanceAmount;

    private Long recordCount;
}
