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
			if (userInfo == null) { 
				throw new Exception("네이버 로그인 실패!");
			} else {
				/** 
				 * 네이버쪽으로 회원 프로필 정보를 받아오면
				 * 1. 이미 소셜 로그인 형태로 가입 되어 있는지 여부 체크 
				 * 2. 가입되어 있으면 -> 바로 로그인 
				 * 3. 가입되어 있지 않으면 -> 소셜 형태로 회원 가입페이지 이동  
				 */
				if (naver.isJoin(userInfo, request)) { // 이미 가입 
					// 로그인 처리 
					
				} else { // 미 가입 
					// 가입 처리 페이지로 이동
				}
				
			}
		
		} catch (Exception e) {
			out.printf("<script>alert('%s');location.href='../member/login';</script>", e.getMessage());
		}
	}
}
