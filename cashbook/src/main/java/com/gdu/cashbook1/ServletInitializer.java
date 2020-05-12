package com.gdu.cashbook1;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {

	/*
	 * servelt API
	 * 1. servlet : 요청처리
	 * 2. filter : 요청전후 처리
	 * 3. litener : 이벤트 반응 처리
	 * */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(CashbookApplication.class);
	}

}
