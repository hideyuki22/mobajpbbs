package controller;

import java.util.ArrayList;
import java.util.Map;

import core.ApplicationCore;
import core.ControllerBase;
import dao.PostDAO;
import dao.RankDAO;
import dao.RollDAO;
import dao.UserDAO;
import model.Post;
import model.User;

public class UserController extends ControllerBase {
	public UserController(ApplicationCore appCore) {
		super(appCore);
	}

	public void ProfileAction() throws Exception {
		//valuesにIDが存在しなかったらトップぺージへ
		if (!this.values.containsKey("id")) {
			this.response.sendRedirect(this.getBaseUrl());
			return;
		}
		//IDからユーザー情報取得
		int id = Integer.parseInt(this.values.get("id"));
		UserDAO userDAO = new UserDAO();
		User user = userDAO.getUser(id);
		//ユーザー情報が存在しなかったらトップページへ
		if (user == null) {
			this.response.sendRedirect(this.getBaseUrl());
			return;
		}
		this.request.setAttribute("user", user);
		//ロールとランク情報取得
		RankDAO rankDAO = new RankDAO();
		Map<Integer, String> ranks = rankDAO.getRankList();
		this.request.setAttribute("ranks", ranks);
		RollDAO rollDAO = new RollDAO();
		Map<Integer, String> rolls = rollDAO.getRollList();
		this.request.setAttribute("rolls", rolls);
		int page = 0;
		//valuesにpageが存在していたら
		if (this.values.containsKey("page")) {
			page = Integer.parseInt((String) this.values.get("page"));
			if (page > 0) {
				int prevPage = page - 1;
				this.request.setAttribute("prevPage", prevPage);
			}
		}

		PostDAO postDAO = new PostDAO();
		ArrayList<Post> postList = postDAO.FetchAllUserPost(page, 5, user.getId());
		this.request.setAttribute("postList", postList);
		ArrayList<Post> nextList = postDAO.FetchAllUserPost(page + 1, 5, user.getId());
		//次のポストリストが存在していたら
		if (nextList.size() > 0) {
			int nextPage = page + 1;
			this.request.setAttribute("nextPage", nextPage);
		}
		//ログインしてなかったらトークン作成
		if (!this.login.checkLogin()) {
			String indexLoginToken = this.csrfToken.genarateToken("indexLogin");
			this.request.setAttribute("indexLoginToken", indexLoginToken);
		}

		this.forward("/WEB-INF/jsp/profile.jsp");
	}
	//管理画面用(未完成)
	/*public void UpdateAction() throws Exception {
		//ログインしてなかったらトップページへ
		if (!this.login.checkLogin()) {
			this.response.sendRedirect(this.getBaseUrl());
			return;
		}
		//ポストからじゃなかったらトップページへ
		if (!this.request.getMethod().equals("POST")) {
			this.response.sendRedirect(this.getBaseUrl());
			return;
		}
		//トークンが登録されていなかったらトップページへ
		String token = this.request.getParameter("userManagementToken");
		if (!this.csrfToken.checkToken("userManagement", token)) {
			this.response.sendRedirect(this.getBaseUrl());
			return;
		}
		UserDAO userDAO = new UserDAO();
		int userid = Integer.valueOf(this.request.getParameter("userid"));
		User user = userDAO.getUser(userid);
		//ユーザーが存在しなかったらトップページへ
		if (user == null) {
			this.response.sendRedirect(this.getBaseUrl());
			return;
		}

		Map<String, String> errors = new HashMap<>();
		//valuesにitemがなかｔったらトップページへ
		if (!this.values.containsKey("item")) {
			this.response.sendRedirect(this.getBaseUrl() + "/account/mypage");
			return;
		}
		String item = this.values.get("item");
		switch (item) {
			case "pass" :
				String pass = this.request.getParameter("pass");
				String verification = this.request.getParameter("verification");
				String pattern = "^\\w{6,20}$";
				if (pass != null && !pass.matches(pattern)) {
					errors.put("pass", "パスワードは6文字以上20文字以内です。");
				} else if (!pass.equals(verification)) {
					errors.put("pass", "パスワードが一致しません。");
				} else if (!userDAO.UpdateString("pass", pass, user.getId())) {
					errors.put("pass", "更新に失敗しました。");
				}
		}
		//エラーが存在しなかったら更新
		if (errors.isEmpty()) {
			System.out.println("更新成功");
			this.response.sendRedirect(this.getBaseUrl() + "/user/management");
		} else {
			//エラーがあったらManagementActionへ
			this.request.setAttribute("errors", errors);
			this.request.setAttribute("userid", userid);
			this.ManagementAction();
		}
	}

	public void ManagementAction() throws Exception {
		//ログインしてなかったらトップページへ
		if (!this.login.checkLogin()) {
			this.response.sendRedirect(this.getBaseUrl());
			return;
		}
		//ユーザーリスト取得
		UserDAO userDAO = new UserDAO();
		ArrayList<User> userList = userDAO.getUserList();
		this.request.setAttribute("userList", userList);
		//トークン作成
		String userManagementToken = this.csrfToken.genarateToken("userManagement");
		this.request.setAttribute("userManagementToken", userManagementToken);
		this.forward("/WEB-INF/jsp/management.jsp");
	}*/
}