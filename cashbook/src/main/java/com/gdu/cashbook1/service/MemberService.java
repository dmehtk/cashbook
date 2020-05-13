package com.gdu.cashbook1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gdu.cashbook1.mapper.MemberMapper;
import com.gdu.cashbook1.vo.LoginMember;
import com.gdu.cashbook1.vo.Member;

@Service
@Transactional //하나라도 예외 발생시 전부 취소
public class MemberService {
	@Autowired
	private MemberMapper memberMapper;
	//회원가입 service
	public int insertMember(Member member) {
		return this.memberMapper.insertMember(member);
	}
	//로그인 service
	public LoginMember login(LoginMember loginMember) {
		return this.memberMapper.selectLoginMember(loginMember);
	}
}
