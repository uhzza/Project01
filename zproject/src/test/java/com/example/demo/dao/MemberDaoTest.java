package com.example.demo.dao;

import java.util.*;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;

@SpringBootTest
public class MemberDaoTest {
	@Autowired
	private MemberDao dao;
	
	@Test
	public void aaa() {
		System.out.println(dao.findByCheckcodeIsNotNull());
	}
	
	//@Test
	public void deleteByUsernamesTest() {
		List<String> usernames = Arrays.asList("spring","summer","winter");
		dao.deleteByUsernames(usernames);
	}
}
