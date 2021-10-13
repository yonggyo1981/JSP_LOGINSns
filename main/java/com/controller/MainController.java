package com.controller;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import java.io.IOException;

import com.snslogin.*;

public class MainController extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=utf-8");
		
		NaverLogin naver = new NaverLogin();
		String naverCodeUrl = naver.getCodeURL(request);
		request.setAttribute("naverCodeUrl", naverCodeUrl);
		
		RequestDispatcher rd = request.getRequestDispatcher("/main.jsp");
		rd.include(request, response);
	}
}
