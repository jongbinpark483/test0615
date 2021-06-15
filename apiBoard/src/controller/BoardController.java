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
		System.out.println("url�ּ�:"+str);
		
		if (str.equals("/board/boardWrite.do")) {		
			
			RequestDispatcher rd = request.getRequestDispatcher("/boardWrite.jsp");
			rd.forward(request, response);
		
		}else if (str.equals("/board/boardWriteAction.do")) {
			
			//���ε� ���� ���		
			String uploadPath = "D:\\api_dev\\apiBoard\\WebContent\\";
			//String uploadPath = "D:\\dev\\eclipse-workspace\\BOARD\\WebContent\\";
			//���� ����
			String savedPath = "filefolder";
			//����� �� ���
			String saveFullPath = uploadPath + savedPath;
			
			int sizeLimit = 1024*1024*15;
			String fileName = null;
			String originFileName = null;
			
			//MultipartRequest ��ü����
			MultipartRequest multi = new MultipartRequest(request, saveFullPath, sizeLimit, "utf-8", new DefaultFileRenamePolicy()); 
			
			//�����ڿ�  ����Name�Ӽ��� �̸��� ��´�
			Enumeration files = multi.getFileNames();
			//��� ���� ��ü�� Name���� ��´�.
			String file = (String)files.nextElement();
			//����Ǵ� �����̸�
			fileName = multi.getFilesystemName(file); 
			//�������� �̸�
			originFileName = multi.getOriginalFileName(file);
			
			String ThumbnailFileName = null;
					
			try {
				if(fileName != null)
				ThumbnailFileName = makeThumbnail(uploadPath,savedPath, fileName);
			} catch (Exception e) {
				e.printStackTrace();
			}			
			
			//���� �Ѱ� �޴´�
			String subject = multi.getParameter("subject");
			String contents = multi.getParameter("contents");
			String writer = multi.getParameter("writer");
			String password = multi.getParameter("password");
					
		//	String fileName = null;
			String ip = InetAddress.getLocalHost().getHostAddress();
			
			HttpSession session = request.getSession();
			
			int midx = 0;
			if (session.getAttribute("midx") == null) {
				midx = 32; // ���Ǻ����� ���� ������ 32�� �ϵ��ڵ�
			}else {
			 midx  = (int)session.getAttribute("midx");
			}
					
			BoardDao bd = new BoardDao();
			int value = bd.boardInsert(subject, contents, writer, password, fileName, ip, midx);
					
			//���ϰ��� ���� �̵�
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
						
			String keyword = request.getParameter("keyword");	//Ű���� �˻��� ����	
			if (keyword == null) keyword = "";
			
			SearchCriteria scri = new SearchCriteria();   //��ġũ���׸��ƿ� Ű����� �������� ��Ƴ��
			scri.setKeyword(keyword);
			scri.setPage(page2);
			System.out.println("scri"+scri);
			
						
			BoardDao bd = new BoardDao();
			int cnt= bd.boardTotalCount(keyword);  //��ü ����Ʈ ������ ���Ѵ�
			System.out.println("cnt"+cnt);
			
			
			PageMaker pm = new PageMaker();
			pm.setScri(scri);  //����������Ŀ�� ��ġũ���׸��ƴ� ���� ��´�
			pm.setTotalCount(cnt);			//��ü������ �����Ŀ� ������ �׺���̼ǿ� ���۰� ��, ������ ���� �������� ���Ѵ�
			
			
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
			
			//1. ���� �Ѱܹ޴´�
			String bidx = request.getParameter("bidx");
			int bidx2 = Integer.parseInt(bidx);
			
			String password = request.getParameter("password");
			String subject = request.getParameter("subject");
			String contents = request.getParameter("contents");
			String writer = request.getParameter("writer");
			
			//2.ó���Ѵ�
			BoardDao bd = new BoardDao();   // ��ü����
			int value = bd.boardModify(bidx2, password, subject, contents, writer);
			System.out.println("value:"+value);
			
			//3.�̵��Ѵ�
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
			
			//1. �Ѱܹ޴´�
			String bidx = request.getParameter("bidx");
			int bidx2 = Integer.parseInt(bidx);
			String password = request.getParameter("password");
			
			//2.ó���Ѵ�
			BoardDao bd = new BoardDao();
			int value = bd.boardDelete(bidx2, password);			
			
			//3.�̵��Ѵ�
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
			//�Ѿ�°�
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
					
			
			//�亯�ϱ��Ҷ��� �α���
			HttpSession session = request.getSession();
			int midx = (int)session.getAttribute("midx");
			System.out.println("midx"+midx);
			//ó���Ѵ�
			BoardDao bd = new BoardDao();   //��ü����
			bd.boardReply(bidx, originbidx, depth, llevel, subject, contents, writer, password, ip, midx);
						
			//�̵��Ѵ�
			response.sendRedirect(request.getContextPath()+"/board/boardList.do");	
			
		}else if (str.equals("/Board/fileDownload.do")) {
			
			//���ε� ���� ���		
			String uploadPath = "D:\\api_dev\\apiBoard\\WebContent\\";
			//String uploadPath = "D:\\dev\\eclipse-workspace\\BOARD\\WebContent\\";
			//���� ����
			String savedPath = "filefolder";
			//����� �� ���
			String saveFullPath = uploadPath + savedPath;			
			
			//�Ѿ���� �����̸�
			String fileName = request.getParameter("fileName");			
			//������ ��ġ�� ��ü���	
	 		String filePath = saveFullPath + File.separator + fileName;
	 			 	
	 		byte[] b = new byte[4096]; 
			//�ش���ġ�� �����ϴ� ������ ����Ʈ ��Ʈ������ �о�帰��
			FileInputStream fileInputStream = new FileInputStream(filePath); 
		
			//����(����)Ÿ���� üũ�Ͽ� null�̸� ��Ʈ��Ÿ������ �����Ѵ�
		//	String mimeType = getServletContext().getMimeType(filePath); 
			// ���� ��θ� �����Ѵ�  
			 Path source = Paths.get(filePath);
			 //�� ��ο� �ִ� ������ ����Ÿ���� Ȯ������ ���ϸ� null�� ��ȯ
			  String mimeType = Files.probeContentType(source);			
			//�������� �ٷ�� Ȯ���ڸ�
			if(mimeType == null) { 
					// 8��Ʈ�� �� ����Ÿ�� .iniȮ������ ������ �д´�
					mimeType = "application/octet-stream"; 
				} 
			response.setContentType(mimeType); 
			 			
			
	         // ���ϸ� UTF-8�� ���ڵ� 
	         String sEncoding = new String(fileName.getBytes("UTF-8"),"ISO-8859-1"); 
	         response.setHeader("Content-Disposition", "attachment; fileName= " + sEncoding); 
	          
	         //  �������� ����� �Ҷ� ServletOutputStream �߻�Ŭ����
	         ServletOutputStream servletOutStream = response.getOutputStream(); 
	          
	         int read= 0; 
	         //�о�帰 ������ 4 ����Ʈ Ÿ������ ���Ͽ� ����
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
		
		//�ø� �ҽ������� �о�帰��
		BufferedImage sourceImg = ImageIO.read(new File(uploadPath+path+File.separator+fileName));
		//�̹����� ������¡�Ѵ�(���� 100�� ���缭 �����̹��� ������ �����Ѵ�)
		BufferedImage destImg = Scalr.resize(sourceImg,Scalr.Method.AUTOMATIC,Scalr.Mode.FIT_TO_HEIGHT,100);
		//����� Ǯ���
		String thumbnailPath = uploadPath + path + File.separator + "s-"+fileName;
		//���� ��ü����
		File newFile = new File(thumbnailPath);
		//Ȯ���� ����
		String formatName = fileName.substring(fileName.lastIndexOf(".")+1);
		//����� �̹��� �����(������¡�� �̹����� �ش� �̹����������� �ش� ��ġ�� ���� ��ü�����Ѵ�)
		ImageIO.write(destImg, formatName.toUpperCase(), newFile);
		
		//����� ���� �̸� ����
		return thumbnailPath.substring((uploadPath+path).length()).replace(File.separatorChar, ' ');
	}

}
