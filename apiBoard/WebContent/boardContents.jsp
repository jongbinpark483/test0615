<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "service.BoardVo" %>    
<% BoardVo bv = (BoardVo)request.getAttribute("bv"); %>    
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h1>contents 내용보기</h1>
<table border="1" style="text-align:left;width:800px;height:300px">
<tr>
<td style="width:100px;height:50px">제목</td>
<td><%=bv.getSubject() %></td>
<td style="width:50px">작성자</td>
<td><%=bv.getWriter() %></td>
<td style="width:50px">조회수</td>
<td><%=bv.getViewcount() %></td>
</tr>

<tr>
		<td style="height:300px">파일</td>
		<td colspan=5>
			원본:
			<img src="<%=request.getContextPath()%>/filefolder/<%=bv.getFilename()%>">
			<br>썸네일 : 
			<img src="<%=request.getContextPath()%>/filefolder/s-<%=bv.getFilename()%>">
			<br>다운로드:
			<a href = "<%=request.getContextPath()%>/board/fileDownload.do?filename=<%=bv.getFilename()%>"><%=bv.getFilename()%></a>
		</td>
</tr>

		<tr>
				<td style="height:300px">내용</td>
				<td colspan=5>
				<%=bv.getContents() %>
				</td>
		</tr>
<tr>
<td>
		</td>
			<td colspan="5" style="text-align:right;">
			<a href="<%=request.getContextPath()%>/board/boardModify.do?bidx=<%=bv.getBidx()%>">수정</a>
			<a href="<%=request.getContextPath()%>/board/boardDelete.do?bidx=<%=bv.getBidx()%>">삭제</a>
			<a href="<%=request.getContextPath()%>/board/boardReply.do?bidx=<%=bv.getBidx()%>&originbidx=<%=bv.getOriginbidx()%>&depth=<%=bv.getDepth()%>&llevel=<%=bv.getLlevel()%>">답변</a>
			목록
		</td>
</tr>
</table>
</body>
</html>