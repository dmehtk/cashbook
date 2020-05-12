package com.gdu.cashbook1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdu.cashbook1.mapper.MemberMapper;
import com.gdu.cashbook1.vo.Member;

@Service
public class MemberService {
	@Autowired
	private MemberMapper memberMapper;
	//회원가입 service
	public int insertMember(Member member) {
		return this.memberMapper.insertMember(member);
	}
}
