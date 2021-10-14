package com.controller.member;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;

import com.models.dao.MemberDao;
import com.models.dto.Member;
import com.exception.AlertException;
import com.snslogin.*;

/**
 * 회원 가입 
 * GET - 가입 양식, POST - 가입 처리 
 */
public class JoinController extends HttpServlet {
	
	/** 회원가입 양식 */
	@Override 
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=utf-8");
		
		NaverLogin naver = new NaverLogin();
		Member member = naver.getSocialUserInfo(request);
		if (member != null) { // 네이버 로그인으로 회원 가입 유입된 경우 
			request.setAttribute("isSocialJoin", true);
			request.setAttribute("member", member);
		}
		
		
		RequestDispatcher rd = request.getRequestDispatcher("/member/form.jsp");
		rd.include(request, response);
	}
	
	/** 회원가입 처리 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		try {
			MemberDao dao = new MemberDao();
			boolean result = dao.join(request);
			if (!result) { // 회원가입 실패 
				throw new AlertException("회원가입 실패!");
			}	
			out.print("<script>parent.location.href='login';</script>");
		} catch (AlertException e) {
			out.print("<script>alert('" + e.getMessage() + "');</script>");
			return;
		}
	}
}