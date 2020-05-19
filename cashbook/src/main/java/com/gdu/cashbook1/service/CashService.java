package com.gdu.cashbook1.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gdu.cashbook1.mapper.CashMapper;
import com.gdu.cashbook1.vo.Cash;

@Service
@Transactional //하나라도 예외 발생시 전부 취소
public class CashService {
	@Autowired private CashMapper cashMapper;
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
