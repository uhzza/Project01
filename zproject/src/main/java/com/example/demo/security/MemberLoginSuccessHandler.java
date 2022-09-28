package com.example.demo.security;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.*;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.*;
import org.springframework.security.web.savedrequest.*;
import org.springframework.stereotype.*;

import com.example.demo.dao.*;
import com.example.demo.entity.*;

@Component
public class MemberLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	@Autowired
	private MemberDao memberDao;
	
	// 사용자가 가려던 목적지를 저장하는 객체
	private RequestCache cache = new HttpSessionRequestCache();
	// 리다이렉트 해주는 객체
	private RedirectStrategy rs = new DefaultRedirectStrategy();
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		// 로그인 성공하면 실패 횟수 초기화, 로그인 횟수 증가
		Member member = Member.builder().username(authentication.getName()).loginFailCnt(0).build();
		memberDao.update(member);
			
		if(request.getParameter("password").length()>=20) {
			HttpSession session = request.getSession();
			session.setAttribute("msg", "임시비밀번호 로그인");
			rs.sendRedirect(request, response, "/member/change_password");
		}
		
		SavedRequest req = cache.getRequest(request, response);
		if(req!=null)
			rs.sendRedirect(request, response, req.getRedirectUrl());
		else
			rs.sendRedirect(request, response, "/");
	}
}