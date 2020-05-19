package com.gdu.cashbook1.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gdu.cashbook1.mapper.CashMapper;
import com.gdu.cashbook1.vo.Cash;

@Service
@Transactional //하나라도 예외 발생시 전부 취소
public class CashService {
	@Autowired private CashMapper cashMapper;
	//오늘 날짜의 cash 리스트
	public List<Cash> getCashListByDate(Cash cash){
		return this.cashMapper.selectCashListByToday(cash);
	}
}
