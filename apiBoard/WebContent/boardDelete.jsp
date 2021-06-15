<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import = "service.BoardVo" %>    
<% BoardVo bv = (BoardVo)request.getAttribute("bv"); %>
<!DOCTYPE HTML>
<HTML>
 <HEAD>
 <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
 <TITLE> 게시판 삭제하기 </TITLE> 
 <script type="text/javascript">
  function check(){
	  if (document.frm.password.value == ""){
		  alert("비밀번호를 입력해주세요");
		  document.frm.password.focus();
		  return;
	  }
	  
	  alert("전송합니다");
	  document.frm.action ="<%=request.getContextPath()%>/board/boardDeleteAction.do";
	  document.frm.method = "post";
	  document.frm.submit(); 
	  return; 
  } 
 </script>
	 <style>
		div{
			font-size:25px; 
			font-weight:bold; 
			text-decoration:underline; 
			padding:10px;
			text-align:center;
		}
	</style>
 </HEAD> 
 <BODY>
<div><h1>게시판 삭제하기</h1></div>
<hr></hr>
<form name="frm"> 
<input type="hidden" name="bidx" value="<%=bv.getBidx()%>">
 <table border="1" style="text-align:left;width:800px;height:300px">
<tr>
<td>제목</td>
<td><%=bv.getSubject()%></td>
</tr>
<tr>
<td>비밀번호</td>
<td><input type="password" name="password" size="30"></td>
</tr>
<tr>
<td></td>
<td>
<input type="button" value="확인" onclick="check();"> 
<input type="reset" value="다시작성"> 
</td>
</tr>
 </table>
 </form>
 </BODY>
</HTML>
