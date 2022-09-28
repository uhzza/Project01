package com.example.demo.dto;

import java.time.*;
import java.util.*;

import javax.validation.constraints.*;

import org.springframework.format.annotation.*;

import com.example.demo.entity.*;
import com.fasterxml.jackson.annotation.*;

import lombok.*;

// 입력용 DTO의 경우 입력값 검증을 수행(Java Validataion)
// 출력용 DTO의 경우 Swagger를 통해 필드의 역할을 설명하겠다

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public class BoardDto {
	@Data
	public static class ForList {
		private Integer bno;
		private String title;
		private String writer;
		@JsonFormat(pattern="yyyy-MM-dd")
		private LocalDateTime writeTime;
		private Integer readCnt;
		private Integer commentCnt;
	}
	
	@Data
	@AllArgsConstructor
	public static class Page {
		private Integer pageno;
		private Integer pagesize;
		private Integer totalcount;
		private Collection<ForList> boardList;
	}
	
	@Data
	public static class Read {
		private Integer bno;
		private String title;
		private String content;
		private String writer;
		@JsonFormat(pattern="yyyy-MM-dd hh:mm:ss")
		private LocalDateTime writeTime;
		private Integer readCnt;
		private Integer goodCnt;
		private Integer badCnt;
		private Integer commentCnt;
		private List<CommentDto.Read> comments;
	}
	
	@Data
	@Builder
	public static class Write {
		@NotEmpty(message="제목은 필수입력입니다")
		private String title;
		@NotEmpty(message="내용은 필수입력입니다")
		private String content;
		public Board toEntity() {
			return Board.builder().title(title).content(content).build();
		}
	}
	
	@Data
	@Builder
	public static class Update {
		@NotNull(message="글번호는 필수입력입니다")
		private Integer bno;
		@NotEmpty(message="제목은 필수입력입니다")
		private String title;
		@NotEmpty(message="내용은 필수입력입니다")
		private String content;
		public Board toEntity() {
			return Board.builder().title(title).content(content).bno(bno).build();
		}
	}
}