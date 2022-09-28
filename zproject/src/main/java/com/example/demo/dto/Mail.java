package com.example.demo.dto;

import lombok.*;
import lombok.experimental.*;

@Data
@AllArgsConstructor
@Builder
@Accessors(chain=true)
public class Mail {
	private String from;			// 보내는 메일
	private String to;				// 받는 메일
	private String subject;			// 제목
	private String text;			// 내용
}
