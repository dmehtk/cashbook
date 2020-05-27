package com.gdu.cashbook1.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gdu.cashbook1.service.CommentService;
import com.gdu.cashbook1.vo.Comment;
import com.gdu.cashbook1.vo.LoginMember;

@Controller
public class CommetController {

	@Autowired
	private CommentService commentService;
	
	@PostMapping("/insertComment")
	public String insertComment(HttpSession session, Comment comment) {
		if(session.getAttribute("loginMember") == null) {
	         return "redirect:/index";
	      }
		System.out.println(comment.getBoardNo());
		System.out.println(comment.getCommentContent());
		String memberId = ((LoginMember)session.getAttribute("loginMember")).getMemberId();
		comment.setMemberId(memberId);
		System.out.println(comment);
		this.commentService.insertComment(comment);
		
		return "redirect:/detailBoardList?boardNo="+comment.getBoardNo();
	}
	
	@GetMapping("/updateComment")
	public String updateComment(HttpSession session, Model model , @RequestParam(value= "commentNo") int commentNo) {
		if(session.getAttribute("loginMember") == null) {
	         return "redirect:/index";
	      }
		Comment comment = this.commentService.selectCommentOne(commentNo);
		model.addAttribute("comment", comment);
		return "updateComment";
	}
	@PostMapping("/updateComment")
	public String updateComment(Comment comment) {
		System.out.println(comment);
		this.commentService.updateComment(comment);
		return "redirect:/detailBoardList?boardNo="+comment.getBoardNo();
	}
	@GetMapping("/deleteComment")
	public String deleteComment(@RequestParam(value= "commentNo") int commentNo, @RequestParam(value= "boardNo") int boardNo) {
		this.commentService.deleteComment(commentNo);
		return "redirect:/detailBoardList?boardNo="+boardNo;
	}
	
	
	
	
	
}
