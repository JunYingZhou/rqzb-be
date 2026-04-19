package com.rqzb.renqing.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rqzb.renqing.entity.GiftRecord;
import com.rqzb.renqing.mapper.GiftRecordMapper;
import com.rqzb.renqing.service.GiftRecordService;
import org.springframework.stereotype.Service;

@Service
public class GiftRecordServiceImpl extends ServiceImpl<GiftRecordMapper, GiftRecord> implements GiftRecordService {
}
