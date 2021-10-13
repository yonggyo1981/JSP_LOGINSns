package com.snslogin;

import java.util.HashMap;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.FilterConfig;

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
	public String getCodeURL() {
		
		StringBuilder sb = new StringBuilder();
		sb.append("https://nid.naver.com/oauth2.0/authorize?");
		sb.append("response_type=code");
		sb.append("&client_id=");
		sb.append(clientId);
		sb.append("&redirect_uri=");
		
		return null;
	}

	@Override
	public String getAccessToken(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAccessToken(String code, String state) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<String, String> getUserProfile(String accessToken) {
		// TODO Auto-generated method stub
		return null;
	}

}
