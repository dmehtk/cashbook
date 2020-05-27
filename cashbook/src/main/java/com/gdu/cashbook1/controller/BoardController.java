package com.gdu.cashbook1.controller;

import java.util.*;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gdu.cashbook1.service.BoardService;
import com.gdu.cashbook1.vo.Board;

@Controller
public class BoardController {
	@Autowired
	private BoardService boardService;
	//게시물 추가
	@GetMapping("/insertBoard")
	public String insertBoard(HttpSession session, Model model) {
		if(session.getAttribute("loginMember") == null) {
	         return "redirect:/index";
	      }
		model.addAttribute("member", session.getAttribute("loginMember"));
		return "insertBoard";
	}
	@PostMapping("/insertBoard")
	public String insertBoard(Board board) {
		this.boardService.insertBoard(board);
		return "redirect:/getBoardList";
	}
	//게시글 삭제
	@GetMapping("/deleteBoard")
	public String deleteBoard(@RequestParam(value= "boardNo") int boardNo) {
		this.boardService.deleteBoard(boardNo);
		return "redirect:/getBoardList";
	}
	//게시글 수정 액션
	@PostMapping("/detailBoardList")
	public String detailBoardUpdate(Model model , Board board) {
		System.out.println(board);
		this.boardService.detailBoardUpdate(board);
			return "redirect:/getBoardList";
		
	}
	//게시글 상세보기
	@GetMapping("/detailBoardList")
	public String detailBoardList(HttpSession session, Model model, @RequestParam(value= "boardNo") int boardNo, @RequestParam(value= "currentPage", defaultValue = "1") int currentPage) {
		if(session.getAttribute("loginMember") == null) {
	         return "redirect:/index";
	      }
		System.out.println(currentPage +"<---controller currentPage");
		int rowPerPage = 5;
		int beginRow = (currentPage-1)*rowPerPage;
		System.out.println(beginRow);
		
		
		Map<String, Object> map = this.boardService.selectBoardOneByComment(boardNo, beginRow, rowPerPage);
		//게시글
		//게시글 출력값
		model.addAttribute("board", map.get("board"));
		//다음 열번호
		model.addAttribute("next", map.get("next"));
		//이전 열 번호
		model.addAttribute("pre", map.get("pre"));
		//현재 로그인한 아이디
		model.addAttribute("member", session.getAttribute("loginMember"));
		//이전 , 다음 비교할 현재 게시글 번호
		model.addAttribute("boardNo", boardNo);
		//테이블 안에 제일 작은 열의 번호
		model.addAttribute("min", map.get("min"));
		//댓글
		//댓글 리스트
		model.addAttribute("list", map.get("list"));
		//마지막 페이지
		model.addAttribute("lastPage", map.get("lastPage"));
		//페이지 시작 번호
		model.addAttribute("currentPage", currentPage);
		return "detailBoardList";
	}
	//게시글 리스트
	@GetMapping("/getBoardList")
	public String getBoardList(HttpSession session, Model model, @RequestParam(value= "currentPage", defaultValue = "1") int currentPage , @RequestParam(value= "boardTitle", defaultValue = "") String boardTitle) {
		if(session.getAttribute("loginMember") == null) {
	         return "redirect:/index";
	      }
		System.out.println(boardTitle +"<-- controller 검색값");
		System.out.println(currentPage +"<---controller currentPage");
		int rowPerPage = 3;
		int beginRow = (currentPage-1)*rowPerPage;
		System.out.println(beginRow);
		
		Map<String, Object> map = this.boardService.selectBoard(beginRow, rowPerPage, boardTitle);
		
		model.addAttribute("list", map.get("list"));
		model.addAttribute("lastPage", map.get("lastPage"));
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("boardTitle", boardTitle);
		
		return "getBoardList";
	}
}
