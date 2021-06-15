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
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/teamEdit.css">
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/teamEdit.js" charset="utf-8"></script>
<title><c:out value="${team.name}" />のページ</title>
</head>
<body>
<jsp:include page="header.jsp" />
<hr>
<main>
<article>
	<div class="team">
		<div class="teamimage">
			<img src="<c:out value="${url}" />/upload/<c:out value="${team.image}" />" id="image">
		</div>
		<form action ="<c:out value="${url}" />/team/upload"method="post" enctype="multipart/form-data">
			<input type="hidden" name="teamEditToken" value="<c:out value="${teamEditToken}" />" >
			<div class="red">
				<input type="file" id="upload" name="upload"/>
			</div>
			<div>
				<input type="submit" id="imagechange" name="imagechange" value="画像変更" >
				<span id="msgimg" class="red"><c:out value="${errors.image}" /></span>
			</div>
		</form>
		<form action ="<c:out value="${url}" />/team/update/item/name" method="post">
			<input type="hidden" name="teamEditToken" value="<c:out value="${teamEditToken}" />" >
			<c:choose>
				<c:when test="${loginUser.id == team.reader.id }">
					<div><input type="text" id="name" name="name" value="<c:out value="${team.name}" />"></div>
					<div>
						<input type="submit" id="namechange" name="namechange" value="チーム名変更">
						<span id="msgname" class="red"><c:out value="${errors.name}" /></span>
					</div>
				</c:when>
				<c:otherwise>
					<div><c:out value="${team.name}" /></div>
				</c:otherwise>
			</c:choose>
			<div>
				リーダー:<a href="<c:out value="${url}" />/user/profile/id/<c:out value="${team.reader.id}" /> ">
					<c:out value="${team.reader.name}" />
				</a>
			</div>
			<div>
				<textarea id="text" name="text" rows="10" cols="40"  wrap="hard"><c:out value="${team.text}" /></textarea>
			</div>
			<div>
				<input type="submit" formaction="<c:out value="${url}" />/team/update/item/text" id="textchange" name="textchange" value="変更する">
				<span id="msgtext" class="red"><c:out value="${errors.text}" /></span>
			</div>
		</form>
	</div><!--  team -->
</article>
<aside>
	<h3>チームメンバー</h3>
	<div class="menberlist">
		<c:forEach var="user" items="${userList}">
			<div class="menber">
				<form action ="<c:out value="${url}" />/team/update/item/reader"method="post">
					<input type="hidden" name="teamEditToken" value="<c:out value="${teamEditToken}" />" >
					<div>
						<a href="<c:out value="${url}" />/user/profile/id/<c:out value="${user.id}" /> ">
						<img src="<c:out value="${url}" />/upload/<c:out value="${user.image}" />" >
							<c:out value="${user.name}" />
						</a>
						<c:if test="${loginUser.id == team.reader.id  && user.id != loginUser.id}">
							<input type="hidden" name="userid" value="<c:out value="${user.id}" />" >
							<input type="submit" name="transfer" onclick="return checkChange()" value="リーダーに任命" >
							<input type="submit" name="remove" onclick="return checkDelete()" formaction="<c:out value="${url}" />/team/remove" value="追放する">
						</c:if>
						<c:if test="${user.id == loginUser.id}">
							<input type="hidden" id="loginuserid" value="<c:out value="${loginUser.id}" />" >
							<input type="hidden" id="readerid" value="<c:out value="${team.reader.id}" />" >
							<input type="submit" id="leave" name="leave" formaction="<c:out value="${url}" />/team/leave" value="チームから脱退する">
						</c:if>
					</div>
				</form>
				<c:if test="${user.id == loginUser.id}">
					<div id="msgleave" class="red"><c:out value="${errors.leave}" /></div>
				</c:if>
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