<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/main.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/mypage.css">
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/mypage.js" charset="utf-8"></script>
<title>マイページ</title>
</head>
<body>
	<jsp:include page="header.jsp" />
	<hr>
	<main>
		<article>
			<div class ="mypage">
				<form action="<c:out value="${url}" />/account/upload" method="post" enctype="multipart/form-data">
					<table class="imagetable">
						<tr>
							<td rowspan="2">
								<img src="<c:out value="${url}" />/upload/<c:out value="${user.image}" />" id="image">
								<input type="hidden" name="accountMypageToken" value="<c:out value="${accountMypageToken}" />">
							</td>
							<td><input type="file" id="upload" name="upload" value=""/></td>
						</tr>
						<tr>
							<td>
								<input type="submit"  id="imagechange" name="imagechange" value="変更する">
								<span id="msgimg"><c:out value="${errors.image}" /></span>
							</td>
						</tr>
					</table>
				</form>
				<form action="<c:out value="${url}" />/account/update/item/name" method="post">
					<input type="hidden" name="accountMypageToken" value="<c:out value="${accountMypageToken}" />">
					<table class="changetable">
							<tr>
								<td>
									表示名:
								</td>
								<td><input type="text" id="name" name="name" value="<c:out value="${user.name}" />"></td>
								<td><input type="submit" id="namechange" name="namechange" value="変更する"></td>
								<td id="msgname"><c:out value="${errors.name}" /></td>
							</tr>
						<tr>
							<td>
								メールアドレス:
							</td>
							<td><input type="text" id="mail" name="mail" value="<c:out value="${user.mail}" />"></td>
							<td><input type="submit" formaction="<c:out value="${url}" />/account/update/item/mail" id="mailchange" name="mailchange" value="変更する"></td>
							<td id="msgmail"><c:out value="${errors.mail}" /></td>
						</tr>
							<tr>
								<td>現在のパスワード:</td>
								<td><input type="password"  name="pass" value=""></td>
							</tr>
						<tr>
							<td>新しいパスワード:</td>
							<td><input type="password" id="pass" name="newpass" value=""></td>
						</tr>
						<tr>
							<td>新しいパスワード(確認):</td>
							<td><input type="password" id ="verification" name="verification" value=""></td>
							<td><input type="submit" formaction="<c:out value="${url}" />/account/update/item/pass" id="passchange" name="passchange" value="変更する"></td>
							<td id="msgpass"><c:out value="${errors.pass}" /></td>
						</tr>
						<tr>
							<td>
								ランク:
							</td>
							<td><select name="rank">
									<c:forEach var="rank" items="${ranks}">
										<option value="<c:out value="${rank.key}" />"
											<c:if test="${user.rank == rank.key }">
												selected
											</c:if>>
											<c:out value="${rank.value}" />
										</option>
									</c:forEach>
							</select></td>
							<td><input type="submit" formaction="<c:out value="${url}" />/account/update/item/rank" name="rankchange" value="変更する" onclick="return checkChange()"></td>
							<td><c:out value="${errors.rank}" /></td>
						</tr>
						<tr>
							<td>
								ロール:
							</td>
							<td><select name="roll">
									<c:forEach var="roll" items="${rolls}">
										<option value="<c:out value="${roll.key}" />"
											<c:if test="${user.roll == roll.key }">
												selected
											</c:if>>
											<c:out value="${roll.value}" />
										</option>
									</c:forEach>
							</select></td>
							<td><input type="submit" formaction="<c:out value="${url}" />/account/update/item/roll" name="rollchange" value="変更する" onclick="return checkChange()"></td>
							<td><c:out value="${errors.roll}" /></td>
						</tr>
						<tr>

							<td colspan="2"><textarea id="text" name="text" rows="10" cols="40" wrap="hard"><c:out value="${user.text}" /></textarea></td>
							<td>
								<input type="submit" formaction="<c:out value="${url}" />/account/update/item/text" id="textchange" name="textchange" value="変更する">
							</td>
							<td id="msgtext" class="red"><c:out value="${errors.text}" /></td>
						</tr>
					</table>
				</form>
			</div>
		</article>
		<aside>
			<div class="item">
				<ul class="page-list">
					<li>
						<c:if test="${prevPage >= 0 }">
							<span class="button">
								<a href="<c:out value="${url}" />/account/mypage/page/<c:out value="${prevPage}" />
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
								<a href="<c:out value="${url}" />/account/mypage/page/<c:out value="${nextPage}" />
								<c:if test="${categoryid >= 0}">
									/category/<c:out value="${categoryid}" />
								</c:if>
								" >進む</a>
							</span>

						</c:if>
					</li>
				</ul>
					<div class="post">
						<table>
							<tr>
							<td>カテゴリー</td>
							<td>タイトル</td>
							<td>投稿者</td>
							<td>投稿日時</td>
							</tr>
						<c:forEach var="post" items="${postList}">
							<tr>
							<td><c:out value="${post.category}" /></td>
							<td><a href="<c:out value="${url}" />/post/show/id/<c:out value="${post.id}" /> ">
							<c:out value="${post.title}" />
							</a></td>
							<td><a href="<c:out value="${url}" />/user/profile/id/<c:out value="${post.user.id}" /> ">
								<c:out value="${post.user.name}" />
							</a></td>
							<td><c:out value="${post.date}" /> <c:out value="${post.time}" /></td>
							<td><c:if test="${post.user.id == user.id }">
								<span class="button">
									<a href="<c:out value="${url}" />/edit/post/id/<c:out value="${post.id}" /> ">
										編集する
									</a>
								</span>
							</c:if></td>
							</tr>
						</c:forEach>
						</table>
					</div><!-- post -->
				</div><!-- item -->
		</aside>
	</main>
	<hr>
	<jsp:include page="footer.jsp" />
</body>
</html>