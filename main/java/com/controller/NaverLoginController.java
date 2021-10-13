package com.controller;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import com.snslogin.*;
import com.exception.*;

/**
 * 네이버 로그인 Callback URL
 *
 */
public class NaverLoginController extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		NaverLogin naver = new NaverLogin();
		try {
			String accessToken = naver.getAccessToken(request);
			HashMap<String, String> userInfo = naver.getUserProfile(accessToken);
			Iterator<String> ir = userInfo.keySet().iterator();
			/*out.print(userInfo);
			
			while(ir.hasNext()) {
				String key = ir.next();
				String value = userInfo.get(key);
				out.printf("%s = %s<br>", key, value);
			}
			*/
			
		} catch (Exception e) {
			out.printf("<script>alert('%s');</script>", e.getMessage());
		}
	}
}
