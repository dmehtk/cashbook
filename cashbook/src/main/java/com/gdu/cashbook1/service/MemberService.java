package com.gdu.cashbook1.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.gdu.cashbook1.mapper.BoardMapper;
import com.gdu.cashbook1.mapper.CashMapper;
import com.gdu.cashbook1.mapper.CommentMapper;
import com.gdu.cashbook1.mapper.MemberMapper;
import com.gdu.cashbook1.vo.Board;
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
	@Autowired
	private BoardMapper boardMapper;
	@Autowired
	private CommentMapper commentMapper;
	@Autowired
	private CashMapper cashMapper;
	@Value("D:\\yusuk\\maven.1590371282000\\cashbook\\src\\main\\resources\\static\\upload\\") // 파일 경로
	private String path;
	
	public int deleteMemberByinsertMemberidByAdmin(String memberId) {
		
		//1. 멤버 이미지 파일 삭제
		//1-1 파일 이름 select member_pic from member
			String memberPic = this.memberMapper.selectMemberPic(memberId);
			System.out.println(memberPic+"<---memberPic");
		//1-2 파일 삭제
			File file = new File(path+memberPic);
			if(file.exists()) { // 파일이 있는지 확인
				if(file.getName() != "default.png") {
					file.delete();
				}
			}
		//1.
		//카테고리 삭제
		this.cashMapper.deleteCategoryById(memberId);
		//캐시삭제
		this.cashMapper.deleteCashById(memberId);
		//댓글 삭제
		this.commentMapper.deleteCommentById(memberId);
		//게시글 삭제
		this.boardMapper.deleteBoardById(memberId);
		this.memberMapper.deleteMemberByAdmin(memberId);
		//id 추가
		return this.memberMapper.insertMemberidByAdmin(memberId);
	}
	//회원 정보 리스트
	public Map<String, Object> selectMemberList(int beginRow, int rowPerPage){
		Map<String, Object> map1 = new HashMap<>();
		
		map1.put("beginRow", beginRow);
		map1.put("rowPerPage", rowPerPage);
		
		Map<String, Object> map2 = new HashMap<>();
		int totalRow = this.memberMapper.selectTotalMember();
		System.out.println(totalRow +"<----totalRow");
		int lastPage = totalRow/rowPerPage;
		if(totalRow%rowPerPage != 0) {
			lastPage+=1;
		}
		System.out.println(lastPage);
		List<Member> list = this.memberMapper.selectMemberList(beginRow, rowPerPage);
		map2.put("list", list);
		map2.put("lastPage", lastPage);
		return map2;
	}
	//회원 정보 수정
	public int updateMember(MemberForm memberForm) {
		
			//데이터 베이스에 저장된 파일의 이름을 불러옴
			String memberPic = this.memberMapper.selectMemberPic(memberForm.getMemberId());
			System.out.println(memberPic+"<---memberPic");
			// 파일이 있다면 삭제해줌
			File file = new File(path+memberPic);
			System.out.println(file+"<---file");
			//파일 삭제
			if(file.exists()) { // 파일이 있는지 확인
				//파일 이름이 default.png (기본값) 이 아니라면 삭제
				if(file.getName() != "default.png") {
					file.delete();
				}
			}
			//삭제후 member에 값들을 넣어준다
			Member member = new Member();
			member.setMemberId(memberForm.getMemberId());
			member.setMemberName(memberForm.getMemberName());
			member.setMemberAddr(memberForm.getMemberAddr());
			member.setMemberEmail(memberForm.getMemberEmail());
			member.setMemberPhone(memberForm.getMemberPhone());
			member.setMemberPw(memberForm.getMemberPw());
			member.setMemberPic(memberPic);
			System.out.println(member);
			// 업데이트문 실행
			int row = this.memberMapper.updateMember(member);
			System.out.println(row +"<---row 값");
			MultipartFile pic = memberForm.getMemberPic();
			System.out.println(pic +"<--pic ");
			
			//삭제 후 파일생성
			try {
				pic.transferTo(file);
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		return row;
	}
	//회원 탈퇴후 탈퇴한 아이디 추가 (트랜잭션 처리)
	public int deleteMemberByinsertMemberid(LoginMember loginMember) {
		
		//1. 멤버 이미지 파일 삭제
		//1-1 파일 이름 select member_pic from member
			String memberPic = this.memberMapper.selectMemberPic(loginMember.getMemberId());
			System.out.println(memberPic+"<---memberPic");
		//1-2 파일 삭제
			File file = new File(path+memberPic);
			if(file.exists()) { // 파일이 있는지 확인
				if(file.getName() != "default.png") {
					file.delete();
				}
			}
		String memberId = loginMember.getMemberId();
		//1.
		//카테고리 삭제
		this.cashMapper.deleteCategoryById(memberId);
		//캐시삭제
		this.cashMapper.deleteCashById(memberId);
		//댓글 삭제
		this.commentMapper.deleteCommentById(memberId);
		//게시글 삭제
		this.boardMapper.deleteBoardById(memberId);
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
			//확장자 필요
			MultipartFile mf = memberForm.getMemberPic();
			String memberPic = "";
			//파일의 풀네임
			String originName = mf.getOriginalFilename();
			System.out.println(originName+"<-----originName");
			/*
			if(mf.getContentType().equals("image/png") || mf.getContentType().equals("image/jpg")) {
				// 업로드 
				
			}else {
				// 업로드 실패
			}*/
			//파일의 이름이 없으면
			if(originName.equals("")) {
				memberPic = "default.png";
				System.out.println(memberPic +"<---memberForm.getMemberPic() == null 일때 memberPic");
			//파일의 이름이 있으면
			}else {
				// 마지막 .을 찾고
				int lastIndex = originName.lastIndexOf(".");
				//.부터 자른다
				String extension = originName.substring(lastIndex);
				// id.~ 
				memberPic = memberForm.getMemberId()+extension;
				System.out.println(memberPic + "<---memberPic");
			}
		
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
		File file = new File(path+memberPic);
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
