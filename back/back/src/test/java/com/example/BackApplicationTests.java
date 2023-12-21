package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.dao.MysqlDAO;
import com.example.dao.UserDAO;

@SpringBootTest
class BackApplicationTests {
	@Autowired
	MysqlDAO dao;
	@Autowired
	UserDAO udao;

	@Test
	void contextLoads() {
	}

	@Test
	void userRead() {
		udao.read("red");
	}
}
