package com.gdu.cashbook1.service;

import java.util.HashMap;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gdu.cashbook1.mapper.BoardMapper;
import com.gdu.cashbook1.vo.Board;

@Service
@Transactional
public class BoardService {
	
	@Autowired
	private BoardMapper boardMapper;
	//게시글 삭제 액션
	public int deleteBoard(int boardNo) {
		return this.boardMapper.deleteBoard(boardNo);
	}
	//게시글 업데이트 액션
	public int detailBoardUpdate(Board board) {
		return this.boardMapper.updateBoard(board);
	}
	//게시물 상세보기
	public Map<String, Object> selectBoardOne(int boardNo) {
		System.out.println(boardNo+"<--boardNo");
		Board board = this.boardMapper.selectBoardOne(boardNo);
		int totalCount = this.boardMapper.totalBoard();
		int min = this.boardMapper.minBoardOne();
		System.out.println(min+"<---min");
		System.out.println(totalCount+"<---totalCount");
		Map<String, Object> map = new HashMap<>();
		map.put("board", board);
		map.put("totalCount", totalCount);
		map.put("min", min);
		
		return map;
	}
	//게시판 리스트 service
	public Map<String, Object> selectBoard(int beginRow, int rowPerPage, String boardTitle) {
		System.out.println(boardTitle+"<---검색값");
		Map<String, Object> map1 = new HashMap<>();
		
		map1.put("beginRow", beginRow);
		map1.put("rowPerPage", rowPerPage);
		map1.put("boardTitle", boardTitle);
		
		Map<String, Object> map2 = new HashMap<>();
		int totalRow = 0;
		if(boardTitle.equals("")) {
			totalRow = this.boardMapper.totalBoard();
		}else {
			totalRow = this.boardMapper.totalBoardByTitle(boardTitle);
		}
		System.out.println(totalRow +"<----totalRow");
		int lastPage = totalRow/rowPerPage;
		if(totalRow%rowPerPage != 0) {
			lastPage+=1;
		}
		System.out.println(lastPage);
		List<Board> list = this.boardMapper.selectBoard(beginRow, rowPerPage, boardTitle);
		map2.put("list", list);
		map2.put("lastPage", lastPage);
		
		return map2;
	}
	
}
