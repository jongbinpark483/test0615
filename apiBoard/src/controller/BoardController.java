package controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.imageio.ImageIO;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.imgscalr.Scalr;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import domain.PageMaker;
import domain.SearchCriteria;
import service.BoardDao;
import service.BoardVo;

@WebServlet("/BoardController")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
      
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//response.getWriter().append("Served at: ").append(request.getContextPath());
	
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=utf-8");
		
		
		String uri = request.getRequestURI();		
		int pnamelength = request.getContextPath().length();
		String str = uri.substring(pnamelength);
		System.out.println("url주소:"+str);
		
		if (str.equals("/board/boardWrite.do")) {		
			
			RequestDispatcher rd = request.getRequestDispatcher("/boardWrite.jsp");
			rd.forward(request, response);
		
		}else if (str.equals("/board/boardWriteAction.do")) {
			
			//업로드 파일 경로		
			String uploadPath = "D:\\api_dev\\apiBoard\\WebContent\\";
			//String uploadPath = "D:\\dev\\eclipse-workspace\\BOARD\\WebContent\\";
			//저장 폴더
			String savedPath = "filefolder";
			//저장된 총 경로
			String saveFullPath = uploadPath + savedPath;
			
			int sizeLimit = 1024*1024*15;
			String fileName = null;
			String originFileName = null;
			
			//MultipartRequest 객체생성
			MultipartRequest multi = new MultipartRequest(request, saveFullPath, sizeLimit, "utf-8", new DefaultFileRenamePolicy()); 
			
			//열거자에  파일Name속성의 이름을 담는다
			Enumeration files = multi.getFileNames();
			//담긴 파일 객체의 Name값을 담는다.
			String file = (String)files.nextElement();
			//저장되는 파일이름
			fileName = multi.getFilesystemName(file); 
			//원래파일 이름
			originFileName = multi.getOriginalFileName(file);
			
			String ThumbnailFileName = null;
					
			try {
				if(fileName != null)
				ThumbnailFileName = makeThumbnail(uploadPath,savedPath, fileName);
			} catch (Exception e) {
				e.printStackTrace();
			}			
			
			//값을 넘겨 받는다
			String subject = multi.getParameter("subject");
			String contents = multi.getParameter("contents");
			String writer = multi.getParameter("writer");
			String password = multi.getParameter("password");
					
		//	String fileName = null;
			String ip = InetAddress.getLocalHost().getHostAddress();
			
			HttpSession session = request.getSession();
			
			int midx = 0;
			if (session.getAttribute("midx") == null) {
				midx = 32; // 세션변수에 값이 없을때 32로 하드코딩
			}else {
			 midx  = (int)session.getAttribute("midx");
			}
					
			BoardDao bd = new BoardDao();
			int value = bd.boardInsert(subject, contents, writer, password, fileName, ip, midx);
					
			//리턴값에 따른 이동
			if (value >0) {				
			response.sendRedirect(request.getContextPath()+"/board/boardList.do");			
			}else {
			response.sendRedirect(request.getContextPath()+"/board/boardWrite.do");	
			}
			
		}else if (str.equals("/board/boardList.do")) {
						
			String page = request.getParameter("page");
			if (page == null) page= "1";
			int page2 = Integer.parseInt(page);
			System.out.println("page2"+page2);
						
			String keyword = request.getParameter("keyword");	//키워드 검색을 위해	
			if (keyword == null) keyword = "";
			
			SearchCriteria scri = new SearchCriteria();   //서치크리테리아에 키워드와 페이지를 담아논다
			scri.setKeyword(keyword);
			scri.setPage(page2);
			System.out.println("scri"+scri);
			
						
			BoardDao bd = new BoardDao();
			int cnt= bd.boardTotalCount(keyword);  //전체 리스트 갯수를 구한다
			System.out.println("cnt"+cnt);
			
			
			PageMaker pm = new PageMaker();
			pm.setScri(scri);  //페이지메이커에 서치크리테리아는 먼저 담는다
			pm.setTotalCount(cnt);			//전체갯수를 담은후에 페이지 네비게이션에 시작과 끝, 이전과 다음 페이지를 정한다
			
			
			System.out.println("pm"+pm);
			
			ArrayList<BoardVo> alist  =  bd.boardSelectAll(page2, keyword);
						
			request.setAttribute("alist", alist);	
			request.setAttribute("pm", pm);			
			
			RequestDispatcher rd = request.getRequestDispatcher("/boardList.jsp");
			rd.forward(request, response);	
			
		}else if (str.equals("/board/boardContents.do")) {
			
			String bidx = request.getParameter("bidx");
			int bidx2 = Integer.parseInt(bidx);
			
			BoardDao bd  = new BoardDao();
			BoardVo bv = bd.boardSelectOne(bidx2);
			bd.boardViewCount(bidx2);			
			
			request.setAttribute("bv", bv);
			RequestDispatcher rd = request.getRequestDispatcher("/boardContents.jsp");
			rd.forward(request, response);	
			
		}else if (str.equals("/board/boardModify.do")) {
			
			String bidx = request.getParameter("bidx");
			int bidx2 = Integer.parseInt(bidx);
			
			BoardDao bd  = new BoardDao();
			BoardVo bv = bd.boardSelectOne(bidx2);					
			
			request.setAttribute("bv", bv);
			RequestDispatcher rd = request.getRequestDispatcher("/boardModify.jsp");
			rd.forward(request, response);	
			
			
		}else if (str.equals("/board/boardModifyAction.do")) {
			
			//1. 값을 넘겨받는다
			String bidx = request.getParameter("bidx");
			int bidx2 = Integer.parseInt(bidx);
			
			String password = request.getParameter("password");
			String subject = request.getParameter("subject");
			String contents = request.getParameter("contents");
			String writer = request.getParameter("writer");
			
			//2.처리한다
			BoardDao bd = new BoardDao();   // 객체생성
			int value = bd.boardModify(bidx2, password, subject, contents, writer);
			System.out.println("value:"+value);
			
			//3.이동한다
			if (value >0)			
			response.sendRedirect(request.getContextPath()+"/board/boardContents.do?bidx="+bidx);	
			else {
			response.sendRedirect(request.getContextPath()+"/board/boardModify.do?bidx="+bidx);		
			}
		
		}else if (str.equals("/board/boardDelete.do")) {
			
			String bidx = request.getParameter("bidx");
			int bidx2 = Integer.parseInt(bidx);
			
			BoardDao bd  = new BoardDao();
			BoardVo bv = bd.boardSelectOne(bidx2);					
			
			request.setAttribute("bv", bv);			
			
			RequestDispatcher rd = request.getRequestDispatcher("/boardDelete.jsp");
			rd.forward(request, response);
			
		}else if (str.equals("/board/boardDeleteAction.do")) {
			
			//1. 넘겨받는다
			String bidx = request.getParameter("bidx");
			int bidx2 = Integer.parseInt(bidx);
			String password = request.getParameter("password");
			
			//2.처리한다
			BoardDao bd = new BoardDao();
			int value = bd.boardDelete(bidx2, password);			
			
			//3.이동한다
			if (value > 0) {  
			response.sendRedirect(request.getContextPath()+"/board/boardList.do");	
			}else {
			response.sendRedirect(request.getContextPath()+"/board/boardDelete.do?bidx="+bidx);					
			}
			
		}else if (str.equals("/board/boardReply.do")) {
			String bidx = request.getParameter("bidx");
			String originbidx = request.getParameter("originbidx");
			String depth = request.getParameter("depth");
			String llevel = request.getParameter("llevel");
			
			request.setAttribute("bidx", bidx);
			request.setAttribute("originbidx", originbidx);
			request.setAttribute("depth", depth);
			request.setAttribute("llevel", llevel);			
			
			RequestDispatcher rd = request.getRequestDispatcher("/boardReply.jsp");
			rd.forward(request, response);
			
		}else if (str.equals("/board/boardReplyAction.do")) {
			//넘어온값
			int bidx = Integer.parseInt(request.getParameter("bidx"));
			int originbidx = Integer.parseInt(request.getParameter("originbidx"));
			int depth = Integer.parseInt(request.getParameter("depth"));
			int llevel = Integer.parseInt(request.getParameter("llevel"));
			
			String subject = request.getParameter("subject");
			String contents = request.getParameter("contents");
			String writer = request.getParameter("writer");
			String password = request.getParameter("password");
			String ip = InetAddress.getLocalHost().getHostAddress();
			
			System.out.println("bidx"+bidx);
			System.out.println("originbidx"+originbidx);
			System.out.println("depth"+depth);
			System.out.println("llevel"+llevel);
			System.out.println("subject"+subject);
			System.out.println("contents"+contents);
			System.out.println("writer"+writer);
			System.out.println("password"+password);
					
			
			//답변하기할때는 로그인
			HttpSession session = request.getSession();
			int midx = (int)session.getAttribute("midx");
			System.out.println("midx"+midx);
			//처리한다
			BoardDao bd = new BoardDao();   //객체생성
			bd.boardReply(bidx, originbidx, depth, llevel, subject, contents, writer, password, ip, midx);
						
			//이동한다
			response.sendRedirect(request.getContextPath()+"/board/boardList.do");	
			
		}else if (str.equals("/Board/fileDownload.do")) {
			
			//업로드 파일 경로		
			String uploadPath = "D:\\api_dev\\apiBoard\\WebContent\\";
			//String uploadPath = "D:\\dev\\eclipse-workspace\\BOARD\\WebContent\\";
			//저장 폴더
			String savedPath = "filefolder";
			//저장된 총 경로
			String saveFullPath = uploadPath + savedPath;			
			
			//넘어오는 파일이름
			String fileName = request.getParameter("fileName");			
			//파일의 위치한 전체경로	
	 		String filePath = saveFullPath + File.separator + fileName;
	 			 	
	 		byte[] b = new byte[4096]; 
			//해당위치에 존재하는 파일을 바이트 스트림으로 읽어드린다
			FileInputStream fileInputStream = new FileInputStream(filePath); 
		
			//마임(파일)타입을 체크하여 null이면 스트림타입으로 지정한다
		//	String mimeType = getServletContext().getMimeType(filePath); 
			// 절대 경로를 정의한다  
			 Path source = Paths.get(filePath);
			 //그 경로에 있는 파일의 마임타입을 확인하지 못하면 null을 반환
			  String mimeType = Files.probeContentType(source);			
			//서버에서 다루는 확장자명
			if(mimeType == null) { 
					// 8비트로 된 데이타로 .ini확장자의 파일을 읽는다
					mimeType = "application/octet-stream"; 
				} 
			response.setContentType(mimeType); 
			 			
			
	         // 파일명 UTF-8로 인코딩 
	         String sEncoding = new String(fileName.getBytes("UTF-8"),"ISO-8859-1"); 
	         response.setHeader("Content-Disposition", "attachment; fileName= " + sEncoding); 
	          
	         //  브라우저에 출력을 할때 ServletOutputStream 추상클래스
	         ServletOutputStream servletOutStream = response.getOutputStream(); 
	          
	         int read= 0; 
	         //읽어드린 파일을 4 바이트 타입으로 파일에 쓴다
	         while((read = fileInputStream.read(b,0,b.length))!= -1){ 
	             servletOutStream.write(b,0,read);             
	         } 
	          
	         servletOutStream.flush(); 
	         servletOutStream.close(); 
	         fileInputStream.close(); 			 	 
				
		}		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private static String makeThumbnail(String uploadPath,String path,String fileName) throws Exception{
		
		//올린 소스파일을 읽어드린다
		BufferedImage sourceImg = ImageIO.read(new File(uploadPath+path+File.separator+fileName));
		//이미지를 리사이징한다(높이 100에 맞춰서 원본이미지 비율을 유지한다)
		BufferedImage destImg = Scalr.resize(sourceImg,Scalr.Method.AUTOMATIC,Scalr.Mode.FIT_TO_HEIGHT,100);
		//썸네일 풀경로
		String thumbnailPath = uploadPath + path + File.separator + "s-"+fileName;
		//파일 객체생성
		File newFile = new File(thumbnailPath);
		//확장자 추출
		String formatName = fileName.substring(fileName.lastIndexOf(".")+1);
		//썸네일 이미지 만들기(리사이징한 이미지를 해당 이미지형식으로 해당 위치에 파일 객체생성한다)
		ImageIO.write(destImg, formatName.toUpperCase(), newFile);
		
		//썸네일 파일 이름 추출
		return thumbnailPath.substring((uploadPath+path).length()).replace(File.separatorChar, ' ');
	}

}
