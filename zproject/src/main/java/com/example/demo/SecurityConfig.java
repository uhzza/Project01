package com.example.demo;

import javax.sql.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.*;
import org.springframework.security.config.annotation.authentication.builders.*;
import org.springframework.security.config.annotation.method.configuration.*;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.crypto.password.*;

import com.example.demo.security.*;

// 캠핑장 오너, 일반 회원
// 둘이 같은 경로로 로그인 /member/login. 같은 컨트롤러를 사용하고 
//		@Secured("ROLE_OWNER"), @Secured("ROLE_USER")

// 캠핑장 오너와 일반 회원의 스프링 시큐리티를 분리

// 관리자는 /admin/**, 식당주인인 /chef/**, 일반회원은 /member/**...
// 관리자는 /admin/**, 관리자가 아닌 경우(/admin이 아닌 경우)는 @Secured로 구별
@EnableWebSecurity
public class SecurityConfig {
	@Order(1)
	@Configuration
	public static class AdminSecurityConfing extends WebSecurityConfigurerAdapter {
		@Autowired
		private PasswordEncoder passwordEncoder;
		
		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.inMemoryAuthentication().withUser("system").password(passwordEncoder.encode("1234")).roles("ADMIN");
		}	
		
		// 파라미터 http는 스프링 시큐리티를 이용한 접근 통제 정보를 저장할 객체
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			// /admin/** 경로 중 접근 통제를 하지 않는 예외 경로를 먼저 설정
			http.authorizeRequests().antMatchers("/admin/login").permitAll();
			
			// /admin/**로 들어오는 요청에 대해 ADMIN 권한과 폼 로그인 설정
			http.requestMatchers().antMatchers("/admin/**").and().authorizeRequests().anyRequest().hasRole("ADMIN")
				.and().formLogin().loginPage("/admin/login").loginProcessingUrl("/admin/login")
				.and().logout().logoutUrl("/admin/logout").logoutSuccessUrl("/").invalidateHttpSession(true);
		}
	}
	
	// /admin/**을 제외한 나머지 경로에 대한 시큐리티 설정 : @PreAuthorize, @PostAuthorize, @Secured 어노테이션 기반으로 접근 통제
	@Order(2)
	@Configuration
	@EnableGlobalMethodSecurity(prePostEnabled=true, securedEnabled=true)
	public static class MemberSecurityConfig extends WebSecurityConfigurerAdapter {
		@Autowired
		private DataSource dataSource;
		@Autowired
		private MemberLoginSuccessHandler memberLoginSuccessHandler;
		@Autowired
		private MemberLoginFailureHandler memberLoginFailureHandler;
		@Autowired
		private MemberAccessDeniedHandler accessDeniedHandler;
		

		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.jdbcAuthentication().dataSource(dataSource)
				.usersByUsernameQuery("select username, password, enabled from member where username=? and rownum<=1")
				.authoritiesByUsernameQuery("select username, role from member where username=? and rownum<=1");
		}
		
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.csrf().disable();
			http.exceptionHandling().accessDeniedHandler(accessDeniedHandler)
				.and().formLogin().loginPage("/member/login").loginProcessingUrl("/member/login").successHandler(memberLoginSuccessHandler).failureHandler(memberLoginFailureHandler)
				.and().logout().logoutUrl("/member/logout").logoutSuccessUrl("/").invalidateHttpSession(true);
		}
	}
}
