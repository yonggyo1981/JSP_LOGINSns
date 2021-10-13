package com.snslogin;

import java.util.HashMap;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpSession;

import com.exception.*;

/**
 * 네이버 아이디로 로그인 
 *
 */
public class NaverLogin extends SocialLogin {
	
	private static String clientId; // 네이버에서 발급받은 Client ID
	private static String clientSecret; // 네이버에서 발급받은 Secret 
	private static String callbackUrl; // 네이버에 앱에 등록한 Callback URL 
	
	/**
	 * 네이버 로그인 계정 설정 
	 * 
	 * @param clientId
	 * @param clientSecret
	 * @param callbackUrl
	 * @throws unsupportedencodingexception 
	 */
	public static void init(String clientId, String clientSecret, String callbackUrl) throws UnsupportedEncodingException {
		NaverLogin.clientId = clientId;
		NaverLogin.clientSecret = clientSecret;
		NaverLogin.callbackUrl = URLEncoder.encode(callbackUrl, "UTF-8");
	}
	
	public static void init(FilterConfig config) throws UnsupportedEncodingException {
		init(
			config.getInitParameter("NaverClientId"),
			config.getInitParameter("NaverClientSecret"),
			config.getInitParameter("NaverCallbackUrl")
		);
	}
	
	@Override
	public String getCodeURL(HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		long state = System.currentTimeMillis();
		session.setAttribute("state", state);
		
		StringBuilder sb = new StringBuilder();
		sb.append("https://nid.naver.com/oauth2.0/authorize?");
		sb.append("response_type=code");
		sb.append("&client_id=");
		sb.append(clientId);
		sb.append("&redirect_uri=");
		sb.append(callbackUrl);
		sb.append("&state=");
		sb.append(state);
		
		return sb.toString();
	}

	@Override
	public String getAccessToken(HttpServletRequest request) throws SocialLoginException {
		// TODO Auto-generated method stub
		String code = request.getParameter("code");
		String state = request.getParameter("state");
		return getAccessToken(request, code, state);
	}

	@Override
	public String getAccessToken(HttpServletRequest request, String code, String state) throws SocialLoginException {
		/** 데이터 변조 체크 - state 값 S */ 
		HttpSession session = request.getSession();
		String _state = String.valueOf((Long)session.getAttribute("state"));
		if (!state.equals(_state)) {
			throw new SocialLoginException("데이터가 변조되었습니다.");
		}
		/** 데이터 변조 체크 - state 값 E */ 
		
		/** 요청 URL 생성 S */
		StringBuilder sb = new StringBuilder();
		sb.append("https://nid.naver.com/oauth2.0/token?");
		sb.append("grant_type=authorization_code");
		sb.append("&client_id=");
		sb.append(clientId);
		sb.append("&client_secret=");
		sb.append(clientSecret);
		sb.append("&code=");
		sb.append(code);
		sb.append("&state=");
		sb.append(state);
		/** 요청 URL 생성 E */
		
		String apiURL = sb.toString();
		
		return null;
	}

	@Override
	public HashMap<String, String> getUserProfile(String accessToken) {
		// TODO Auto-generated method stub
		return null;
	}

}
