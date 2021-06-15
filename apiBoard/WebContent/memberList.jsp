<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import ="java.util.*" %>    
<%@ page import ="service.*" %> 
    
<%  ArrayList<MemberVo> alist =  (ArrayList<MemberVo>)request.getAttribute("alist"); %>    
<%
out.println("세션에 담긴 아이디는?");
String member_id = (String)session.getAttribute("S_memberId");
out.println("<a href='./memberLogout.do'>"+member_id+"</a>");


%>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
	<style>
		 body{
        line-height:2em;        
        font-family:"맑은 고딕";
		}	
		* {
		margin: 0 auto;
		padding: 0;
		font-family: 'Malgun gothic','Sans-Serif','Arial';
		}
		a {
		text-decoration: none;
		color:#333;
		}
		</style>
<title>회원 리스트</title>
</head>
<body>
<center><h1>회원리스트 보기</h1></center>
<table border=1 style="text-align:left;width:500px;height:80px">
<tr>
<td>번호</td>
<td>이름</td>
<td>아이디</td>
<td>등록일</td>
</tr>
<%for(MemberVo mv : alist) { %>
<tr>
<td><%=mv.getMidx()%></td>
<td><%=mv.getMemberName() %></td>
<td><%=mv.getMemberId() %></td>
<td><%=mv.getWriteday()%></td>
</tr>
<% } %>

</table>
<a href="<%=request.getContextPath()%>/board/boardList.do">게시판가기</a>
</body>
</html>