<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<HTML>
 <HEAD>
 <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <TITLE> New Document </TITLE> 
  <script language = "javascript">
	function check(){
		var fm = document.frm;		

	if (fm.memberId.value =="")	{
		alert("아이디 입력");
		fm.memberId.focus();
		return;
	}else if (fm.memberPwd.value==""){
		alert("비밀번호 입력");
		fm.memberPwd.focus();
		return;
	}else if (fm.memberPwd2.value==""){
		alert("비밀번호 입력2");
		fm.memberPwd2.focus();
		return;
	}else if (fm.memberPwd.value !=	fm.memberPwd2.value){
	    alert("비밀번호 비교");
		fm.memberPwd2.value = "";
		fm.memberPwd2.focus();
		return;
	}else if (fm.memberName.value ==""){
	    alert("이름 입력");
		fm.memberName.focus();
		return;
	}else if (fm.memberEmail.value ==""){
	    alert("이메일 입력");
		fm.memberEmail.focus();
		return;
	}else if (fm.memberJumin.value ==""){
	    alert("주민번호 입력");
		fm.memberJumin.focus();
		return;
	}else if (fm.memberPhone.value ==""){
	    alert("폰번호 입력");
		fm.memberPhone.focus();
		return;
	}
		fm.action ="<%=request.getContextPath()%>/memberWriteAction.do";
		fm.method = "post";
		fm.submit();	

	return;
}

 </script>
 </HEAD>

 <BODY>
<center><h1>회원가입</h1></center>
<hr></hr>
<form name="frm"> 
 <table border="1" style="text-align:left;width:500px;height:80px">
<tr>
<td>아이디</td>
<td><input type="text" name="memberId" size="30"></td>
</tr>
<tr>
<td>비밀번호</td>
<td><input type="password" name="memberPwd" size="30"></td>
</tr>
<tr>
<td>비밀번호2</td>
<td><input type="password" name="memberPwd2" size="30"></td>
</tr>
<tr>
<td>이름</td>
<td><input type="text" name="memberName" size="30"></td>
</tr>
<tr>
<td>주민번호</td>
<td>
<input type="text" name ="memberJumin">
</td>
</tr>
<tr>
<td>성별</td>
<td>
<input type="radio" name ="memberGender" value="1" checked>남성
<input type="radio" name ="memberGender" value="2">여성
</td>
</tr>
<tr>
<td>이메일</td>
<td><input type="email" name="memberEmail" size="30"></td>
</tr>
<tr>
<td>폰</td>
<td><input type="text" name="memberPhone" size="30"></td>
</tr>
<tr>
<td>주소</td>
<td><select name="memberAddr" style="width:100px;height:25px">
	<option value="서울">서울</option>
	<option value="대전">대전</option>
	<option value="전주">전주</option>
	</select>
</td>
</tr>
<tr>
<td></td>
<td>
<input type="button" value="확인" onclick="check();"> 
<input type="reset" value="reset"> 
</td>
</tr>
 </table>
 </form>
 </BODY>
</HTML>
