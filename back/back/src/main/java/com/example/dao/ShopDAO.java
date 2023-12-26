package com.example.dao;
import java.util.*;
import com.example.domain.*;

public interface ShopDAO {
	public List<HashMap<String, Object>> list(QueryVO vo);
	public int total();
	public void insert(ShopVO vo);
	public ShopVO read(int pid);
	public void update(ShopVO vo);
	public void updateImage(ShopVO vo);
}
