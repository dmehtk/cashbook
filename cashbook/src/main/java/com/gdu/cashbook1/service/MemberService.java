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
	//회원 정보 수정
	public int updateMember(Member member) {
		return this.memberMapper.updateMember(member);
	}
	//회원 탈퇴후 탈퇴한 아이디 추가 (트랜잭션 처리)
	public int deleteMemberByinsertMemberid(LoginMember loginMember) {
		//1.
		int row = this.memberMapper.deleteMember(loginMember);
		// 실행된 값에 따라 0반환 혹은 1반환
		if(row == 0) {
			return 0;
		}else {
			return this.memberMapper.insertMemberid(loginMember);
		}
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
	public String searchMemberId(Member member) {
		return this.memberMapper.searchMemberId(member);
	}
	//비밀번호 찾기 service
	public LoginMember searchMemberPw(Member member) {
		return this.memberMapper.searchMemberPw(member);
	}
}
