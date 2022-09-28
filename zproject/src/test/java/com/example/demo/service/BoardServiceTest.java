package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.transaction.annotation.*;

import com.example.demo.dto.*;
import com.example.demo.entity.*;
import com.example.demo.exception.*;

@SpringBootTest
public class BoardServiceTest {
	@Autowired
	private BoardService service;
	
	@Transactional
	//@Test
	public void writeTest() {
		BoardDto.Write dto = BoardDto.Write.builder().title("aaa").content("bbb").build();
		Board board = service.write(dto, "spring");
		assertNotNull(board.getBno());
	}
	
	@Transactional
	//@Test
	public void readTest() {
		// 글을 읽었는 데 글이 없다
		Assertions.assertThrows(BoardNotFoundException.class, ()->service.read(1000, "spring"));
		// 글을 읽었는데 있다
		assertEquals(0, service.read(31, null).getReadCnt());
		// 글쓴이어서 조회수가 변경되지 않는다
		assertEquals(0, service.read(32, "spring").getReadCnt());
		// 로그인했고 글쓴이가 아니어서 조회수가 변경되었다
		assertEquals(1, service.read(33, "summer").getReadCnt());
	}
	
	//@Test
	public void listTest() {
		assertEquals(10, service.list(1, "spring").getPagesize());
	}
	
	@Transactional
	//@Test
	public void updateTest() {
		// 없는 글
		BoardDto.Update board1 = BoardDto.Update.builder().bno(1).title("hello").content("aaaa").build();
		// 있는 글
		BoardDto.Update board2 = BoardDto.Update.builder().bno(2).title("hello").content("aaaa").build();
		Assertions.assertThrows(BoardNotFoundException.class, ()->service.update(board1, "spring"));
		Assertions.assertThrows(JobFailException.class, ()->service.update(board2, "winter"));
		assertEquals(1, service.update(board2, "spring"));
	}

	@Transactional
	//@Test
	public void deleteTest() {
		// 없는 글 삭제
		Assertions.assertThrows(BoardNotFoundException.class, ()->service.delete(1, "spring"));
		// 글쓴이가 아님
		Assertions.assertThrows(JobFailException.class, ()->service.delete(2, "winter"));
		assertEquals(1, service.delete(2, "spring"));
	}
	
	@Transactional
	@Test
	public void goodOrBadTest() {
		MemberBoardDto dto1 = new MemberBoardDto(1234, true);
		Assertions.assertThrows(BoardNotFoundException.class, ()->service.goodOrBad(dto1, "spring"));
		
		// 좋아요 실행. 좋아요 개수는 1
		MemberBoardDto dto2 = new MemberBoardDto(21, true);
		assertEquals(1, service.goodOrBad(dto2, "spring"));
		
		// 이미 좋아요한 글을 다시 좋아요...좋아요 개수는 여전히 1
		assertEquals(1, service.goodOrBad(dto2, "spring"));
		
		// 이미 좋아요한 글을 싫어요. 불가능하고 싫어요 개수는 변화없이 0
		MemberBoardDto dto3 = new MemberBoardDto(21, false);
		assertEquals(1, service.goodOrBad(dto2, "spring"));
	}
}






