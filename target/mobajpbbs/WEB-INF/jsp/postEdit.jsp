<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/postEdit.css">
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/postEdit.js" charset="utf-8"></script>
<title>マイページ編集</title>
</head>
<body>
	<jsp:include page="header.jsp" />
	<hr>
	<main>
		<article>
			<form action ="<c:out value="${url}" />/edit/update" method="post" >
				<input type="hidden" name="editPostToken" value="<c:out value="${editPostToken}" />" >
				<input type="hidden" name="postid" value="<c:out value="${post.id}" />" >
					<table class="posttable">
					<tr>
						<td colspan="2"><c:out value="${post.date}" /> <c:out value="${post.time}" /></td>
					</tr>
					<tr>
						<td>カテゴリー：<c:out value="${post.category}" /></td>
						<td><input type="text" id="title" name ="title" value="<c:out value="${post.title}" />"></td>

						<td id="msgtitle"><c:out value="${errors.title}" /></td>
					</tr>
					<tr>
						<td colspan="2"><textarea id="text" name="text" rows="10" cols="40"  wrap="hard"><c:out value="${post.text}" /></textarea></td>
						<td id="msgtext"><c:out value="${errors.text}" /></td>
					</tr>
					<tr>
						<td colspan="2">
							<input type="submit" id="edit" name=edit value="変更する">
						</td>
						<td id="msgedit"></td>
					</tr>
					<tr>
						<td colspan="2">
							<input type="submit" formaction="<c:out value="${url}" />/edit/drop" name=drop value="全て削除" onclick="return checkDelete()">
						</td>
					</tr>
				</table>
			</form>
			<hr>
			<div class="commentlist">
				<c:forEach var="cm" items="${cmList}">
					<form action ="<c:out value="${url}" />/edit/delete" method="post" onsubmit="return checkDelete()">
						<div class="comment">
							<table class="level<c:out value="${cm.level}" />">
								<tr>
									<td><c:out value="${cm.id}" /></td>
									<td colspan="2">
										<a href="<c:out value="${url}" />/user/profile/id/<c:out value="${cm.user.id}" /> ">
											<c:out value="${cm.user.name}" />
										</a>
									</td>
									<td><c:out value="${cm.date}" /> <c:out value="${cm.time}" /></td>
								</tr>
								<tr>
									<td><img src="<c:out value="${cm.user.image}" />" ></td>
									<td colspan="3">
										<c:forEach var="text" items="${cm.getSplitText()}">
											<c:out value="${text}"/><br>
										</c:forEach>
									</td>
								</tr>
							</table>
							<div class="del">
								<input type="hidden" name="editPostToken" value="<c:out value="${editPostToken}" />" >
								<input type="hidden" name="commentid" value="<c:out value="${cm.id}" />" >
								<input type="hidden" name="postid" value="<c:out value="${post.id}" />" >
								<input type="submit" name="delete" value="削除する" >
							</div>
						</div>
					</form>
				</c:forEach>
			</div><!-- commnetlist -->
		</article>
	</main>
	<hr>
</body>
</html>