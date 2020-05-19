package com.gdu.cashbook1.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.gdu.cashbook1.service.CashService;
import com.gdu.cashbook1.vo.Cash;
import com.gdu.cashbook1.vo.LoginMember;

@Controller
public class CashController {
	@Autowired private CashService cashService;
	
	//오늘 날짜 cash 리스트 폼
	@GetMapping("/getCashListByDate")
	public String getCashListByDate(HttpSession session, Model model) {
		if(session.getAttribute("loginMember") == null) {
			return "redirect:index";
		}
		//로그인 아이디
		String loginMemberId = ((LoginMember)session.getAttribute("loginMember")).getMemberId();
		System.out.println(loginMemberId);
		//오늘 날짜를 받아옴  ex ) 2020년 5월 20일
		Date today = new Date();
		System.out.println(today+"<---포맷 변경전 날짜");
		//날짜 형식 변환 2020-05-20
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String strToday = sdf.format(today);
		System.out.println(strToday);
		// 제목 날짜 형식
		SimpleDateFormat titleDate = new SimpleDateFormat("yyyy년MM월dd일");
		String titleToday = titleDate.format(today);
		System.out.println(titleToday);
		// cash 안에 set으로 값을 넣어줌
		Cash cash = new Cash();
		cash.setMemberId(loginMemberId);
		System.out.println(cash.getMemberId());
		cash.setCashDate(strToday);
		
		List<Cash> cashList = this.cashService.getCashListByDate(cash);
		System.out.println(cashList);
		//페이지로 값을 넘겨줌
		model.addAttribute("cashList", cashList);
		model.addAttribute("title", titleToday);
		
		
		return "getCashListByDate";
	}
}
