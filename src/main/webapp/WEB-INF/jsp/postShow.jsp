<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/postShow.css">
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/postShow.js" charset="utf-8"></script>
<title><c:out value="${post.title}" /></title>
</head>
<body>
<jsp:include page="header.jsp" />
<hr>
<main>
	<article>
		<div class="item-post">
			<div class="user">
				<div class="user-image"><img src="<c:out value="${url}" />/upload/<c:out value="${post.user.image}" />" ></div>
				<div class="user-name">
					<a href="<c:out value="${url}" />/user/profile/id/<c:out value="${post.user.id}" /> ">
						<c:out value="${post.user.name}" />
					</a>
				</div>
			</div><!--  user -->
				<div class ="post">
					<div class="post-category">カテゴリー:<c:out value="${post.category}" /></div>
					<div class="post-date"><c:out value="${post.date}" /> <c:out value="${post.time}" /></div>
					<div class="post-title">
						<a href="<c:out value="${url}" />/post/show/id/<c:out value="${post.id}" /> ">
							<c:out value="${post.title}" />
						</a>
					</div>
					<div class="post-text">
							<c:out value="${post.text}"/>
					</div>
			</div><!-- post -->
		</div><!-- item -->
		<hr>
		<h3>コメント</h3>
		<div class="commentlist">
			<c:forEach var="cm" items="${cmList}">
				<table class="level<c:out value="${cm.level}" />">
					<tr>
						<td id="comment_<c:out value="${cm.id}" />"><c:out value="${cm.id}" /></td>
						<td colspan="2">
							<a href="<c:out value="${url}" />/user/profile/id/<c:out value="${cm.user.id}" /> ">
								<c:out value="${cm.user.name}" />
							</a>
						</td>
						<td><c:out value="${cm.date}" /> <c:out value="${cm.time}" /></td>
						<td class="reply">▼返信</td>
					</tr>
					<tr>
						<td><img src="<c:out value="${url}" />/upload/<c:out value="${cm.user.image}" />" ></td>
						<td colspan="4" class="comment-text">
							<c:forEach var="text" items="${cm.getSplitText()}">
								<c:out value="${text}"/><br>
							</c:forEach>
						</td>
					</tr>
				</table>
			</c:forEach>
		</div><!-- commnetlist -->
		<hr>
		<form action ="<c:out value="${url}" />/post/comment" method="post" id="comment">
			<table class="comment">
			<tr>
				<td><textarea id="text" name="text" rows="10" cols="40"  wrap="hard"><c:out value="${text}" /></textarea></td>
				<td id="msgtext"><c:out value="${errors.text}" /></td>
			</tr>
			<tr>
				<td>
					<input type="hidden" name="postCommentToken" value="<c:out value="${postCommentToken}" />" >
					<input type="hidden" name="postid" value="<c:out value="${post.id}" />" >
					<input type="submit" name="comment" value="コメントする" >
				</td>
				<td><c:out value="${errors.submit}" /></td>
			</tr>
			</table>
		</form>
	</article>
</main>
<hr>
<jsp:include page="footer.jsp" />
</body>
</html>