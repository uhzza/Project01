package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.*;
import lombok.*;

// 성공/실패시 응답 형식을 통일하기 위한 DTO
@Data
@AllArgsConstructor
public class RestResponse {
	@Schema(description="작업 메시지", example="OK|FAIL")
	private String message;
	@Schema(description="작업 결과 데이터")
	private Object result;
	@Schema(description="이동할 주소. 주소가 없을 경우 null")
	private String url;
}
