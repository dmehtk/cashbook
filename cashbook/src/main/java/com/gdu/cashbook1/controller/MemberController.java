package com.gdu.cashbook1.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.gdu.cashbook1.service.MemberService;
import com.gdu.cashbook1.vo.*;

@Controller
public class MemberController {
	
	@Autowired
	private MemberService memberService;
	
	//로그인폼
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	//로그인 액션
	@PostMapping("/login")
	public String login(LoginMember loginMember, HttpSession session /*세션 사용*/) {
		System.out.println(loginMember.toString());
		LoginMember returnLoginMember = this.memberService.login(loginMember);
		//로그인 검증
		if(returnLoginMember == null) { //로그인 실패시
			return "redirect:/login";
		}else { //로그인 성공시
			session.setAttribute("loginMember", returnLoginMember);
			System.out.println(returnLoginMember+"<--로그인");
			return "redirect:/index";
		}
	}
	//로그아웃
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();//현재 세션을 삭제(무효화)
		System.out.println("로그아웃");
		return "redirect:/index";
	}
	//회원가입 폼
	@GetMapping("/addMember")
	public String addMember() {
		return "addMember";
	}
	//회원가입 액션
	@PostMapping("/addMember")
	public String addMember(Member member) { //commend 객체
		System.out.println(member.toString()); //toString 사용함으로써 내가 쓴 값들을 디버깅확인
		this.memberService.insertMember(member);
		return "redirect:/index";
	}
}
