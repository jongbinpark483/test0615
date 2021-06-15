<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import ="java.util.*" %>    
<%@ page import ="service.*" %> 
<%@ page import ="domain.*" %>    
 <%  
 ArrayList<BoardVo> alist =  (ArrayList<BoardVo>)request.getAttribute("alist"); 
 PageMaker pm = (PageMaker)request.getAttribute("pm");
 %>      
   
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판 리스트 보기</title>
	 <style>
		div{
			font-size:25px; 
			font-weight:bold; 
			text-decoration:underline; 
			padding:10px;
			text-align:center;
		}
	</style>
</head>
<body>
<div>게시판 리스트 보기</div>

<form name='frm' action='<%=request.getContextPath()%>/board/boardList.do' method='post'>
<table border ="0" width="800px">
<tr>	
<td width="750px" align="right">
<input type='text' name='keyword' size=10>
</td>
<td>
<input type='submit' name='submit' value='검색' />
</td>
</tr>	
</table>
</form>

<table border=1 style="text-align:left;width:800px;height:80px">
<tr>
<td>번호</td>
<td>제목</td>
<td>작성자</td>
<td>작성일</td>
</tr>
<%for (BoardVo bv : alist) { %>
<tr>
<td><%=bv.getBidx()%></td>

<td>
<% for(int i =1;i<=bv.getLlevel();i++){
	out.print("&nbsp;&nbsp;");
		if (i == bv.getLlevel()){
			out.print("ㄴ");
		}	
	}
%>
<a href="<%=request.getContextPath()%>/board/boardContents.do?bidx=<%=bv.getBidx()%>"><%=bv.getSubject()%></a>

</td>
<td><%=bv.getWriter()%></td>
<td><%=bv.getWriteday()%></td>
</tr>
<% } %>

</table>

<table border=0 style="text-align:center;width:800px;height:80px">
	<tr>
		<td width="200px">
		<% if (pm.isPrev() == true) {%>
		<a href="<%=request.getContextPath()%>/board/boardList.do?page=<%=pm.getStartPage()-1%>&keyword=<%=pm.encoding(pm.getScri().getKeyword())%>">◀</a>
		<%} %>
		</td>
		<td>
		<%
		for(int i = pm.getStartPage(); i<=pm.getEndPage(); i++){
		%>	
		&nbsp;&nbsp;<a href="<%=request.getContextPath()%>/board/boardList.do?page=<%=i%>&keyword=<%=pm.encoding(pm.getScri().getKeyword())%>"><%=i%></a>	
		<%
		}
		%>
		</td>
		<td width="200px">
		<%if (pm.isNext() == true){ %>
		<a href="<%=request.getContextPath()%>/board/boardList.do?page=<%=pm.getEndPage()+1%>&keyword=<%=pm.encoding(pm.getScri().getKeyword())%>">▶</a>
		<%} %>
		</td>
	</tr>
</table>


<br>
<a href="<%=request.getContextPath()%>/board/boardWrite.do">글쓰기</a>

</body>
</html>