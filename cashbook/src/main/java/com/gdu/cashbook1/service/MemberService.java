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
	//회원 탈퇴
	public int deleteMember(LoginMember loginMember) {
		return this.memberMapper.deleteMember(loginMember);
	}
	//회원정보 service
	public Member getMemberOne(LoginMember loginMember) {
		return this.memberMapper.selectMemberOne(loginMember);
	}
	//회원가입 service
	public int insertMember(Member member) {
		return this.memberMapper.insertMember(member);
	}
	//로그인 service
	public LoginMember login(LoginMember loginMember) {
		return this.memberMapper.selectLoginMember(loginMember);
	}
	//로그인 (중복체크) service
	public String checkMemberId(String memberIdCheck) {
		return this.memberMapper.selectMemberId(memberIdCheck);
	}
	//아이디 찾기 service
	public String searchMemberId(String memberEmail) {
		return this.memberMapper.searchMemberId(memberEmail);
	}
	//비밀번호 찾기 service
	public LoginMember searchMemberPw(Member member) {
		return this.memberMapper.searchMemberPw(member);
	}
}
