<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>管理画面</title>
</head>
<body>
<jsp:include page="header.jsp" />
<hr>
<form action ="<c:out value="${url}" />/user/update/item/pass" method="post"><table>
	<tr>
		<td>ユーザー</td>
		<td><select name ="userid">
			<c:forEach var="user" items="${userList}">
				<option value ="<c:out value="${user.id}" />"
				<c:if test="${userid == user.id }">
					selected
				</c:if>
				>
				<c:out value="${user.name}" />
				</option>
			</c:forEach>
		</select><input type="hidden" name="userManagementToken" value="<c:out value="${userManagementToken}" />" ></td>
	<tr>
	<tr>
		<td>新しいパスワード</td>
		<td><input type="password" name="pass" value=""></td>
		<td><c:out value="${errors.pass}" /></td>
	</tr>
	<tr>
		<td>パスワード(確認)</td>
		<td><input type="password" name="verification" value=""></td>
		<td><input type="submit" name="passchange" value="変更する"></td>
		<td></td>
	</tr>
</table></form>
</body>
</html>