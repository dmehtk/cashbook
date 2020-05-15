package com.gdu.cashbook1.vo;

public class Memberid {
	private int memberNo;
	private String memberId;
	public int getMemberNo() {
		return memberNo;
	}
	public void setMemberNo(int memberNo) {
		this.memberNo = memberNo;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	@Override
	public String toString() {
		return "Memberid [memberNo=" + memberNo + ", memberId=" + memberId + "]";
	}
	
	
}
