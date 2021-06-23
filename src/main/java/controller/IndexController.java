package controller;

import java.util.ArrayList;

import core.ApplicationCore;
import core.ControllerBase;
import dao.CategoryDAO;
import dao.PostDAO;
import dao.UserDAO;
import model.Category;
import model.Post;
import model.User;

public class IndexController extends ControllerBase {
	public IndexController(ApplicationCore appCore) {
		super(appCore);
	}

	public void IndexAction() throws Exception {
		//カテゴリーリストとポストリストの取得
		CategoryDAO caDAO = new CategoryDAO();
		ArrayList<Category> categoryList = caDAO.getCategoryList();
		this.request.setAttribute("categoryList", categoryList);
		PostDAO postDAO = new PostDAO();
		ArrayList<Post> postList = postDAO.FetchAllPost();
		this.request.setAttribute("postList", postList);
		//次のポストリストがあるか確認
		ArrayList<Post> nextList = postDAO.FetchAllPost(1);
		if (nextList.size() > 0) {
			int nextPage = 1;
			this.request.setAttribute("nextPage", Integer.valueOf(nextPage));
		}

		//ログインしてなかったらトークン作成
		if (!this.login.checkLogin()) {
			String indexLoginToken = this.csrfToken.genarateToken("indexLogin");
			this.request.setAttribute("indexLoginToken", indexLoginToken);
		}

		this.forward("/WEB-INF/jsp/index.jsp");
	}

	public void ShowAction() throws Exception {
		int page = 0;
		//valuesにpageがあれば取得
		if (this.values.containsKey("page")) {
			page = Integer.parseInt(this.values.get("page"));
			if (page > 0) {
				//1個前のページ
				int prevPage = page - 1;
				this.request.setAttribute("prevPage", prevPage);
			}
		}
		//カテゴリーリストとポストリスト取得
		CategoryDAO caDAO = new CategoryDAO();
		ArrayList<Category> categoryList = caDAO.getCategoryList();
		this.request.setAttribute("categoryList", categoryList);
		PostDAO postDAO = new PostDAO();
		ArrayList<Post> postList;
		ArrayList<Post> nextList;
		int nextPage;
		if (this.values.containsKey("category")) {
			nextPage = Integer.parseInt((String) this.values.get("category"));
			this.request.setAttribute("categoryid", nextPage);
			postList = postDAO.FetchAllPost(page, 12, nextPage);
			nextList = postDAO.FetchAllPost(page + 1, 12, nextPage);
		} else {
			postList = postDAO.FetchAllPost(page);
			nextList = postDAO.FetchAllPost(page + 1);
		}
		//次のポストリストがあるか確認
		if (nextList.size() > 0) {
			nextPage = page + 1;
			this.request.setAttribute("nextPage", nextPage);
		}

		//ポストリストの登録
		this.request.setAttribute("postList", postList);
		//ログインしてなかったらトークン作成
		if (!this.login.checkLogin()) {
			String indexLoginToken = this.csrfToken.genarateToken("indexLogin");
			this.request.setAttribute("indexLoginToken", indexLoginToken);
		}

		this.forward("/WEB-INF/jsp/index.jsp");
	}

	public void LogoutAction() throws Exception {
		//POSTかつログアウトからならセッションリセット
		if (this.request.getMethod().equals("POST") && "ログアウト".equals(this.request.getParameter("logout"))) {
			this.session.invalidate();
		}
			this.response.sendRedirect(this.getBaseUrl());

	}

	public void LoginAction() throws Exception {
		//POSTまたはログインからじゃなかったらトップページへ
		if (!this.request.getMethod().equals("POST") || !"ログイン".equals(this.request.getParameter("login"))) {
			 this.response.sendRedirect(this.getBaseUrl());
			 return;
		}
		//すでにログインしていたらトップページへ
		if (this.login.checkLogin()) {
			this.response.sendRedirect(this.getBaseUrl());
			return;
		}

		String token = this.request.getParameter("indexLoginToken");
		//トークンが登録されてなかったらトップページ
		if (!this.csrfToken.checkToken("indexLogin", token)) {
			this.response.sendRedirect(this.getBaseUrl());
			return;
		}
		//フォームの値の取得
		String loginid = this.request.getParameter("loginid");
		String pass = this.request.getParameter("pass");
		UserDAO userDAO = new UserDAO();
		User loginUser = userDAO.CheckLogin(loginid, pass);
		//ユーザーが存在したらログインしてセッションに登録
		if (loginUser != null) {
			this.login.login();
			this.session.setAttribute("loginUser", loginUser);
			this.response.sendRedirect(this.getBaseUrl());
		} else {
			//違っていたらメッセージを登録してIndexActionへ
			String loginError = "ユーザーIDまたパスワードが違います";
			this.request.setAttribute("loginError", loginError);
			this.request.setAttribute("loginid", loginid);
			this.IndexAction();
		}
}

}