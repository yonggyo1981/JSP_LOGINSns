package com.controller;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;

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
			naver.getUserProfile(accessToken);
			
		} catch (Exception e) {
			out.printf("<script>alert('%s');</script>", e.getMessage());
		}
	}
}
