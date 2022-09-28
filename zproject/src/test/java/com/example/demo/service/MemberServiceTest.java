package com.example.demo.service;

import java.time.*;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;

import com.example.demo.dto.*;

@SpringBootTest
public class MemberServiceTest {
	@Autowired
	private MemberService service;
	
	//@Transactional
	//@Test
	public void joinTest() {
		MemberDto.Join dto = MemberDto.Join.builder().username("SPRING").password("1234")
			.irum("홍길동").birthday(LocalDate.now()).email("hasaway@naver.com").build();
		service.join(dto);
	}
	
	@Test
	public void readTest() {
		MemberDto.Read dto = service.read("SUMMER11");
		System.out.println(dto);
	}
	
}
