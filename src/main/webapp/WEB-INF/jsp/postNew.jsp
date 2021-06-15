<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/postNew.css">
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/postNew.js" charset="utf-8"></script>
<title>新規投稿</title>
</head>
<body>
<jsp:include page="header.jsp" />
<hr>
<main>
	<article>
		<form action ="<c:out value="${url}" />/post/register" method="post" id="post">
			<table class="posttable">
			<tr>
				<td>カテゴリー:</td>
				<td><select name ="category">
				<c:forEach var="category" items="${categoryList}">
					<c:if test="${loginUser.teamid >0 || category.teamFlag == false }">
						<option value ="<c:out value="${category.id}" />"
						<c:if test="${categoryid == category.id }">
							selected
						</c:if>
						>
						<c:out value="${category.name}" />
						</option>
					</c:if>
				</c:forEach>
				</select></td>
				<td><c:out value="${errors.category}" /></td>
			</tr>
			<tr>
				<td>タイトル:</td>
				<td><input type="text" id="title" name="title" value="<c:out value="${title}" />"></td>
				<td id="msgtitle" class="red"><c:out value="${errors.title}" /></td>
			</tr>
			<tr>
				<td>本文:</td>
				<td><textarea id="text" name="text" rows="10" cols="40"  wrap="hard"><c:out value="${text}" /></textarea></td>
				<td id="msgtext" class="red"><c:out value="${errors.text}" /></td>
			</tr>
			<tr>
				<td><input type="hidden" name="postNewToken" value="<c:out value="${postNewToken}" />" ></td>
				<td><input type="submit" name=register value="新規投稿"></td>
				<td id="msgpost" class="red"><c:out value="${errors.submit}" /></td>
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