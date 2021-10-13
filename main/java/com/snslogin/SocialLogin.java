package com.snslogin;

import javax.servlet.http.HttpServletRequest;
import java.net.*;
import java.io.*;
import java.util.*;

import com.exception.*;

import org.json.simple.parser.*;
import org.json.simple.*;

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



