package com.example.demo.entity;

import java.time.*;

import lombok.*;

@Getter
@Builder
@ToString
public class Comment {
	private Integer cno;
	private String content;
	private String writer;
	private LocalDateTime writeTime;
	private Integer bno;
	
	public Comment addWriter(String loginId) {
		this.writer = loginId;
		return this;
	}
}
