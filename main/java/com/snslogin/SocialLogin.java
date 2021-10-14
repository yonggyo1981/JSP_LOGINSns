package com.snslogin;

import javax.servlet.http.HttpServletRequest;
import java.net.*;
import java.io.*;
import java.util.*;

import com.exception.*;
import com.models.dto.*;

import org.json.simple.parser.*;
import org.json.simple.*;

/**
 * SNS 로그인 추상 클래스  
 *
 */
public abstract class SocialLogin {
	
	/**
	 * Social 채널별 UserInfo 세션을 비우기
	 * 
	 * @param request
	 */
	public abstract void clearSession(HttpServletRequest request);
	
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
	public abstract String getAccessToken(HttpServletRequest request) throws SocialLoginException, IOException, ParseException;
	public abstract String getAccessToken(HttpServletRequest request, String code, String state) throws SocialLoginException, IOException, ParseException;
	
	/**
	 * getAccessToken을 통해서 발급받은 토큰으로 회원 정보 조회
	 * 
	 * @param accessToken
	 * @return
	 */
	public abstract HashMap<String, String> getUserProfile(String accessToken);
	
	/**
	 * 소셜 로그인 형태로 이미 가입이 되어 있는지 여부 체크 
	 * 		- socialChannel 과 socialId로 체크
	 * 		- 이미 가입 되어 있는 경우 -> 바로 로그인 처리
	 *      - 가입이 안되어 있는 경우 -> 회원 가입 페이지(소셜 로그인 형태)
	 * @param userInfo
	 * @param request - userInfo 정보를 쉽게 접근하기 위해서 세션에 담아서 처리 
	 * @return
	 */
	public abstract boolean isJoin(HashMap<String, String> userInfo, HttpServletRequest request);
	
	/**
	 * 이미 가입된 소셜 로그인 회원인 경우 
	 * 세션에 저장된 userInfo를 통해서 로그인 처리 
	 * 
	 * @param request
	 * @return
	 */
	public abstract boolean login(HttpServletRequest request);
	
	/**
	 * 세션에 저장되어 있는 각 채널별 userInfo 값을 Member 클래스 형태로 반환
	 * 
	 * @param request
	 * @return
	 */
	public abstract Member getSocialUserInfo(HttpServletRequest request);
	
	/**
	 * Http 소켓을 통해서 응답 데이터 가져오는 메서드
	 * 
	 * @param apiUrl
	 * @return JSONObject 
	 * @throws IOException, ParseException 
	 */
	public JSONObject httpRequest(String apiUrl) throws IOException, ParseException {
		return httpRequest(apiUrl, null);
	}
	
	/**
	 * Http 소켓을 통해서 응답 데이터 가져오는 메서드
	 * 
	 * @param apiUrl 
	 * @param headers - 요청 헤더 
	 * @return JSONObject
	 * @throws IOException
	 * @throws ParseException
	 */
	public JSONObject httpRequest(String apiUrl, HashMap<String,String> headers) throws IOException, ParseException {
		URL url = new URL(apiUrl);
		
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setRequestMethod("GET");
		
		/** 요청 헤더 추가 S */
		if (headers != null) {
			Iterator<String> ir = headers.keySet().iterator();
			while(ir.hasNext()) {
				String key = ir.next();
				String value = headers.get(key);
				conn.setRequestProperty(key, value);
			}
		}
		/** 요청 헤더 추가 E */
		
		int statusCode = conn.getResponseCode();
		
		// getInputStream(), getErrorStream()
		InputStream in; 
		if (statusCode == HttpURLConnection.HTTP_OK) {
			in = conn.getInputStream();
		} else { // 상태코드가 200이 아닌 경우 
			in = conn.getErrorStream();
		}
		
		InputStreamReader isr = new InputStreamReader(in);
		BufferedReader br = new BufferedReader(isr);
		
		StringBuilder sb = new StringBuilder();
		String line;
		while((line = br.readLine()) != null) {
			sb.append(line);
		}
		
		br.close();
		isr.close();
		in.close();
		
		
		
		JSONObject json = (JSONObject)new JSONParser().parse(sb.toString());
		
		return json;
	}
}



