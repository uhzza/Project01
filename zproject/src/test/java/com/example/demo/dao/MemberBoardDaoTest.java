package com.example.demo.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;


@SpringBootTest
public class MemberBoardDaoTest {
	@Autowired
	private MemberBoardDao dao;
	
	//@Test
	public void saveTest() {
		Map<String, Object> map = new HashMap<>();
		map.put("username", "spring");
		map.put("bno", 1000);
		assertEquals(1, dao.save(map));
	}
	
	@Test
	public void existsTest() {
		Map<String, Object> map = new HashMap<>();
		map.put("username", "spring");
		map.put("bno", 1000);
		assertEquals(true, dao.existsById(map));
		
		map.put("bno", 1234);
		assertEquals(false, dao.existsById(map));
	}
}
