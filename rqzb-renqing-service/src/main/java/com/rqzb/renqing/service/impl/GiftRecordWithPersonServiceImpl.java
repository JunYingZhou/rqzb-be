package com.rqzb.renqing.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rqzb.renqing.entity.GiftRecordWithPerson;
import com.rqzb.renqing.mapper.GiftRecordWithPersonMapper;
import com.rqzb.renqing.service.GiftRecordWithPersonService;
import org.springframework.stereotype.Service;

@Service
public class GiftRecordWithPersonServiceImpl
        extends ServiceImpl<GiftRecordWithPersonMapper, GiftRecordWithPerson>
        implements GiftRecordWithPersonService {
}
