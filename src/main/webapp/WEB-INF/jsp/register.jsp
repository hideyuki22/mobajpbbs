<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
<script>
	var url = "${pageContext.request.contextPath}";
</script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/register.css">
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/register.js" charset="utf-8"></script>
<title>アカウント登録</title>
</head>
<body>
<jsp:include page="header.jsp" />
<hr>
<main>
<article>
	<form action ="<c:out value="${url}" />/account/inspection"method="post" id="register">
	<table>
		<tr>
			<td>ログインID:</td>
			<td><input type="text" id="loginid" name="loginid" value="<c:out value="${loginid}" />"></td>
			<td id="msgid"><c:out value="${errors.loginid}" /></td>
		</tr>
		<tr>
			<td>パスワード:</td>
			<td><input type="password" id ="pass" name="pass"></td>
			<td id="msgpass"><c:out value="${errors.pass}" /></td>
		<tr>
			<td>パスワード(確認):</td>
			<td><input type="password" id ="verification" name="verification"></td>
			<td id="msgver"><c:out value="${errors.verification}" /></td>
		</tr>
		<tr>
			<td>メールアドレス:</td>
			<td><input type="email" id ="mail" name="mail" value="<c:out value="${mail}" />"></td>
			<td id =msgmail><c:out value="${errors.mail}" /></td>
		</tr>
		<tr>
			<td>表示名:</td>
			<td><input type="text" id="name" name="name" value="<c:out value="${name}" />"></td>
			<td id="msgname"><c:out value="${errors.name}" /></td>
		</tr>
		<tr>
			<td><input type="hidden" name="accountRegisterToken" value="<c:out value="${accountRegisterToken}" />" ></td>
			<td><input type="submit"  name="register" value="新規登録"></td>
			<td id="msgreg"><c:out value="${errors.submit}" /></td>
		</tr>
		</table>
	</form>
</article>
</main>
<hr>
<jsp:include page="footer.jsp" />
</body>
</html>