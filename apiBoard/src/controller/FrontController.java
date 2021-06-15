package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/FrontController")
public class FrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		
		String uri = request.getRequestURI();
		int p_length= request.getContextPath().length();
		String str = uri.substring(p_length);    //������Ʈ�̸��� �� �� �ּҰ��
		
	//	System.out.println("str:"+str);
	
		//     /board/boardWrite.do
		
		String[] gubun  = str.split("/");
		String str2 = gubun[1];
	//	System.out.println("str2:"+str2);
		
		//�Խ��� �������̸�
		if (str2.equals("board")) {					
			BoardController bc = new BoardController();
			bc.doGet(request, response);			
		}else {
			MemberController mc = new MemberController();
			mc.doGet(request, response);
		}			
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
