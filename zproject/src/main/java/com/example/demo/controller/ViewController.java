package com.example.demo.controller;

import javax.servlet.http.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.security.access.prepost.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;

import com.example.demo.service.*;

import springfox.documentation.annotations.*;

@Controller
@ApiIgnore
public class ViewController {
	@Autowired
	private MemberService service;
	
	@GetMapping({"/", "/board/list"})
	public ModelAndView list(HttpSession session) {
		ModelAndView mav = new ModelAndView("/board/list");
		if(session.getAttribute("msg")!=null) {
			mav.addObject("msg", session.getAttribute("msg"));
			session.removeAttribute("msg");
		}
		return mav;
	}
	
	@GetMapping("/board/read")
	public void read() {
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/board/write")
	public void write() {
	}
	
	@PreAuthorize("isAnonymous()")
	@GetMapping("/member/login")
	public ModelAndView login(HttpSession session) {
		ModelAndView mav = new ModelAndView("/member/login");
		if(session.getAttribute("msg")!=null) {
			mav.addObject("msg", session.getAttribute("msg"));
			session.removeAttribute("msg");
		}
		return mav;
	}
	
	@PreAuthorize("isAnonymous()")
	@GetMapping("/member/join")
	public void join() {
	}
	
	@PreAuthorize("isAnonymous()")
	@GetMapping("/member/find")
	public void find() {
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/member/read")
	public void memberRead() {
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/member/change_password")
	public ModelAndView changePassword(HttpSession session) {
		ModelAndView mav = new ModelAndView("/member/change_password");
		if(session.getAttribute("msg")!=null) {
			mav.addObject("msg", session.getAttribute("msg"));
			session.removeAttribute("msg");
		}
		return mav;
	}
	
	@PreAuthorize("isAnonymous()")
	@GetMapping("/member/check/join")
	public String checkJoin(String checkcode) {
		service.checkJoin(checkcode);
		return "/member/login";
	}
}
