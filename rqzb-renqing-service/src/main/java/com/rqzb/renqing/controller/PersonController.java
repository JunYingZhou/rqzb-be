package com.rqzb.renqing.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.rqzb.common.ApiResponse;
import com.rqzb.renqing.entity.Person;
import com.rqzb.renqing.service.PersonService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/renqing/persons")
public class PersonController extends BaseCrudController<Person> {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        super(personService);
        this.personService = personService;
    }

    @GetMapping("/name/{name}")
    public ApiResponse<List<Person>> listByName(@PathVariable String name) {
        return ApiResponse.ok(personService.list(Wrappers.<Person>lambdaQuery()
                .like(Person::getName, name)
                .orderByDesc(Person::getUpdatedAt)));
    }
}
