package com.example.demo.controller;

import java.security.*;

import javax.validation.*;
import javax.validation.constraints.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.*;
import org.springframework.validation.*;
import org.springframework.validation.annotation.*;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.*;

import com.example.demo.controller.editor.*;
import com.example.demo.dto.*;
import com.example.demo.entity.*;
import com.example.demo.service.*;

import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.*;
import springfox.documentation.annotations.*;

@Validated
@RestController
public class BoardController {
	@Autowired
	private BoardService service;
	
	@InitBinder    
	public void init(WebDataBinder webDataBinder) {
		webDataBinder.registerCustomEditor(Boolean.class, "isGood", new MyBooleanPropertyEditor());    
	}
	
	@Operation(summary="1.글 읽기", description="게시판 글 읽기")
	@ApiResponses({
		@ApiResponse(code=200, response=RestResponse.class, message="result : Read DTO"), 
		@ApiResponse(code=409, response=RestResponse.class, message="오류 메시지") 
	})
	@ApiImplicitParam(name="bno", value="글번호", required=true, dataTypeClass=Integer.class)
	@GetMapping(value="/board", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RestResponse> read(@RequestParam @NotNull(message="글번호는 필수입력입니다") Integer bno, @ApiIgnore Principal principal) {
		String loginId = principal==null? null : principal.getName();
		BoardDto.Read dto = service.read(bno, loginId);
		return ResponseEntity.ok(new RestResponse("OK", dto, null));
	}
	
	@PreAuthorize("isAuthenticated()")
	@Operation(summary="2.글 변경", description="게시판 글 변경")
	@ApiImplicitParams({
		@ApiImplicitParam(name="bno", value="글번호", required=true, dataTypeClass=Integer.class),
		@ApiImplicitParam(name="title", value="제목", required=true, dataTypeClass=String.class),
		@ApiImplicitParam(name="content", value="내용", required=true, dataTypeClass=String.class),
	})
	@ApiResponses({
		@ApiResponse(code=200, response=RestResponse.class, message="result: 이동할 주소"), 
		@ApiResponse(code=409, response=RestResponse.class, message="오류 메시지") 
	})
	@PutMapping(value="/board", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RestResponse> update(@ModelAttribute @Valid BoardDto.Update dto, BindingResult bindingResult, @ApiIgnore Principal principal) {
		service.update(dto, principal.getName());
		return ResponseEntity.ok(new RestResponse("OK", dto.getBno() + "글을 변경했습니다", "/board/read?bno="+dto.getBno()));
	}
	
	@PreAuthorize("isAuthenticated()")
	@Operation(summary="3.글 삭제", description="게시판 글 삭제")
	@ApiResponses({ 
		@ApiResponse(code=200, response=RestResponse.class, message="result : 이동할 주소"), 
		@ApiResponse(code=409, response=RestResponse.class, message="오류 메시지") 
	})
	@ApiImplicitParam(name="bno", value="글번호", required=true, dataTypeClass=Integer.class)
	@DeleteMapping(value="/board", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RestResponse> delete(@RequestParam  @NotNull(message="글번호는 필수입력입니다") Integer bno, @ApiIgnore Principal principal) {
		service.delete(bno, "summer");
		return ResponseEntity.ok(new RestResponse("OK", bno +"번 글을 삭제했습니다", "/board/list"));
	}
	
	@Operation(summary="4.목록", description="페이지 번호에 해당하는 boardList, pageno, pagesize, totalcount 리턴")
	@ApiImplicitParams({
		@ApiImplicitParam(name="pageno", value="글번호(기본값은 1)", required=false, dataTypeClass=Integer.class),
		@ApiImplicitParam(name="writer", value="글쓴이", required=true, dataTypeClass=String.class)
	})
	@ApiResponses({
		@ApiResponse(code=200, response=RestResponse.class, message="result : Page DTO"), 
		@ApiResponse(code=409, response=RestResponse.class, message="오류") 
	})
	@GetMapping(value="/board/all", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RestResponse> list(@RequestParam(defaultValue="1") Integer pageno, @Pattern(regexp="^[A-Z0-9]{6,10}$", message="아이디는 대문자와 숫자 8~10자입니다")  String writer) {
		return ResponseEntity.ok(new RestResponse("OK", service.list(pageno, writer), null));
	}
	
	@PreAuthorize("isAuthenticated()")
	@Operation(summary="5.글 쓰기", description="게시판 글 작성")
	@ApiImplicitParams({
		@ApiImplicitParam(name="title", value="제목", required=true, dataTypeClass=String.class),
		@ApiImplicitParam(name="content", value="내용", required=true, dataTypeClass=String.class),
	})
	@ApiResponses({
		@ApiResponse(code=200, response=RestResponse.class, message="result : 글읽기 주소"), 
		@ApiResponse(code=409, response=RestResponse.class, message="오류 메시지") 
	})
	@PostMapping(value="/board/new", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RestResponse> write(@ModelAttribute @Valid BoardDto.Write dto, BindingResult bindingResult, @ApiIgnore Principal principal) {
		Board board = service.write(dto, principal.getName());
		return ResponseEntity.ok(new RestResponse("OK", board, "/board/read?bno=" + board.getBno()));
	}
	
	//@PreAuthorize("isAuthenticated()")
	@PatchMapping(value="/board", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RestResponse> goodOrBad(@ModelAttribute @Valid MemberBoardDto dto, BindingResult bindingResult, Principal principal) {
		Integer result = service.goodOrBad(dto, "spring");
		return ResponseEntity.ok(new RestResponse("OK", result, null));
	} 
}