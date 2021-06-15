<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
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
 	<div>게시판</div>
 

<br>
<a href="<%=request.getContextPath()%>/memberWrite.do">회원가입 페이지 가기</a><br>
<a href="<%=request.getContextPath()%>/memberList.do">회원 리스트 가기</a><br>
<a href="<%=request.getContextPath()%>/memberLogin.do">회원 로그인</a><br>

<a href="<%=request.getContextPath()%>/board/boardList.do">게시판 리스트가기</a><br>
<a href="<%=request.getContextPath()%>/board/boardWrite.do">게시판 글쓰기</a><br>


</body>
</html>