package com.example.demo.dao;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.transaction.annotation.*;

import com.example.demo.dao.*;
import com.example.demo.entity.*;

@SpringBootTest
public class CommentDaoTest {
	@Autowired
	private CommentDao commentDao;
	
	//@Test
	public void diTest() {
		assertNotNull(commentDao);
	}
	
	//@Test
	public void saveTest() {
		assertEquals(1, commentDao.save(Comment.builder().bno(21).content("zzz").writer("winter").build()));
	}
	
	//@Test
	public void findByBnoTest() {
		assertEquals(1, commentDao.findByBno(21).size());
	}
	
	//@Test
	public void findWriterTest() {
		assertEquals("winter", commentDao.findWriterById(2).get());
	}
	
	@Transactional
	//@Test
	public void deleteByCnoTest() {
		assertEquals(1, commentDao.deleteByCno(2));
	}
	
	@Transactional
	@Test
	public void deleteByBnoTest() {
		assertNotEquals(0, commentDao.deleteByBno(1));
	}
}
















