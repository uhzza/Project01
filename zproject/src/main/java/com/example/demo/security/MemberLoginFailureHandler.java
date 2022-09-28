package com.example.demo.security;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.*;
import org.springframework.stereotype.*;

import com.example.demo.dao.*;
import com.example.demo.entity.*;

@Component
public class MemberLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
	@Autowired
	private MemberDao dao;
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		String username = request.getParameter("username");
		Member member = dao.findById(username).orElse(null);
		HttpSession session = request.getSession();
		
		if(exception instanceof BadCredentialsException) {
			if(member!=null) {
				Integer loginFailCnt = member.getLoginFailCnt();
				if(loginFailCnt<5) {
					dao.update(Member.builder().username(username).loginFailCnt(loginFailCnt+1).build());
					session.setAttribute("msg", (loginFailCnt+1) + "회 로그인에 실패했습니다");
				}
			} else 
				session.setAttribute("msg", "로그인에 실패했습니다");
		} else if(exception instanceof DisabledException) {
			session.setAttribute("msg", "블록된 계정입니다. 관리자에게 문의하세요");
		}
		
		new DefaultRedirectStrategy().sendRedirect(request, response, "/member/login");
	}
}






