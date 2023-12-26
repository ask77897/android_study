package com.example.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dao.UserDAO;
import com.example.domain.QueryVO;
import com.example.domain.UserVO;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	UserDAO dao;
	
	//로그인
	@PostMapping("/login")
	public int login(@RequestBody UserVO vo) {
		int result=0;
		UserVO user=dao.read(vo.getUid());
		if(user != null) {
			if(!user.getUpass().equals(vo.getUpass())) {
				result=2;
			}else {
				result=1;
			}
		}
		return result;
	}
	
	//정보수정
	@PostMapping("/update")
	public void update(@RequestBody UserVO vo) {
		dao.update(vo);
	}
	
	//사용자읽기
	@GetMapping("/read/{uid}")
	public UserVO read(@PathVariable("uid") String uid) {
		return dao.read(uid);
	}
	
	//사용자등록
	@PostMapping("/insert")
	public void insert(@RequestBody UserVO vo) {
		dao.insert(vo);
	}
	
	//사용자목록
	@GetMapping("/list.json")
	public HashMap<String,Object> list(QueryVO vo) {
		HashMap<String, Object> map=new HashMap<>();
		map.put("list", dao.list(vo));
		map.put("total", dao.total(vo));
		return map;
	}
	
	@GetMapping("/list")
	public List<UserVO> list1(QueryVO vo){
		return dao.list(vo);
	}
}







