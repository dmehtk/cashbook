package com.gdu.cashbook1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gdu.cashbook1.mapper.CommentMapper;
import com.gdu.cashbook1.vo.Comment;

@Service
@Transactional
public class CommentService {

	@Autowired
	private CommentMapper commentMapper;
	
	public int deleteComment(int commentNo) {
		return this.commentMapper.deleteComment(commentNo);
	}
	
	public int updateComment(Comment comment) {
		return this.commentMapper.updateComment(comment);
	}
	public int insertComment(Comment comment) {
		return this.commentMapper.insertComment(comment);
	}
	public Comment selectCommentOne(int commentNo) {
		return this.commentMapper.selectCommentOne(commentNo);
	}
}
