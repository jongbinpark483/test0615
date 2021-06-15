package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import service.MemberDao;
import service.MemberVo;


@WebServlet("/MemberController")
public class MemberController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	//	response.getWriter().append("Served at: ").append(request.getContextPath());
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=utf-8");
		
		
		String uri = request.getRequestURI();		
		int pnamelength = request.getContextPath().length();
		String str = uri.substring(pnamelength);
		System.out.println("url주소:"+str);
		
		if(str.equals("/memberWriteAction.do")) {
			
			String memberId = request.getParameter("memberId");
			String memberPwd = request.getParameter("memberPwd");
			String memberName = request.getParameter("memberName");
			String memberEmail = request.getParameter("memberEmail");
			String memberJumin = request.getParameter("memberJumin");
			String memberPhone = request.getParameter("memberPhone");
			String memberAddr = request.getParameter("memberAddr");
			String memberGender = request.getParameter("memberGender");
			
			String ip = InetAddress.getLocalHost().getHostAddress();

			//�޼ҵ� ȣ��
			MemberDao md = new MemberDao();
			md.memberInsert(memberName, memberId, memberPwd, memberJumin, memberAddr, memberEmail, memberPhone, memberGender, ip);
					
			response.sendRedirect(request.getContextPath()+"/memberWrite.do");
			
		}else if (str.equals("/memberWrite.do")) {
			
			RequestDispatcher rd = request.getRequestDispatcher("/memberWrite.jsp");
			rd.forward(request, response);				
			
		}else if (str.equals("/memberList.do")) {
			
			MemberDao md = new MemberDao();
			ArrayList<MemberVo> alist  =  md.memberSelectAll();
						
			request.setAttribute("alist", alist);
			
			RequestDispatcher rd = request.getRequestDispatcher("/memberList.jsp");
			rd.forward(request, response);				

			
		}else if (str.equals("/memberLogin.do")) {			
			
			RequestDispatcher rd = request.getRequestDispatcher("/memberLogin.jsp");
			rd.forward(request, response);
	
		}else if (str.equals("/memberLoginAction.do")) {
			
			String memberId  = request.getParameter("memberId");
			String memberPwd  = request.getParameter("memberPwd");
			System.out.println("memberId"+memberId);
			
			MemberDao md = new MemberDao();
			int midx = md.memberLoginCheck(memberId, memberPwd);			
			
			PrintWriter out = response.getWriter();
			if (midx > 0) {  //로그인한 id와 같은 사람이 존재한다면
				//세션변수에 값을 담는다 
			HttpSession session = request.getSession();
			session.setAttribute("S_memberId", memberId);
			session.setAttribute("midx", midx);
				
				out.println("<script>alert('로그인하셨습니다.');document.location.href='./memberList.do';</script>");
				
			}else {
				out.println("<script>alert('잘못된 로그인입니다.');document.location.href='./memberLogin.do';</script>");
			}
			
		}else if (str.equals("/memberLogout.do")) {
			HttpSession session = request.getSession();
			session.invalidate();
			PrintWriter out = response.getWriter();
			out.println("<script>alert('로그아웃 하셨습니다.');document.location.href='"+request.getContextPath()+"/';</script>");
		}		
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		doGet(request, response);
	}

}
