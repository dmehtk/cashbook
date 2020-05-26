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
	//게시글 삭제
	@GetMapping("/deleteBoard")
	public String deleteBoard(@RequestParam(value= "boardNo") int boardNo) {
			this.boardService.deleteBoard(boardNo);
		return "redirect:/getBoardList";
	}
	//게시글 수정 액션
	@PostMapping("/detailBoardList")
	public String detailBoardUpdate(Board board) {
		System.out.println(board);
		this.boardService.detailBoardUpdate(board);
		return "redirect:/getBoardList";
	}
	//게시글 상세보기
	@GetMapping("/detailBoardList")
	public String detailBoardList(HttpSession session, Model model, @RequestParam(value= "boardNo") int boardNo) {
		if(session.getAttribute("loginMember") == null) {
	         return "redirect:/index";
	      }
		Map<String, Object> map = this.boardService.selectBoardOne(boardNo);
		model.addAttribute("board", map.get("board"));
		model.addAttribute("totalCount", map.get("totalCount"));
		model.addAttribute("member", session.getAttribute("loginMember"));
		model.addAttribute("boardNo", boardNo);
		model.addAttribute("min", map.get("min"));
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
