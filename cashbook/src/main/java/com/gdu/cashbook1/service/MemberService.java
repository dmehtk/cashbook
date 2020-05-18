package com.gdu.cashbook1.service;

import java.io.File;
import java.util.UUID;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.gdu.cashbook1.mapper.MemberMapper;
import com.gdu.cashbook1.vo.LoginMember;
import com.gdu.cashbook1.vo.Member;
import com.gdu.cashbook1.vo.MemberForm;

@Service
@Transactional //하나라도 예외 발생시 전부 취소
public class MemberService {
	@Autowired
	private MemberMapper memberMapper;
	@Autowired
	private JavaMailSender javaMailSender;
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
	//회원가입 insert(사진 추가)
	public int addMember(MemberForm memberForm) {
		
		MultipartFile mf = memberForm.getMemberPic();
		//확장자 필요
		
		String originName = mf.getOriginalFilename();
		System.out.println(originName+"<-----originName");
		/*
		if(mf.getContentType().equals("image/png") || mf.getContentType().equals("image/jpg")) {
			// 업로드 
			
		}else {
			// 업로드 실패
		}*/
		
		int lastIndex = originName.lastIndexOf(".");
		String extension = originName.substring(lastIndex);
		String memberPic = memberForm.getMemberId()+extension;
		System.out.println(memberPic + "<---memberPic");
		//	 db에 저장
		
		Member member = new Member();
		// memberForm -> member
		member.setMemberId(memberForm.getMemberId());
		member.setMemberName(memberForm.getMemberName());
		member.setMemberAddr(memberForm.getMemberAddr());
		member.setMemberEmail(memberForm.getMemberEmail());
		member.setMemberPhone(memberForm.getMemberPhone());
		member.setMemberPw(memberForm.getMemberPw());
		member.setMemberPic(memberPic);
		System.out.println(member+"<---member ");
		int row = this.memberMapper.insertMember(member);
		//1. 파일 저장
		String path = "D:\\git-cashbook\\cashbook\\src\\main\\resources\\static\\upload";
		File file = new File(path+"\\"+memberPic);
		try {
			mf.transferTo(file);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new RuntimeException();
			/*Exception
			 * 1. 예외처리를 해야만 문법적으로 이상없는 예외
			 * 2. 예외처리를 콛에서 구현하지 않아도 아무문제없는 예외 RuntimeException
			 * */
		}
		
		//return this.memberMapper.insertMember(member);
		return row;
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
	public int getMemberPw(Member member) {
		UUID uuid = UUID.randomUUID();// 랜덤 문자열 생성 라이브러리(api)
		
		String memberPw = uuid.toString().substring(0, 8);
		System.out.println(memberPw +"<---바뀐비번");
		member.setMemberPw(memberPw);
		int row = this.memberMapper.updateMemberPw(member);
		if(row == 1) {
			// 메일로 update성공한 랜덤 pw를 전송
			// 메일 객체 new JavaMailSender();
			SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
			simpleMailMessage.setTo(member.getMemberEmail());
			simpleMailMessage.setFrom("dmehtk@gmail.com");
			simpleMailMessage.setSubject("cashbook 비밀번호찾기 메일");
			simpleMailMessage.setText("변경된 비밀번호 : "+ memberPw + "입니다");
			this.javaMailSender.send(simpleMailMessage);
			System.out.println();
		}
		return row;
	}
}
