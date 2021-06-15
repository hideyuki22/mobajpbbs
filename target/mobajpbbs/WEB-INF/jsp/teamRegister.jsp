<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/teamRegister.css">
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/teamRegister.js" charset="utf-8"></script>
<title>チーム登録</title>
</head>
<body>
<jsp:include page="header.jsp" />
<hr>
<main>
	<article>
		<form action ="<c:out value="${url}" />/team/inspection"method="post" enctype="multipart/form-data" id="register" >
			<table class="teamtable">
			<tr>
				<td>チーム名(必須):</td>
				<td colspan="2"><input type="text" id="name" name="name" value="<c:out value="${name}" />"></td>
				<td id="msgname" class="red"><c:out value="${errors.name}" /></td>
			</tr>
			<tr>
				<td><img src="<c:out value="${url}" />/upload/default.jpg" id="image"></td>
				<td colspan="2"><input type="file" id="upload" name="upload"/></td>
				<td id="msgimg" class="red"><c:out value="${errors.image}" /></td>
			<tr>
				<td>紹介文:</td>
				<td colspan="2"><textarea id="text" name="text" rows="10" cols="40"  wrap="hard"><c:out value="${text}" /></textarea></td>
				<td id="msgtext" class="red"><c:out value="${errors.text}" /></td>
			</tr>
			<tr>
				<td colspan="2"><input type="hidden" name="teamRegisterToken" value="<c:out value="${teamRegisterToken}" />" ></td>
				<td><input type="submit" name="register" value="チーム登録"></td>
				<td id="msgreg" class="red"><c:out value="${errors.submit}" /></td>
			</tr>
			</table>
		</form>
	</article>
</main>
<hr>
<!-- footer呼び出し -->
<jsp:include page="footer.jsp" />
</body>
</html>