package com.gdu.cashbook1.service;

import java.util.HashMap;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gdu.cashbook1.mapper.BoardMapper;
import com.gdu.cashbook1.mapper.CommentMapper;
import com.gdu.cashbook1.vo.Board;
import com.gdu.cashbook1.vo.Comment;

@Service
@Transactional
public class BoardService {
	
	@Autowired
	private BoardMapper boardMapper;
	@Autowired
	private CommentMapper commentMapper;
	
	public int insertBoard(Board board) {
		return this.boardMapper.insertBoard(board);
	}
	//게시글 삭제 액션
	public int deleteBoard(int boardNo) {
		//댓글 삭제
		this.commentMapper.deleteCommentByBoard(boardNo);
		// 삭제 후 게시글 삭제
		return this.boardMapper.deleteBoard(boardNo);
	}
	//게시글 업데이트 액션
	public int detailBoardUpdate(Board board) {
		return this.boardMapper.updateBoard(board);
	}
	//게시물 상세보기 & 댓글(+페이징)
	public Map<String, Object> selectBoardOneByComment(int boardNo, int beginRow, int rowPerPage) {
		//댓글
		Map<String, Object> map1 = new HashMap<>();
		map1.put("boardNo", boardNo);
		map1.put("beginRow", beginRow);
		map1.put("rowPerPage", rowPerPage);
		//게시글
		System.out.println(boardNo+"<--boardNo");
		//상세보기 출력값
		Board board = this.boardMapper.selectBoardOne(boardNo);
		/*이전 열 번호
		int preNumberSelect = this.boardMapper.preNumberSelect(boardNo);
		//다음 열 번호
		int nextNumberSelect = this.boardMapper.nextNumberSelect(boardNo);
		//제일 작은 열 번호*/
		int min = this.boardMapper.minBoardOne();
		System.out.println(min+"<---min");
		//댓글 -----
		List<Comment> list = this.commentMapper.selectCommentList(map1);
		int commentTotalCount = this.commentMapper.selectCommentListTotal(boardNo);
		int commentLastPage = commentTotalCount/rowPerPage;
		if(commentTotalCount%rowPerPage != 0) {
			commentLastPage +=1;
		}
		Map<String, Object> map2 = new HashMap<>();
		map2.put("board", board);
		map2.put("min", min);
		//map2.put("pre", preNumberSelect);
		//map2.put("next", nextNumberSelect);
		map2.put("list", list);
		map2.put("lastPage", commentLastPage);
		
		return map2;
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
