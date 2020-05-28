package com.gdu.cashbook1.controller;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gdu.cashbook1.service.CashService;
import com.gdu.cashbook1.vo.Cash;
import com.gdu.cashbook1.vo.Category;
import com.gdu.cashbook1.vo.DayAndPrice;
import com.gdu.cashbook1.vo.LoginMember;

@Controller
public class CashController {
	@Autowired private CashService cashService;
	@GetMapping("/insertCategory")
	public String insertCategory(HttpSession session, Model model, @RequestParam(value="day" , required = false) String day) {
		if(session.getAttribute("loginMember") == null) {
	         return "redirect:/index";
	      }
		model.addAttribute("day", day);
		return "insertCategory";
	}
	@PostMapping("/insertCategory")
	public String insertCategory(HttpSession session, @RequestParam(value="categoryName") String categoryName,  @RequestParam(value="day" , required = false) String day) {
		String memberId = ((LoginMember)session.getAttribute("loginMember")).getMemberId();
		Category category = new Category();
		category.setCategoryName(categoryName);
		category.setMemberId(memberId);
		this.cashService.insertCategory(category);
		return "redirect:/insertCash?memberId="+memberId+"&day="+day;
	}
	//수입/지출 입력 폼
	@GetMapping("/insertCash")
	public String insertCash(HttpSession session, Model model,  @RequestParam(value="day" , required = false) String day, @RequestParam(value="memberId") String memberId) {
		System.out.println(day+"<----입력하려는 날짜");
		System.out.println(memberId+"<---memberId");
		if(session.getAttribute("loginMember") == null) {
	         return "redirect:/index";
	      }
		List<Category> list = new ArrayList<Category>();
		list = this.cashService.selectCategory(memberId);
		System.out.println(list+"<---list");
		model.addAttribute("day", day);
		model.addAttribute("list", list);
		return "insertCash";
	}
	//수입/지출  입력 액션
	@PostMapping("/insertCash")
	public String insertCash(HttpSession session, Cash cash) {
		System.out.println(cash);
		if(session.getAttribute("loginMember") == null) {
	         return "redirect:/index";
	      }
		System.out.println(cash+"<---cash 정보 확인");
		String memberId = ((LoginMember)session.getAttribute("loginMember")).getMemberId();
		System.out.println(memberId+"<---memberId");
		cash.setMemberId(memberId);
		this.cashService.insertCash(cash);
		
		return "redirect:/getCashListByDate?day="+cash.getCashDate();
	}
	//캐시 업데이트 폼
	@GetMapping("/updateCash")
	public String updateCash(HttpSession session , Model model, @RequestParam(value="cashNo") int cashNo) {
		System.out.println(cashNo+"<---cashNo");
		if(session.getAttribute("loginMember") == null) {
	         return "redirect:/index";
	      }
		Cash cash = this.cashService.selectCashOne(cashNo);
		model.addAttribute("cash", cash);
		
		return "updateCash";
	}
	//캐시 업데이트 액션
	@PostMapping("/updateCash")
	public String updateCash(HttpSession session ,Model model, Cash cash ) {
		System.out.println(cash+"<---view 에서 넘어온 cash. ~ 값");
		if(session.getAttribute("loginMember") == null) {
	         return "redirect:/index";
	      }
		int row = this.cashService.updateCash(cash);
		System.out.println(row+"row값");
		return "redirect:/getCashListByDate";
		
	}
	//가계부 캘린더 폼
	@GetMapping("/getCashListByMonth")
	public String getCashListByMonth(HttpSession session , Model model,
			@RequestParam(value="day" , required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate day) {
		 // 로그인이 되어있지 않으면
	      if(session.getAttribute("loginMember") == null) {
	         return "redirect:/index";
	      }
			
			Calendar cDay = Calendar.getInstance(); //오늘
		
		  if(day == null) {
		         day = LocalDate.now();
		      } else {
		         // day -- cDay 변환
		         // LocalDate -> Calendar
		         // 오늘 날짜에서 day값과 동일하게 변경
		         cDay.set(day.getYear(), day.getMonthValue()-1, day.getDayOfMonth());
		      }
		      // 일별 수입지출 총액 받아오기
		      String memberId = ((LoginMember)session.getAttribute("loginMember")).getMemberId();
		      System.out.println(memberId + "<-- CashController.getCashListByMonth : memberId");
		      int year = cDay.get(Calendar.YEAR);
		      System.out.println(year + "<-- CashController.getCashListByMonth : year");
		      int month = cDay.get(Calendar.MONTH)+1;
		      System.out.println(month + "<-- CashController.getCashListByMonth : month");
		      List<DayAndPrice> dayAndPriceList = cashService.getCashAndPriceList(memberId, year, month);
		      System.out.println(dayAndPriceList +"<---dayandprice");
		      
		      // 0. 오늘 LocalDate타입 1. 오늘이 Calendar 무슨달 2. 이번달의 마지막 일 3. 이번달 1일의 요일
		      model.addAttribute("day", day);
		      model.addAttribute("dayAndPriceList", dayAndPriceList);
		      // 지금 년도
		      model.addAttribute("year", cDay.get(Calendar.YEAR));
		      // 지금 월
		      //model.addAttribute("month", cDay.get(Calendar.MONTH)+1);
		      // 그 달의 마지막 일
		      model.addAttribute("lastDay", cDay.getActualMaximum(Calendar.DATE));
		      // 날짜를 하나 더 구해서
		      Calendar firstDay = cDay;
		      // 1로 변경
		      firstDay.set(Calendar.DATE, 1);
		      // 요일 1=일요일~7=토요일
		      // cDay.get(Calendar.DAY_OF_WEEK);
		      model.addAttribute("firstDayOfWeek", firstDay.get(Calendar.DAY_OF_WEEK));
		      System.out.println(firstDay.get(Calendar.DAY_OF_WEEK));
		      
		      return "getCashListByMonth";


	}
	// cash 삭제
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
		System.out.println(day+"<---day getCashListByDate");
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
		//맵 사용
		Map<String, Object> map = this.cashService.getCashListByDate(cash);
		//리스트
		model.addAttribute("cashList", map.get("cashList"));
		//금액 합계
		model.addAttribute("cashKindSum", map.get("cashKindSum"));
		//날짜
		model.addAttribute("day", day);
		//년도
		model.addAttribute("year", day.getYear());
		
		return "getCashListByDate";
	}
}
