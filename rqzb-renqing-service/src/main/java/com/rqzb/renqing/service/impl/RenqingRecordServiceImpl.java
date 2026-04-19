package com.rqzb.renqing.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rqzb.renqing.entity.RenqingRecord;
import com.rqzb.renqing.mapper.RenqingRecordMapper;
import com.rqzb.renqing.service.RenqingRecordService;
import org.springframework.stereotype.Service;

@Service
public class RenqingRecordServiceImpl extends ServiceImpl<RenqingRecordMapper, RenqingRecord>
        implements RenqingRecordService {
}
