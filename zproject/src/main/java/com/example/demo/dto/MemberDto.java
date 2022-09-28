package com.example.demo.dto;

import java.time.*;

import javax.validation.constraints.*;

import org.springframework.web.multipart.*;

import com.example.demo.entity.*;

import lombok.*;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public class MemberDto {
	@Data
	public static class IdCheck {
		@Pattern(regexp="^[A-Z0-9]{8,10}$", message = "아이디는 대문자와 숫자 8~10자입니다")
		@NotEmpty(message="아이디는 필수입력입니다")
		private String username;
	}
	
	@Data
	public static class EmailCheck {
		@Email
		@NotEmpty(message="이메일은 필수입력입니다")
		private String email;
	}
	
	@Data
	@Builder
	public static class Join {
		@Pattern(regexp="^[A-Z0-9]{8,10}$", message = "아이디는 대문자와 숫자 8~10자입니다")
		@NotEmpty(message="아이디는 필수입력입니다")
		private String username;
		
		@NotEmpty(message="이름은 필수입력입니다")
		private String irum;
		
		@NotEmpty(message="비밀번호는 필수입력입니다")
		private String password;
		
		@Email
		@NotEmpty(message="이메일은 필수입력입니다")
		private String email;
		
		@NotNull(message="생일은 필수입력입니다")
		private LocalDate birthday;
		
		private MultipartFile profile;
		public Member toEntity() {
			return Member.builder().username(username).password(password).irum(irum).email(email).birthday(birthday).build();
		}
	}
	
	@Data
	public static class FindId {
		@NotEmpty(message="이메일는 필수입력입니다")
		@Email(message="잘못된 이메일 형식입니다")
		private String email;
	}
	
	@Data
	public static class ResetPassword {
		//@Pattern(regexp="^[A-Z0-9]{8,10}$", message = "아이디는 대문자와 숫자 8~10자입니다")
		@NotEmpty(message="아이디는 필수입력입니다")
		private String username;
		
		@Email
		@NotEmpty(message="이메일은 필수입력입니다")
		private String email;
	}

	@Data
	public static class ChangePassword {	
		@NotEmpty(message="비밀번호는 필수입력입니다")
		private String password;
		
		@NotEmpty(message="새 비밀번호는 필수입력입니다")
		private String newPassword;
	}

	@Data
	@Builder
	public static class Read {
		private String username;
		private String irum;
		private String email;
		private LocalDate birthday;
		private LocalDate joinday;
		private Long days;
		
		private Level levels;
		private String profile;
	}

	@Data
	public static class Update {
		private String email;
		private MultipartFile profile;
	}
}
