package com.gdu.cashbook1.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.cashbook1.vo.Cash;
import com.gdu.cashbook1.vo.Category;
import com.gdu.cashbook1.vo.DayAndPrice;

@Mapper
public interface CashMapper {
	//수입/지출 폼 category select
	public List<Category> selectCategory();
	//수입/지출 입력 액션
	public int insertCash(Cash cash);
	//수입/지출 수정 폼 
	public Cash selectCashOne(int cashNo);
	//수입&지출 내역 업데이트
	public int updateCash(Cash cash);
	//사용날짜의 금액 합계
	public List<DayAndPrice> selectDayAndPriceList(Map<String, Object> map);
	//로그인 사용자의 오늘날짜 cash 목록
	public List<Cash> selectCashListByToday(Cash cash);
	//cash 삭제
	public int removeCash(int cashNo);
	//그 달의 수입+지출 총합
	public int selectCashKindSum(Cash cash);
}
