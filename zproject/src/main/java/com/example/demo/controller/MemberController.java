package com.example.demo.controller;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.security.*;
import java.time.*;

import javax.servlet.http.*;
import javax.validation.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.*;
import org.springframework.security.core.*;
import org.springframework.security.web.authentication.logout.*;
import org.springframework.stereotype.*;
import org.springframework.validation.*;
import org.springframework.validation.annotation.*;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.*;

import com.example.demo.controller.editor.*;
import com.example.demo.dto.*;
import com.example.demo.entity.*;
import com.example.demo.service.*;

@Validated
@Controller
public class MemberController {
	@Autowired
	private MemberService service;
	@Value("c:/upload/profile/")
	private String profileFolder;
	
	@InitBinder
	public void init(WebDataBinder wdb) {
		wdb.registerCustomEditor(LocalDate.class, new MyDatePropertyEditor());
	}
	
	// 아이디 사용 여부
	@GetMapping(path="/member/check/username", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RestResponse> idCheck(@Valid MemberDto.IdCheck dto, BindingResult bindingResult) {
		if(service.idCheck(dto))
			return ResponseEntity.ok(new RestResponse("OK", "사용가능한 아이디입니다", null));
		return ResponseEntity.status(HttpStatus.CONFLICT).body(new RestResponse("Fail", "사용중인 아이디입니다", null));
	}
	
	// 이메일 사용 여부
	@GetMapping(path="/member/check/email", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RestResponse> emailAvailableCheck(@Valid MemberDto.EmailCheck dto, BindingResult bindingResult) {
		if(service.emailCheck(dto))
			return ResponseEntity.ok(new RestResponse("OK", "사용가능한 아이디입니다", null));
		return ResponseEntity.status(HttpStatus.CONFLICT).body(new RestResponse("Fail", "사용중인 아이디입니다", null));
	}
	
	// 회원 가입
	@PostMapping(path="/member/new", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RestResponse> join(@Valid MemberDto.Join dto, BindingResult bindingResult) {
		Member member = service.join(dto);
		return ResponseEntity.ok(new RestResponse("OK", member, "/member/login"));
	}
	
	// 아이디 찾기
	@GetMapping(path="/member/find/username", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RestResponse> findId(@Valid MemberDto.FindId dto, BindingResult bindingResult) {
		service.findId(dto);
		return ResponseEntity.ok(new RestResponse("OK","아이디를 이메일로 보냈습니다", null));
	}
	
	// 비밀번호 리셋
	@PatchMapping(path="/member/find/password", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RestResponse> resetPassword(@Valid MemberDto.ResetPassword dto, BindingResult bindingResult) {
		service.resetPassword(dto);
		return ResponseEntity.ok(new RestResponse("OK","아이디를 이메일로 보냈습니다", null));
	}

	// 비밀번호 변경
	@PatchMapping(path="/member/password", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RestResponse> changePassword(@Valid MemberDto.ChangePassword dto, BindingResult bindingResult, Principal principal) {
		service.changePassword(dto, principal.getName());
		return ResponseEntity.ok(new RestResponse("OK","아이디를 이메일로 보냈습니다", null));
	}
	
	// 내 정보 보기
	@PreAuthorize("isAuthenticated()")
	@GetMapping(path="/member", produces = MediaType.APPLICATION_JSON_VALUE) 
	public ResponseEntity<RestResponse> read(Principal principal) {
		return ResponseEntity.ok(new RestResponse("OK",service.read(principal.getName()), null));
	}
	
	// 내 정보 변경
	@PreAuthorize("isAuthenticated()")
	@PostMapping(path="/member", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RestResponse> update(@Valid MemberDto.Update dto, BindingResult bindingResult, Principal principal) {
		service.update(dto, principal.getName());
		return ResponseEntity.ok(new RestResponse("OK", "정보를 변경했습니다", "/member/read"));
	}
	
	// 회원 탈퇴 후 루트 페이지로 이동(회원 탈퇴 버튼이 내정보에 있다. 따라서 루트로 강제이동 시키자)
	@PreAuthorize("isAuthenticated()")
	@DeleteMapping(path="/member", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RestResponse> resign(SecurityContextLogoutHandler handler, HttpServletRequest req, HttpServletResponse res, Authentication authentication )  {
		service.resign(authentication.getName());
		handler.logout(req, res, authentication);
		return ResponseEntity.ok(new RestResponse("OK", "회원 정보를 삭제했습니다", "/member/list"));
	}
	
	private MediaType getMediaType(String fileName) {
		String extension = fileName.substring(fileName.lastIndexOf(".")).toUpperCase();
		MediaType type = MediaType.IMAGE_JPEG;
		if(extension.equals("PNG"))
			type = MediaType.IMAGE_PNG;
		else if(extension.equals("GIF"))
			type=MediaType.IMAGE_GIF;
		return type;
	}
	
	@GetMapping(value="/profile/{imagename}", produces=MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<byte[]> viewProfile(@PathVariable String imagename) {
		File file = new File(profileFolder + imagename);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(getMediaType(imagename));
		headers.add("Content-Disposition", "inline;filename="  + imagename);
		try {
			return ResponseEntity.ok().headers(headers).body(Files.readAllBytes(file.toPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
