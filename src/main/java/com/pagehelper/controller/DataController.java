package com.pagehelper.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.pagehelper.domain.model.Person;
import com.pagehelper.service.PersonService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DataController {

    private static final Logger logger = LoggerFactory.getLogger(DataController.class);
    
    @Autowired
    PersonService personService;

    @RequestMapping("/save")
    public Person save(@RequestBody Person person) {
        personService.insert(person);
        return person;
    }

    @RequestMapping("/all")
    public List<Person> all() {
        List<Person> list = personService.findAll();
        return list;
    }

    @RequestMapping("/page")
    public Object page(@RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize) {
        logger.info("page: {}, {}", pageNo, pageSize);
        List<Person> pagePeople = personService.findByPage(pageNo, pageSize);
        System.out.println(JSON.toJSON(pagePeople));
        return new PageInfo<Person>(pagePeople);
    }

}