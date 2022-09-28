package com.example.demo.entity;

import java.time.*;

import com.example.demo.dto.*;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member {
	private String username;
	private String password;
	private String irum;
	private String email;
	private LocalDate birthday;
	private LocalDate joinday;
	private String profile;
	private String role;
	private Integer loginFailCnt;
	private String checkcode;
	private Boolean enabled;
	private Integer buyCount;
	private Integer buyMoney;
	private Level levels;
	public MemberDto.Read toRead() {
		return MemberDto.Read.builder().username(username).birthday(birthday).email(email).irum(irum).joinday(joinday).profile(profile).levels(levels).build();
	}
	public void addJoinInfo(String profile, String checkcode, String encodedPassword) {
		this.profile = profile;
		this.checkcode = checkcode;
		this.password = encodedPassword;
		this.levels = Level.BRONZE;
	}
}
