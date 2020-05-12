package com.gdu.cashbook1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.gdu.cashbook1.service.MemberService;
import com.gdu.cashbook1.vo.Member;

@Controller
public class MemberController {
	
	@Autowired
	private MemberService memberService;
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
