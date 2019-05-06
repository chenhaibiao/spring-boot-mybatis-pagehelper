package com.pagehelper.service;

import com.pagehelper.domain.model.Person;

import java.util.List;

public interface PersonService {

    /**
     * findAll
     * @return
     */
    List<Person> findAll();

    /**
     * 分页查询
     * @param pageNo 页号
     * @param pageSize 每页显示记录数
     * @return
     */
    List<Person> findByPage(int pageNo, int pageSize);

    /**
     * insert
     * @param person
     */
    void insert(Person person);
}
