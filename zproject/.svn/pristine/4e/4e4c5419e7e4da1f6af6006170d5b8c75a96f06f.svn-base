package com.example.demo.controller;

import java.security.*;
import java.util.*;

import javax.validation.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.*;
import org.springframework.validation.*;
import org.springframework.validation.annotation.*;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.*;
import com.example.demo.service.*;

@Validated
@RestController
public class CommentController {
	@Autowired
	private CommentService service;
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping(value="/comments/new", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RestResponse> write(@ModelAttribute @Valid CommentDto.Write dto, BindingResult bindingResult, Principal principal) {
		List<CommentDto.Read> list = service.write(dto, principal.getName());
		return ResponseEntity.ok(new RestResponse("OK", list, null));
	}
	
	@PreAuthorize("isAuthenticated()")
	@DeleteMapping(value="/comments", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RestResponse> delete(@ModelAttribute @Valid CommentDto.Delete dto, BindingResult bindingResult, Principal principal) {
		List<CommentDto.Read> list = service.delete(dto, principal.getName());
		return ResponseEntity.ok(new RestResponse("OK", list, null));
	}
}
