package com.example.demo.dto;

import java.time.*;

import javax.validation.constraints.*;

import com.example.demo.entity.*;
import com.fasterxml.jackson.annotation.*;

import lombok.*;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public class CommentDto {
	@Data
	public static class Read {
		private Integer cno;
		private String content;
		private String writer;
		@JsonFormat(pattern="yyyy-MM-dd hh:mm:ss")
		private LocalDateTime writeTime;
	}

	@Data
	@Builder
	public static class Write {
		@NotEmpty
		private String content;
		@NotNull
		private Integer bno;
		public Comment toEntity() {
			return Comment.builder().content(content).bno(bno).build();
		}
	}
	
	@Data
	public static class Delete {
		@NotNull
		private Integer cno;
		@NotNull
		private Integer bno;
	}
}