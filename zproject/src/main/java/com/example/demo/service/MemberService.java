package com.example.demo.service;

import java.io.*;
import java.time.*;
import java.time.temporal.*;
import java.util.*;

import org.apache.commons.lang3.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.*;
import org.springframework.web.multipart.*;

import com.example.demo.dao.*;
import com.example.demo.dto.*;
import com.example.demo.entity.*;
import com.example.demo.exception.*;
import com.example.demo.util.*;

@Service
public class MemberService {
	@Autowired
	private MemberDao memberDao;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private MailUtil mailUtil;
	@Value("c:/upload/profile/")
	private String profileFolder;
	@Value("http://localhost:8087/profile/")
	private String profilePath;
	
	// 매주 목요일 새벽 4시에 체크코드를 가지고 있는 멤버(가입신청만 하고 확인을 안했다)를 삭제
	// cron은 리눅스의 스케줄링 표현법이고 7자리(초 시 분 일 월 요일 년)인데 스프링만 년도를 제외한 6자리를 지
	@Scheduled(cron = "0 0 4 ? * THU")
	public void deleteMemberWithInvalidCheckcode() {
		List<String> usrnames = memberDao.findByCheckcodeIsNotNull();
		memberDao.deleteByUsernames(usrnames);
	}

	public Boolean idCheck(MemberDto.IdCheck dto) {
		return !memberDao.existsById(dto.getUsername());
	}
	
	public Boolean emailCheck(MemberDto.EmailCheck dto) {
		return !memberDao.existsById(dto.getEmail());
	}

	public Member join(MemberDto.Join dto) {
		Member member = dto.toEntity();
		MultipartFile profile = dto.getProfile();
	
		String profileName = "default.jpg";
		System.out.println(dto);
		System.out.println(profile);
		System.out.println(profile==null);
		System.out.println("====================================");
		if(profile!=null && profile.isEmpty()==false) {
			File file = new File(profileFolder, profile.getOriginalFilename());
			try {
				profile.transferTo(file);
				profileName = profile.getOriginalFilename();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}		
		}	
		String checkcode = RandomStringUtils.randomAlphanumeric(20);
		String encodedPassword = passwordEncoder.encode(member.getPassword());
		member.addJoinInfo(profileName, checkcode, encodedPassword);
		memberDao.save(member);
		mailUtil.sendJoinCheckMail("admin@zmall.com", member.getEmail(), checkcode);
		return member;
	}
	
	public void checkJoin(String checkcode) {
		Member member = memberDao.findByCheckcode(checkcode).orElseThrow(MemberNotFoundException::new);
		memberDao.update(Member.builder().username(member.getUsername()).enabled(true).checkcode("").build());
	}

	public void findId(MemberDto.FindId dto) {
		Member member = memberDao.findByEmail(dto.getEmail()).orElseThrow(MemberNotFoundException::new);
		mailUtil.sendFindIdMail("admin@icia.com", dto.getEmail(), member.getUsername());
	}

	public void resetPassword(MemberDto.ResetPassword dto) {
		memberDao.findById(dto.getUsername()).orElseThrow(MemberNotFoundException::new);	
		String password = RandomStringUtils.randomAlphanumeric(20);
		String encodedPassword = passwordEncoder.encode(password);
		memberDao.update(Member.builder().username(dto.getUsername()).password(encodedPassword).build());
		mailUtil.sendResetPasswordMail("admin@icia.com", dto.getEmail(), password);
	}
	
	public Integer changePassword(MemberDto.ChangePassword dto, String loginId) {
		String encodedPassword = memberDao.findById(loginId).orElseThrow(MemberNotFoundException::new).getPassword();
		if(passwordEncoder.matches(dto.getNewPassword(), encodedPassword)==false)
			throw new JobFailException("비밀번호를 변경할 수 없습니다");
		String newEncodedPassword = passwordEncoder.encode(dto.getNewPassword());
		return memberDao.update(Member.builder().username(loginId).password(newEncodedPassword).build());
	}
	
	public MemberDto.Read read(String loginId) {
		Member member = memberDao.findById(loginId).orElseThrow(MemberNotFoundException::new);
		MemberDto.Read dto = member.toRead();
		Long days = ChronoUnit.DAYS.between(dto.getJoinday(), LocalDate.now());
		dto.setDays(days);
		dto.setProfile(profilePath + dto.getProfile()); 
		return dto;
	}
	
	public Integer update(MemberDto.Update dto, String loginId) {
		Member member = memberDao.findById(loginId).orElseThrow(MemberNotFoundException::new);
		MultipartFile profile = dto.getProfile();
		if(profile!=null && profile.isEmpty()==false) {
			File oldProfile = new File(profilePath, member.getProfile());
			if(oldProfile.exists()) 
				oldProfile.delete();
			File newProfile = new File(profileFolder, profile.getOriginalFilename());
			try {
				profile.transferTo(newProfile);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}	
			return memberDao.update(Member.builder().username(loginId).profile(profile.getOriginalFilename()).email(dto.getEmail()).build());
		}
		return memberDao.update(Member.builder().username(loginId).email(dto.getEmail()).build());
	}
	
	public Integer resign(String loginId) {
		if(memberDao.existsById(loginId)==false)
			throw new MemberNotFoundException();
		return memberDao.deleteById(loginId);
	}
}
