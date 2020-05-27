package com.gdu.cashbook1.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.cashbook1.vo.Board;

@Mapper
public interface BoardMapper {
	//게시글 전체 삭제(id)
	public int deleteBoardById(String memberId);
	//게시글 추가
	public int insertBoard(Board board);
	//이전글 번호
	public int preNumberSelect(int boardNo);
	//다음글 번호
	public int nextNumberSelect(int boardNo);
	//게시글 삭제
	public int deleteBoard(int boardNo);
	//게시글 업데이트
	public int updateBoard(Board board);
	//게시글 최소 번호
	public int minBoardOne();
	//게시글 리스트 (페이징 , 검색)
	public List<Board> selectBoard(int beginRow, int rowPerPage, String boardTitle);
	//게시글 수
	public int totalBoard();
	//게시글 수(검색값)
	public int totalBoardByTitle(String boardTitle);
	//게시글 상세보기
	public Board selectBoardOne(int boardNo);
}
