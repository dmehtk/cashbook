package com.gdu.cashbook1.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gdu.cashbook1.mapper.CashMapper;
import com.gdu.cashbook1.vo.Cash;
import com.gdu.cashbook1.vo.Category;
import com.gdu.cashbook1.vo.DayAndPrice;

@Service
@Transactional //하나라도 예외 발생시 전부 취소
public class CashService {
	@Autowired private CashMapper cashMapper;
	//수입/지출 폼 (category)
	public List<Category> selectCategory(){
		return this.cashMapper.selectCategory();
	}
	// 수입/지출 입력 액션
	public int insertCash(Cash cash) {
		return this.cashMapper.insertCash(cash);
	}
	//업데이트 폼 service
	public Cash selectCashOne(int cashNo) {
		return this.cashMapper.selectCashOne(cashNo);
	}
	//업데이트 액션 service
	public int updateCash(Cash cash) {
		return this.cashMapper.updateCash(cash);
	}
	//날짜에 표기할 수입/지출 계산  service
	public List<DayAndPrice> getCashAndPriceList(String memberId, int year, int month){
		Map<String, Object> map = new HashMap<>();
		map.put("memberId", memberId);
		map.put("year", year);
		map.put("month", month);
		
		return this.cashMapper.selectDayAndPriceList(map);
	}
	//합계 계산
	//cash 삭제
	public int removeCash(int cashNo) {
		return this.cashMapper.removeCash(cashNo);
	}
	//오늘 날짜의 cash 리스트
	public Map<String, Object> getCashListByDate(Cash cash){
		List<Cash> cashList = this.cashMapper.selectCashListByToday(cash);
		int cashKindSum = this.cashMapper.selectCashKindSum(cash);
		Map<String, Object> map = new HashMap();
		map.put("cashList", cashList);
		map.put("cashKindSum", cashKindSum);
		return map;
		
	}
}
