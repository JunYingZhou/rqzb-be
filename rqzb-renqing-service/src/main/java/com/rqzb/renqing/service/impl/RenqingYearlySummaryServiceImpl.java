package com.rqzb.renqing.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rqzb.renqing.entity.RenqingYearlySummary;
import com.rqzb.renqing.mapper.RenqingYearlySummaryMapper;
import com.rqzb.renqing.service.RenqingYearlySummaryService;
import org.springframework.stereotype.Service;

@Service
public class RenqingYearlySummaryServiceImpl
        extends ServiceImpl<RenqingYearlySummaryMapper, RenqingYearlySummary>
        implements RenqingYearlySummaryService {
}
