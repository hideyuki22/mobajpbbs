<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/profile.css">
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/profile.js" charset="utf-8"></script>
<title><c:out value="${user.name}" />さんのページ</title>
</head>
<body>
<jsp:include page="header.jsp" />
<hr>
<main>
	<aside>
		<div class="profile">
			<div class="profileimage"><img src="<c:out value="${user.image}" />" ></div>
			<div class="profilename"><c:out value="${user.name}" /></div>
			<div>チーム:
				<c:if test="${user.teamid > 0 }">
					<a href="<c:out value="${url}" />/team/show/id/<c:out value="${user.teamid}" /> ">
						<c:out value="${user.teamName}" />
					</a>
				</c:if>
			</div>

			<div>ランク:
				<c:forEach var="rank" items="${ranks}">
					<c:if test="${user.rank == rank.key }"><c:out value="${rank.value}" /></c:if>
				</c:forEach>
			</div>
			<div>ロール:
				<c:forEach var="roll" items="${rolls}">
					<c:if test="${user.roll == roll.key }"><c:out value="${roll.value}" /></c:if>
				</c:forEach>
			</div>
			<div>
				<c:forEach var="text" items="${user.getSplitText()}">
					<c:out value="${text}"/><br>
				</c:forEach>
			</div>
		</div><!-- profile -->
	</aside>
	<section>
		<h3>投稿一覧</h3>
		<ul class="page-list">
			<li>
				<c:if test="${prevPage >= 0 }">
					<span class="button">
						<a href="<c:out value="${url}" />/user/profile/page/<c:out value="${prevPage}" />
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
						<a href="<c:out value="${url}" />/user/profile/id/<c:out value="${user.id}" />/page/<c:out value="${nextPage}" />
						<c:if test="${categoryid >= 0}">
							/category/<c:out value="${categoryid}" />
						</c:if>
						" >進む</a>
					</span>
				</c:if>
			</li>
		</ul>
		<div class="item">
			<c:forEach var="post" items="${postList}">
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
			</c:forEach>
		</div>
	</section>
</main>
<hr>
<!-- footer呼び出し -->
<jsp:include page="footer.jsp" />
</body>
</html>