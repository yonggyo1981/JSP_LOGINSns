package com.snslogin;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * SNS 로그인 추상 클래스  
 *
 */
public abstract class SocialLogin {
		
	/**
	 * SNS별 인증 code를 발급받는 URL 생성
	 * 
	 * @return
	 */
	public abstract String getCodeURL(HttpServletRequest request);
	
	/**
	 * getCodeURL()로 부터 리다렉트될때 실려온 code, state 값을 통해 
	 * API 접속 할 수 있는 AccessToken 발급 
	 * 
	 * @param request
	 * @return
	 */
	public abstract String getAccessToken(HttpServletRequest request);
	public abstract String getAccessToken(String code, String state);
	
	/**
	 * getAccessToken을 통해서 발급받은 토큰으로 회원 정보 조회
	 * 
	 * @param accessToken
	 * @return
	 */
	public abstract HashMap<String, String> getUserProfile(String accessToken);
}



