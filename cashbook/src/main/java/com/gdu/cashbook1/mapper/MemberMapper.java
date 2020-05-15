package com.gdu.cashbook1.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.cashbook1.vo.*;

@Mapper //mapper 기능 + component 기능
public interface MemberMapper {
	//회원 정보수정
	public int updateMember(Member member);
	//회원 탈퇴후 삭제된 아이디 추가 insert
	public int insertMemberid(LoginMember loginMember);
	//회원 탈퇴 delete
	public int deleteMember(LoginMember loginMember);
	//회원 정보 select
	public Member selectMemberOne(LoginMember loginMember);
	//회원가입 insert
	public int insertMember(Member member); 
	//로그인 select
	public LoginMember selectLoginMember(LoginMember loginMember);
	//중복체크 select
	public String selectMemberId(String memberIdCheck);
	//아이디 찾기 select 
	public String searchMemberId(Member member);
	//비밀번호 찾기 select
	public LoginMember searchMemberPw(Member member);
	
}
