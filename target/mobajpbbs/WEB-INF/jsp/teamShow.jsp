<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/teamShow.css">
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/teamShow.js" charset="utf-8"></script>
<title><c:out value="${team.name}" />のページ</title>
</head>
<body>
<jsp:include page="header.jsp" />
<hr>
<main>
	<article>
		<div class="team">
			<div class="teamimage">
				<img src="<c:out value="${team.image}" />">
			</div>
			<div><c:out value="${team.name}" /></div>
			<div>
				リーダー:<a href="<c:out value="${url}" />/user/profile/id/<c:out value="${team.reader.id}" /> ">
					<c:out value="${team.reader.name}" />
				</a>
			</div>
			<div>
				<c:out value="${team.text}" />
			</div>
			<c:if test="${login == 'true' && loginUser.teamid == 0 }" >
				<form action ="<c:out value="${url}" />/team/join" method="post">
					<input type="hidden" name="teamJoinToken" value="<c:out value="${teamJoinToken}" />" >
					<input type="hidden" name="teamid" value="<c:out value="${team.id}" />" >
					<input type="submit" onclick="return checkJoin()"name="join" value="加入する">
				</form>
			</c:if>
		</div><!--  team -->
	</article>
	<aside>
		<h3>チームメンバー</h3>
		<div class="menberlist">
			<c:forEach var="user" items="${userList}">
				<div class="menber" data-url="<c:out value="${url}" />/user/profile/id/<c:out value="${post.user.id}" /> ">
					<div class="menberimage">
						<img src="<c:out value="${user.image}" />" >
					</div>
					<div class="menbername">
						<a href="<c:out value="${url}" />/user/profile/id/<c:out value="${user.id}" /> ">
							<c:out value="${user.name}" />
						</a>
					</div>
				</div>
			</c:forEach>
		</div>
	</aside>
</main>
<hr>
<!-- footer呼び出し -->
<jsp:include page="footer.jsp" />
</body>
</html>