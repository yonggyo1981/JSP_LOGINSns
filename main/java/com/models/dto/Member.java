package com.models.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Member {
	
	private int memNo; // 회원번호
	private String memId; // 회원 아이디
	private String memPw; // 회원 비밀번호
	private String memNm; // 회원명
	private String socialChannel; // 소셜 로그인 채널(none - 일반, Naver - 네이버, Kakao - 카카오)
	private String socialId; // 소셜 로그인 채널에서 전달된 id(유일한 값)
	private String regDt; // 가입일
	
	public Member() {}
	
	public Member(ResultSet rs) throws SQLException {
		this(
			rs.getInt("memNo"),
			rs.getString("memId"),
			rs.getString("memPw"),
			rs.getString("memNm"),
			rs.getString("socialChannel"),
			rs.getString("socialId"),
			rs.getString("regDt")
		);
	}
	
	public Member(int memNo, String memId, String memPw, String memNm,  String socialChannel, String socialId, String regDt) {
		this.memNo = memNo;
		this.memId = memId;
		this.memPw = memPw;
		this.memNm = memNm;
		this.socialChannel = socialChannel;
		this.socialId = socialId;
		this.regDt = regDt;
	}
	
	public int getMemNo() {
		return memNo;
	}
	
	public void setMemNo(int memNo) {
		this.memNo = memNo;
	}
	
	public String getMemId() {
		return memId;
	}
	
	public void setMemId(String memId) {
		this.memId = memId;
	}
	
	public String getMemPw() {
		return memPw;
	}
	
	public void setMemPw(String memPw) {
		this.memPw = memPw;
	}
	
	public String getMemNm() {
		return memNm;
	}
	
	public void setMemNm(String memNm) {
		this.memNm = memNm;
	}
	
	public String getSocialChannel() {
		return socialChannel;
	}

	public void setSocialChannel(String socialChannel) {
		this.socialChannel = socialChannel;
	}

	public String getSocialId() {
		return socialId;
	}

	public void setSocialId(String socialId) {
		this.socialId = socialId;
	}

	public String getRegDt() {
		return regDt;
	}
	
	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}	
}