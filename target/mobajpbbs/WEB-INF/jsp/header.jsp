<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/header.js" charset="utf-8"></script>
<header class="page-header">
<h1><a href="<c:out value="${url}" />">Mobajpbbs</a></h1>
	<nav>
	<c:choose>
		<c:when test="${login == 'true' }" >
			<form action ="<c:out value="${url}" />/index/logout" method="post" id="logout">
			<ul class ="main-nav">
				<li>ようこそ <c:out value="${loginUser.name}" /> さん</li>
				<li class="dropmenu">
					<span class="button">各種ページ</span>
					<ul class="sub-nav">
						<li><a href="<c:out value="${url}" />/account/mypage">マイページへ</a></li>
						<li><a href="<c:out value="${url}" />/post/new">新規投稿</a></li>
						<li class="dropmene">
							<c:choose>
								<c:when test="${loginUser.teamid >0}" >
									<a href="<c:out value="${url}" />/team/edit">チームページへ</a>
								</c:when>
								<c:otherwise>
									<a href="<c:out value="${url}" />/team/register">チーム作成へ</a>
								</c:otherwise>
							</c:choose>
						</li>
					</ul>
				<li><input type="submit" name="logout" value="ログアウト" class="button"><input type="hidden" name="indexLoginToken" value="<c:out value="${indexLoginToken}" />" ></li>
			</ul>
			</form>
		</c:when>
		<c:otherwise>
			<form action ="<c:out value="${url}" />/index/login" method="post">
			<ul class ="main-nav">
				<li>ようこそ ゲスト さん</li>
				<li class="dropmenu">
					<div class="button">ログインする</div>
					<ul class="sub-nav">
						<li>ログインID</li>
						<li><input type="text" name="loginid" value="<c:out value="${loginid}" />"></li>
						<li>パスワード</li>
						<li><input type="password" name="pass"><input type="hidden" name="indexLoginToken" value="<c:out value="${indexLoginToken}" />" ></li>
						<li><input type="submit" name="login" value="ログイン"></li>
						<li><c:out value="${loginError}" /></li>
					</ul>
				</li>
				<li><span class="button">
					<a href="<c:out value="${url}" />/account/register">アカウント登録</a>
				</span></li>
			</ul>
			</form>
		</c:otherwise>
	</c:choose>
	</nav>
</header>