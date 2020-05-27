package com.gdu.cashbook1.vo;

public class LoginMember { // Member를 접근 
	private String memberId;
	private String memberPw;
	private String memberPic;
	private int memberLevel;
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getMemberPw() {
		return memberPw;
	}
	public void setMemberPw(String memberPw) {
		this.memberPw = memberPw;
	}
	public String getMemberPic() {
		return memberPic;
	}
	public void setMemberPic(String memberPic) {
		this.memberPic = memberPic;
	}
	
	public int getMemberLevel() {
		return memberLevel;
	}
	public void setMemberLevel(int memberLevel) {
		this.memberLevel = memberLevel;
	}
	@Override
	public String toString() {
		return "LoginMember [memberId=" + memberId + ", memberPw=" + memberPw + ", memberPic=" + memberPic
				+ ", memberLevel=" + memberLevel + "]";
	}
	
	
}
