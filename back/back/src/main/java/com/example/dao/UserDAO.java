package com.example.dao;

import java.util.List;

import com.example.domain.QueryVO;
import com.example.domain.UserVO;

public interface UserDAO {
	public List<UserVO> list(QueryVO vo);
	public UserVO read(String uid);
	public void insert(UserVO vo);
	public void update(UserVO vo);
	public int total(QueryVO vo);
}
