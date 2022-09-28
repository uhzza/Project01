package com.example.demo.controller.advice;

import javax.validation.*;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.*;
import com.example.demo.exception.*;

@RestControllerAdvice
public class BoardControllerAdvice {
	@ExceptionHandler(BoardNotFoundException.class)
	public ResponseEntity<RestResponse> boardNotFoundExceptionHandler() {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(new RestResponse("Fail", "게시물을 찾을 수 없습니다", "/board/list"));
	}
	
	@ExceptionHandler(JobFailException.class)
	public ResponseEntity<RestResponse> jobFailExceptionHandler(JobFailException e) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(new RestResponse("FAIL", e.getMessage(), null));
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<RestResponse> constraintViolationException(ConstraintViolationException e) {
		String message = e.getConstraintViolations().stream().findFirst().get().getMessageTemplate();
		return ResponseEntity.status(HttpStatus.CONFLICT).body(new RestResponse("FAIL", message, null));
	}
}
