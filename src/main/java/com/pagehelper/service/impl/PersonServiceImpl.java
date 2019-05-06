package com.pagehelper.service.impl;

import com.github.pagehelper.PageHelper;
import com.pagehelper.domain.mapper.PersonMapper;
import com.pagehelper.domain.model.Person;
import com.pagehelper.service.PersonService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


@Service(value = "personService")
public class PersonServiceImpl implements PersonService {

    @Resource
    private PersonMapper personMapper;

    @Override
    public List<Person> findAll() {
        return personMapper.findAll();
    }

    @Override
    public List<Person> findByPage(int pageNo, int pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        return personMapper.findByPage();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insert(Person person) {
        personMapper.insert(person);
    }


}
