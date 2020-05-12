package com.gdu.cashbook1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.gdu.cashbook1.vo.Member;

@Controller
public class MemberController {
	@GetMapping("/addMember")
	public String addMember() {
		return "addMember";
	}
	
	@PostMapping("/addMember")
	public String addMember(Member member) { //commend 객체
		System.out.println(member.toString());
		return "redirect:/index";
	}
}
