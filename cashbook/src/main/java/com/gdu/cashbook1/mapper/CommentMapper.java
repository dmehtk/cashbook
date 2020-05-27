package com.gdu.cashbook1.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.cashbook1.vo.Comment;

@Mapper
public interface CommentMapper {
	//댓글 리스트
	public List<Comment> selectCommentList(Map<String, Object> map);
	//댓글의 총갯수
	public int selectCommentListTotal(int boardNo);
	//댓글 추가
	public int insertComment(Comment comment);
	//댓글 수정 폼
	public Comment selectCommentOne(int commentNo);
	//댯굴 수정 액션
	public int updateComment(Comment comment);
	//댓글 삭제 
	public int deleteComment(int commentNo);
	//게시판 삭제시 댓글 삭제
	public int deleteCommentByBoard(int boardNo);
	//아이디 삭제시 댓글 삭제
	public int deleteCommentById(String memberId);
}
