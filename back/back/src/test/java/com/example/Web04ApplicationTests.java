package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.dao.MysqlDAO;
import com.example.dao.UserDAO;
import com.example.domain.UserVO;

@SpringBootTest
class Web04ApplicationTests {
	@Autowired
	MysqlDAO dao;
	
	@Autowired
	UserDAO udao;
	
	@Test
	void contextLoads() {
		System.out.println("..........." + dao.now());
	}
	
//	@Test
//	void userList() {
//		udao.list();
//	}

//	@Test
//	void userInsert() {
//		UserVO vo=new UserVO();
//		vo.setUid("white");
//		vo.setUpass("pass");
//		vo.setUname("화이트");
//		vo.setAddress1("인천 서구 경서동");
//		vo.setAddress2("120번지");
//		vo.setPhone("010-1010-2020");
//		udao.insert(vo);
//	}
	
	@Test
	void userRead() {
		udao.read("sky");
	}
	
//	@Test
//	void userUpdate() {
//		UserVO vo=udao.read("sky");
//		//vo.setAddress1("서울 강남구 압구정동");
//		vo.setPhone("010-1234-5678");
//		udao.update(vo);
//	}
}












