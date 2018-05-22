package com.pagehelper;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.pagehelper.domain.model.Person;
import com.pagehelper.service.PersonService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.alibaba.fastjson.JSON;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PersonMapperTests {

	private Logger logger = LoggerFactory.getLogger(PersonMapperTests.class);

	@Autowired
	private PersonService personService;
	
	// @Before
	public void testInsert() {
		Person person = new Person();
		person.setName("测试");
		person.setAddress("address");
		person.setAge(10);
		personService.insert(person);
		Assert.assertNotNull(person.getId());
		logger.debug("test insert person: {}", JSON.toJSONString(person));
	}
	
	@Test
	public void testFindAll() {
		List<Person> persons = personService.findAll();
		Assert.assertNotNull(persons);
		logger.debug("test find all: {}", JSON.toJSONString(persons));
	}

	@Test
	public void testFindByPage() {
		Page<Person> persons = personService.findByPage(1, 2);
		// 需要把Page包装成PageInfo对象才能序列化
		PageInfo<Person> pageInfo = new PageInfo<>(persons);
		Assert.assertNotNull(persons);
		logger.debug("test find by page: {}", JSON.toJSONString(pageInfo));
	}
}
