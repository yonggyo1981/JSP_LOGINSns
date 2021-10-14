package com.snslogin;

import java.util.HashMap;
import java.util.Iterator;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.io.IOException;
import java.sql.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpSession;

import org.json.simple.parser.*;
import org.json.simple.*;

import com.core.DB;
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
	public String getAccessToken(HttpServletRequest request) throws SocialLoginException, IOException, ParseException {
		// TODO Auto-generated method stub
		String code = request.getParameter("code");
		String state = request.getParameter("state");
		return getAccessToken(request, code, state);
	}

	@Override
	public String getAccessToken(HttpServletRequest request, String code, String state) throws SocialLoginException, IOException, ParseException  {
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
		JSONObject result = httpRequest(apiURL);
		String accessToken = null;
		if (result.containsKey("access_token")) {
			accessToken = (String)result.get("access_token");
		}
		
		return accessToken;
	}

	@Override
	public HashMap<String, String> getUserProfile(String accessToken) {
		//  Authorization : Bearer accessToken 
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Authorization", "Bearer " + accessToken);
		String apiURL = "https://openapi.naver.com/v1/nid/me";
		
		HashMap<String, String> userInfo = null;
		try {
			JSONObject result = httpRequest(apiURL, headers);
			String resultcode = (String)result.get("resultcode");
			if (resultcode.equals("00")) {
				userInfo = new HashMap<String, String>();
				JSONObject response = (JSONObject)result.get("response");
				Iterator<String> ir = response.keySet().iterator();
				while(ir.hasNext()) {
					String key = ir.next();
					String value = (String)response.get(key);
					userInfo.put(key, value);
				}
			}
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		
		return userInfo;
	}

	@Override
	public boolean isJoin(HashMap<String, String> userInfo, HttpServletRequest request) {
		if (userInfo == null)
			return false;
		
		/** 
		 * userInfo를 회원 가입 페이지등 여러 페이지에 유지하기 위해서 
		 * 회원 가입시 -> 소셜 로그인인 경우 비밀번호 체크 제외 
		 * 세션에 담아서 처리 
		 */
		HttpSession session = request.getSession();
		session.setAttribute("naverUserInfo", userInfo);
		
		String id = userInfo.get("id");
		String channel = "Naver";
		
		String sql = "SELECT COUNT(*) cnt FROM member WHERE socialChannel = ? AND socialId = ?";
		try (Connection conn = DB.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, channel);
			pstmt.setString(2, id);
			
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				int cnt = rs.getInt("cnt"); // 1 이상 -> 이미 가입된 회원
				if (cnt > 0) {
					return true;
				}
			}
			
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return false;
	}
}





