<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/index.js" charset="utf-8"></script>
<title>Mobajpbbs</title>
</head>
<body>
<!-- header呼び出し -->
<jsp:include page="header.jsp" />
<hr>
<main>
	<ul class="category-list">
		<li class="<c:if test="${categoryid != null }">reverse-</c:if>button">
			<a href="<c:out value="${url}" />/index/show">全般 </a>
		</li>
		<c:forEach var="category" items="${categoryList}">
			<li class="<c:if test="${categoryid != category.id }">reverse-</c:if>button">
				<a href="<c:out value="${url}" />/index/show/category/<c:out value="${category.id}" />" >
				<c:out value="${category.name}" />
			</a></li>
		</c:forEach>
	</ul>
	<ul class="page-list">
		<li>
			<c:if test="${prevPage >= 0 }">
				<span class="button">
					<a href="<c:out value="${url}" />/index/show/page/<c:out value="${prevPage}" />
						<c:if test="${categoryid >= 0}">
							/category/<c:out value="${categoryid}" />
						</c:if>
					" >戻る</a>
				</span>
			</c:if>
		</li>
		<li>
			<c:if test="${nextPage > 0}">
				<span class="button">
					<a href="<c:out value="${url}" />/index/show/page/<c:out value="${nextPage}" />
					<c:if test="${categoryid >= 0}">
						/category/<c:out value="${categoryid}" />
					</c:if>
					" >進む</a>
				</span>
			</c:if>
		</li>
	</ul>
	<hr>
	<article>
		<c:forEach var="post" items="${postList}">
			<div class="item">
				<div class="item-post">
					<div class="user" data-url="<c:out value="${url}" />/user/profile/id/<c:out value="${post.user.id}" /> ">
						<div class="user-image"><img src="<c:out value="${post.user.image}" />" ></div>
						<div class="user-name">
							<a href="<c:out value="${url}" />/user/profile/id/<c:out value="${post.user.id}" /> ">
								<c:out value="${post.user.name}" />
							</a>
						</div>
					</div><!--  user -->
						<div class ="post" data-url="<c:out value="${url}" />/post/show/id/<c:out value="${post.id}" /> ">
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
				</div><!-- item-post -->
			</div><!-- item -->
		</c:forEach>
	</article>
</main>
<hr>
<!-- footer呼び出し -->
<jsp:include page="footer.jsp" />
</body>
</html>