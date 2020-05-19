package com.gdu.cashbook1.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gdu.cashbook1.service.CashService;
import com.gdu.cashbook1.vo.Cash;
import com.gdu.cashbook1.vo.LoginMember;

@Controller
public class CashController {
	@Autowired private CashService cashService;
	@GetMapping("/removeCash")
	public String removeCash(HttpSession session, @RequestParam(value="cashNo") int cashNo) {
		System.out.println(cashNo+"<--cashNo");
		if(session.getAttribute("loginMember") == null) {
			return "redirect:index";
		}
		this.cashService.removeCash(cashNo);
		return "redirect:/getCashListByDate";
	}
	//오늘 날짜 cash 리스트 폼
	@GetMapping("/getCashListByDate")
	public String getCashListByDate(HttpSession session, Model model,
			@RequestParam(value="day" , required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate day) {
		if(day == null) {
			day = LocalDate.now();
		}
		System.out.println(day);
		if(session.getAttribute("loginMember") == null) {
			return "redirect:index";
		}
		//로그인 아이디
		String loginMemberId = ((LoginMember)session.getAttribute("loginMember")).getMemberId();
		System.out.println(loginMemberId);
		
		/*오늘 날짜를 받아옴  ex ) 2020년 5월 20일
		//날짜 형식 변환 2020-05-20
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String strToday = sdf.format(day);
		System.out.println(strToday);
		*/
	 // 날짜 형식을 원하는 문자열 형태로 변경
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
		String dday = day.format(formatter); 
		System.out.println(dday + "<-- CashController:getCashListByDate strToday");

		// cash 안에 set으로 값을 넣어줌
		Cash cash = new Cash();
		cash.setMemberId(loginMemberId);
		System.out.println(cash.getMemberId());
		cash.setCashDate(dday);
		
		Map<String, Object> map = this.cashService.getCashListByDate(cash);
		model.addAttribute("cashList", map.get("cashList"));
		model.addAttribute("cashKindSum", map.get("cashKindSum"));
		model.addAttribute("day", day);
		model.addAttribute("year", day.getYear());
		
		return "getCashListByDate";
	}
}
