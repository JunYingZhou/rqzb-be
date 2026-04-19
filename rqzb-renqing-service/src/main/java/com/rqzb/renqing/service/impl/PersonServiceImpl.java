package com.rqzb.renqing.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rqzb.renqing.entity.Person;
import com.rqzb.renqing.mapper.PersonMapper;
import com.rqzb.renqing.service.PersonService;
import org.springframework.stereotype.Service;

@Service
public class PersonServiceImpl extends ServiceImpl<PersonMapper, Person> implements PersonService {
}
