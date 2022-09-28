package com.example.demo.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.transaction.annotation.*;

import com.example.demo.entity.*;

@SpringBootTest
public class BoardDaoTest {
	@Autowired
	BoardDao boardDao;
	
	//@Test
	public void diTest() {
		assertNotNull(boardDao);
	}
	
	//@Test
	public void countTest() {
		// 페이징 : 페이지번호와 작성자
		// 작성자 없음. 전체 글의 개수
		assertEquals(18, boardDao.count(null));
		// spring이 작성한 글의 개수
		assertEquals(18, boardDao.count("spring"));
		// summer가 작성한 글의 개수 (0개)
		assertEquals(0, boardDao.count("summer"));
	}
	
	//@Test
	public void findAllTest() {
		// 전체글에서 첫번째 페이지 : 글의 개수 10개
		Map<String, Object> map = new HashMap<>();
		map.put("writer", null);
		map.put("start", 1);
		map.put("end", 10);
		assertEquals(10, boardDao.findAll(map).size());
		
		// summer가 작성한 글 중 첫번째 페이지 : 0개
		map = new HashMap<>();
		map.put("writer", "summer");
		map.put("start", 1);
		map.put("end", 10);
		assertEquals(0, boardDao.findAll(map).size());
	}
	
	//@Transactional
	@Test
	public void saveTest() {
		Board board = Board.builder().title("yyyy").content("bbb").writer("spring").build();
		assertEquals(1, boardDao.save(board));
	}
	
	@Transactional
	//@Test
	public void updateTest() {
		assertEquals(1, boardDao.update(Board.builder().bno(2).readCnt(1).build()));
		assertEquals(1, boardDao.update(Board.builder().bno(2).goodCnt(1).build()));
		assertEquals(1, boardDao.update(Board.builder().bno(2).commentCnt(15).build()));
		assertEquals(1, boardDao.update(Board.builder().bno(2).title("변경").content("변경").build()));
	}
	
	//@Test
	public void findByIdTest() {
		// findById의 결과 값은 Optional. 글이 없는 경우 get()을 하면 예외 발생
		Assertions.assertThrows(NoSuchElementException.class, 
			()->boardDao.findById(1).get());
		// 댓글이 있는 경우
		assertNotNull(boardDao.findById(2).get());
		
		// 댓글이 있는 글의 경우 comments의 크기는 0이 아니다
		assertNotEquals(0, boardDao.findById(2).get().getComments());
		
		// 댓글이 없는 글의 경우 comments의 크기는 0이다
		assertEquals(0, boardDao.findById(25).get().getComments().size());
	}
	
	//@Test
	public void findWriterTest() {
		Assertions.assertThrows(NoSuchElementException.class, 
				()->boardDao.findWriterById(1).get());
		assertEquals("spring", boardDao.findWriterById(2).get());
	}
	
	@Transactional
	//@Test
	public void deleteByIdTest() {
		// 글이 없어 삭제 실패
		assertEquals(0, boardDao.deleteById(1));
		// 삭제 성공
		assertEquals(1, boardDao.deleteById(2));
	}
}
