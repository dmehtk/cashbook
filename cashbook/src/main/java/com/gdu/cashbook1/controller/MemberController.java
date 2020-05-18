package com.gdu.cashbook1.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gdu.cashbook1.service.MemberService;
import com.gdu.cashbook1.vo.*;

@Controller
public class MemberController {
	
	@Autowired
	private MemberService memberService;
	//회원 정보 수정 폼
	@GetMapping("/updateMember")
	public String updateMember(HttpSession session, Model model) {
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/index";
		}
		Member member = this.memberService.getMemberOne((LoginMember)(session.getAttribute("loginMember")));
		System.out.println(member+"<--member");
		model.addAttribute("member", member);
		return "updateMember";
	}
	//회원 정보 수정 액션
	@PostMapping("/updateMember")
	public String updateMember(Member member, Model model) {
		int row = this.memberService.updateMember(member);
		System.out.println(row+"<---row 값");
		if(row == 0) {
			model.addAttribute("msg", "수정된 값이 없습니다.");
			return "updateMember";
		}else {
			return "redirect:/memberInfo";
		}
		
	}
	
	
	
	//회원탈퇴 비밀번호 확인 폼
	@GetMapping("/memberPwConfirm")
	public String memberPwConfirm(HttpSession session, Model model) {
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/index";
		}
		//현재 회원의 아이디를 출력하기 위해 값을 보내줌
		model.addAttribute("loginMember", session.getAttribute("loginMember"));
		return "memberPwConfirm";
	}
	//회원탈퇴 비밀번호 확인 액션 + 회원 탈퇴 후 탈퇴한 아이디 추가
	@PostMapping("/memberPwConfirm")
	public String memberPwConfirm(HttpSession session, Model model, LoginMember loginMember) {
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/index";
		}
		//delete 쿼리 결과를 int 값으로 받음
		int row = this.memberService.deleteMemberByinsertMemberid(loginMember);
		System.out.println(row+"<--결과값  0 or 1");
		//0일시에는 비밀번호가 틀립니다 문구 출력
		if(row == 0) {
			model.addAttribute("msg", "비밀번호가 틀립니다");
			return "memberPwConfirm";
		//0이 아니라면 index로 복귀
		}else {
			System.out.println("회원탈퇴 성공");
			//남아있는 세션값을 무효화해줌
			session.invalidate();
			return "redirect:/index";
		}
	}
	//회원 정보 폼
	@GetMapping("/memberInfo")
	public String memberInfo(HttpSession session, Model model) {
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/index";
		}
		Member member = this.memberService.getMemberOne((LoginMember)(session.getAttribute("loginMember")));
		System.out.println(member+"<--member");
		model.addAttribute("member", member);
		return "memberInfo";
	}
	
	//비밀번호 찾기 폼
	@GetMapping("/searchMemberPw")
	public String searchMemberPw(HttpSession session, Model model /*, @RequestParam("memberId") String memberId*/) {
		if(session.getAttribute("loginMember") != null) {
			return "redirect:/index";
		}
		//매개 변수를 받아와서 값을 같이 날려줌
		/*System.out.println(memberId+"<--memerId");
		model.addAttribute("memberId", memberId);*/
		return "searchMemberPw";
	}
	//비밀번호 찾기 액션
	@PostMapping("/searchMemberPw")
	public String searchMemberPw(HttpSession session, Model model, Member member) {
		System.out.println(member);
		//세션정보 확인 (로그인 상태인지 아닌지)
		if(session.getAttribute("loginMember") != null) {
			return "redirect:/index";
		}
		int row = this.memberService.getMemberPw(member);
		System.out.println(row+"<---row 값");
		String msg = "";
		if(row == 1) {
			msg = "비밀번호를 입력한 메일로 전송하였습니다";
		}else {
			msg = "비밀번호가 틀렸습니다";
		}
		model.addAttribute("msg", msg);
		return "login";
	}
	//아이디 찾기 폼
	@GetMapping("/searchMemberId")
	public String searchMemberId(HttpSession session) {
		if(session.getAttribute("loginMember") != null) {
			return "redirect:/index";
		}
		return "searchMemberId";
	}
	
	//아이디 찾기 액션
	@PostMapping("/searchMemberId")
	public String searchMemberId(HttpSession session, Model model, Member member) {
		if(session.getAttribute("loginMember") != null) {
			return "redirect:/index";
		}
		String searchMemberId = this.memberService.searchMemberId(member);
		if(searchMemberId == null) {
			System.out.println("아이디 없음");
			model.addAttribute("msg", "아이디가 없습니다");
		}else {
			System.out.println("아이디 있음");
			System.out.println(searchMemberId);
			model.addAttribute("searchId", searchMemberId);
		}
		return "searchMemberId";
	}
	
	//회원가입폼 + 중복확인
	@PostMapping("/CheckMemberId")
	public String checkMemberId(HttpSession session, Model model,@RequestParam("memberIdCheck") String memberIdCheck) {
		if(session.getAttribute("loginMember") != null) {
			return "redirect:/index";
		}
		
		String confirmMemberId = this.memberService.checkMemberId(memberIdCheck);
		if(confirmMemberId == null) {
			//아이디 사용 가능
			System.out.println("아이디 사용 가능");
			model.addAttribute("memberId", memberIdCheck);
		}else {
			//아이디 사용할 수 없음
			System.out.println("아이디 사용 불가능");
			model.addAttribute("msg", "아이디가 이미 존재합니다.");
		}
		return "addMember";
	}
	
	//로그인폼
	@GetMapping("/login")
	public String login(HttpSession session) {
		//로그인 상태일때
		if(session.getAttribute("loginMember") != null) {
			return "redirect:/index";
		}
		//로그인 중일때
		return "login";
	}
	
	//로그인 액션
	@PostMapping("/login")
	public String login(Model model,LoginMember loginMember, HttpSession session /*세션 사용*/) {
		System.out.println(loginMember.toString());
		LoginMember returnLoginMember = this.memberService.login(loginMember);
		//로그인 검증
		if(returnLoginMember == null) { //로그인 실패시
			model.addAttribute("msg", "아이디와 비밀번호를 확인하세요");
			return "login";
		}else { //로그인 성공시
			session.setAttribute("loginMember", returnLoginMember);
			System.out.println(returnLoginMember+"<--로그인");
			return "redirect:/home";
		}
	}
	
	//로그아웃
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/index";
		}
		session.invalidate();//현재 세션을 삭제(무효화)
		System.out.println("로그아웃");
		return "redirect:/index";
	}
	
	//회원가입 폼
	@GetMapping("/addMember")
	public String addMember(HttpSession session) {
		//로그인중일때
		if(session.getAttribute("loginMember") != null) {
			return "redirect:/index";
		}
		return "addMember";
	}
	
	//회원가입 액션
	@PostMapping("/addMember")
	public String addMember(MemberForm memberForm, HttpSession session) { //commend 객체
		if(session.getAttribute("loginMember") != null) {
			return "redirect:/index";
		}
		System.out.println(memberForm.toString());
		
		//System.out.println(member.toString()); //toString 사용함으로써 내가 쓴 값들을 디버깅확인
		this.memberService.addMember(memberForm);
		return "redirect:/index";
	}
}
