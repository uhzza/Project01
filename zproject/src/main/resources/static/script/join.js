function usernameCheck() {
	$("#username_msg").text("");
	const pattern = /^[0-9A-Z]{8,10}$/;
	const $username = $("#username").val().toUpperCase();
	$("#username").val($username);
	const result = pattern.test($username);
	if(result==false)
		$("#username_msg").text("아이디는 대문자와 숫자 8~10자입니다").attr("class", "fail")
	return result;
}

function emailCheck() {
	$("#email_msg").text("");
	const pattern = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
	const $email = $("#email").val();
	const result = pattern.test($email);
	if(result==false)
		$("#email_msg").text("이메일을 정확하게 입력하세요").attr("class", "fail");
	return result;
}

function irumCheck() {
	$("#irum_msg").text("");
	const pattern = /^[가-힣]{2,10}$/; 
	const $irum = $("#irum").val();
	const result = pattern.test($irum);
	if(result==false)
		$("#irum_msg").text("이름은 한글 2~10자입니다");
	return result;
}

function passwordCheck() {
	$("#password_msg").text("");
	const pattern = /^(?=.*[!@#$%^&*])^[A-Za-z0-9!@#$%^&*]{8,10}$/;
	const $password = $("#password").val();
	const result = pattern.test($password);
	if(result==false)
		$("#password_msg").text("비밀번호는 영숫자와 특수문자 8~10자입니다");
	return result;
}

function password2Check() {
	$("#password2_msg").text("");
	const $password2 = $("#password2").val();
	if($password2=="") {
		$("#password2_msg").text("필수입력입니다");
		return false;
	} 
	if($password2!==$("#password").val()) {
		$("#password2_msg").text("비밀번호가 일치하지 않습니다");
		return false;
	}
	return true;
}

function birthdayCheck() {
	$("#birthday_msg").text("");
	const pattern = /^[0-9]{4}-[0-9]{2}-[0-9]{2}$/;
	const $birthday = $("#birthday").val();
	const result = pattern.test($birthday);
	if(result==false)
		$("#birthday_msg").text("정확한 날짜를 입력하세요");
	return result;
}

function loadProfile() {
	const file = $("#profile")[0].files[0];
	const maxSize = 1024*1024;			
	if(file.size>maxSize) {
		Swal.fire('프로필 크기 오류', '프로필 사진은 1MB를 넘을 수 없습니다','error');
		$("#profile").val("");
		$("#show_profile").removeAttr("src");
		return false;
	}
	const reader = new FileReader();
	reader.readAsDataURL(file);
	reader.onload = function() {
		$("#show_profile").attr("src", reader.result);
	}
	return true;
}

function printFormData(formData) {
	for(const key of formData.keys())
		console.log(key);
	for(const value of formData.values()) 
		console.log(value);
}

function join() {
	const formData = new FormData($("#join_form")[0]);
	
	$.ajax({
		url: "/member/new",
		method: "post",
		data: formData,
		processData: false,
		contentType: false
	}).then(
		(result)=>{
			Swal.fire("가입신청 완료","이메일을 확인하세요", "success").then((choice)=>{
				if(choice.isConfirmed) location.href= result.url;
			})
		}, (result)=>{
			Swal.fire('가입신청 실패', msg,'error').then((choice)=>{
				if(choice.isConfirmed) location.href= result.url;
			})
		}
	)
}

$(document).ready(function() {
	// 프로필 사진을 변경하면 출력하기
	$("#profile").on("change", loadProfile);
	
	// 아이디와 이메일를 입력하면 패턴 확인 후 서버에 사용가능여부를 확인할 것이다
	$("#username").on("blur", function() {
		if(usernameCheck()==false)
			return false;
		$.ajax("/member/check/username?username=" + $("#username").val())
			.then(
				()=>$("#username_msg").text("좋은 아이디네요").attr("class", "success"),
				()=>$("#username_msg").text("사용중인 아이디입니다").attr("class", "fail")
			);
	});
	
	$("#email").on("blur", function() {
		if(emailCheck()==false)
			return false;
		$.ajax("/member/check/email?email=" + $("#email").val())
			.then(
				()=>$("#email_msg").text("사용할 수 있는 이메일입니다").attr("class", "success"),
				()=>$("#email_msg").text("사용중인 이메일입니다").attr("class", "fail")
			);
	});
	
	// 이름, 비밀번호, 비밀번호 확인, 생일을 체크
	$("#irum").on("blur", irumCheck);
	$("#password").on("blur", passwordCheck);
	$("#password2").on("blur", password2Check);
	$("#birthday").on("blur", birthdayCheck);
	
	$("#join").on("click", function() {
		// join을 호출하기 전에 6개를 입력했고 패턴을 통과하는 지 테스트
		const r1 = usernameCheck();
		const r2 = passwordCheck();
		const r3 = password2Check();
		const r4 = irumCheck();
		const r5 = emailCheck();
		const r6 = birthdayCheck();
		if((r1 && r2 && r3 && r4 && r5 && r6) == false)
			return false;
		
		// jQuery의 $.when을 이용해 여러개의 ajax를 한꺼번에 처리할 수 있다
		$.when(
			$.ajax("/member/check/username?username="+$("#username").val()), 
			$.ajax("/member/check/email?email="+$("#email").val())
		).then(
			()=>join(),
			()=>Swal.fire("실패", "아이디나 이메일이 사용중입니다", "error")
		)	
	})
});