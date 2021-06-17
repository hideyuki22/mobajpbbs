package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import core.ApplicationCore;
import core.ControllerBase;
import dao.CategoryDAO;
import dao.CommentDAO;
import dao.PostDAO;
import dao.UserDAO;
import model.Category;
import model.Comment;
import model.Post;
import model.User;

public class PostController extends ControllerBase {
	public PostController(ApplicationCore appCore) {
		super(appCore);
	}

	public void CommentAction() throws Exception {
		//postidが存在しなかったらトップページへ
		int postid = Integer.parseInt(this.request.getParameter("postid"));
		PostDAO postDAO = new PostDAO();
		if (!postDAO.isPost(postid)) {
			this.response.sendRedirect(this.getBaseUrl());
			return;
		}
		//POSTまたコメントするからじゃなかったらポストページへ
		if (!this.request.getMethod().equals("POST") || !"コメントする".equals(this.request.getParameter("comment"))) {
			this.response.sendRedirect(this.getBaseUrl() + "/post/show/id/" + postid);
			return;
		}
		//トークン確認して存在しなかったらポストページへ
		String token = this.request.getParameter("postCommentToken");
		if (!this.csrfToken.checkToken("postComment", token)) {
			this.response.sendRedirect(this.getBaseUrl() + "/post/show/id/" + postid);
			return;
		}
		//エラー用編集
		Map<String, String> errors = new HashMap<>();
		//ログインしてなかったら
		if (!this.login.checkLogin()) {
			errors.put("submit", "ログインを行ってください。");
		}
		//テキストチェック
		String text = this.request.getParameter("text");
		if (text == null || text.length() < 2 || text.length() > 1000) {
			errors.put("text", "コメントの文字数は2文字以上1000文字以内です。");
		}
		//エラーがなかったら
		if (errors.isEmpty()) {
			User loginUser = (User) this.session.getAttribute("loginUser");
			CommentDAO cmDAO = new CommentDAO();
			//コメントを追加してポストページへ
			if (cmDAO.InsertComment(postid, loginUser.getId(), text)) {
				this.response.sendRedirect(this.getBaseUrl() + "/post/show/id/" + postid);
				return;
			}

			errors.put("submit", "投稿に失敗しました。");
		}
		//テキストとエラーを登録してShowActionへ
		this.request.setAttribute("errors", errors);
		this.request.setAttribute("text", text);
		this.values.put("id", String.valueOf(postid));
		this.ShowAction();
	}

	public void ShowAction() throws Exception {
		//valuesにidがなかったらトップページへ
		if (!this.values.containsKey("id")) {
			this.response.sendRedirect(this.getBaseUrl());
			return;
		}
		//postidが存在しなかったらトップページへ
		int postid = Integer.parseInt((String) this.values.get("id"));
		PostDAO postDAO = new PostDAO();
		Post post = postDAO.getPost(postid);
		if (post == null) {
			this.response.sendRedirect(this.getBaseUrl());
		}
		//ポストとコメントリスト取得
		this.request.setAttribute("post", post);
		CommentDAO cmDAO = new CommentDAO();
		ArrayList<Comment> cmList = cmDAO.FetchAllComment(postid);
		cmList = Comment.CreateTreeList(cmList);
		this.request.setAttribute("cmList", cmList);
		//ログインしてなかったらトークン作成
		if (!this.login.checkLogin()) {
			String indexLoginToken = this.csrfToken.genarateToken("indexLogin");
			this.request.setAttribute("indexLoginToken", indexLoginToken);
		}
		//トークン作成
		String postCommentToken = this.csrfToken.genarateToken("postComment");
		this.request.setAttribute("postCommentToken", postCommentToken);
		this.forward("/WEB-INF/jsp/postShow.jsp");
	}

	public void NewAction() throws Exception {
		//ログインしてなかったらトップページへ
		if (!this.login.checkLogin()) {
			this.response.sendRedirect(this.getBaseUrl());
			return;
		}
		//カテゴリーリスト取得
		CategoryDAO caDAO = new CategoryDAO();
		ArrayList<Category> categoryList = caDAO.getCategoryList();
		this.request.setAttribute("categoryList", categoryList);
		//トークン作成
		String postNewToken = this.csrfToken.genarateToken("postNew");
		this.request.setAttribute("postNewToken", postNewToken);
		//ログインしてなかったらトークン作成
		if (!this.login.checkLogin()) {
			String indexLoginToken = this.csrfToken.genarateToken("indexLogin");
			this.request.setAttribute("indexLoginToken", indexLoginToken);
		}

		this.forward("/WEB-INF/jsp/postNew.jsp");
	}

	public void RegisterAction() throws Exception {
		//ログインしてなかったらトップページへ
		if (!this.login.checkLogin()) {
			this.response.sendRedirect(this.getBaseUrl());
			return;
		}
		//POSTまた新規投稿からじゃなかったらトップページへ
		if (!this.request.getMethod().equals("POST") || !"新規投稿".equals(this.request.getParameter("register"))) {
			this.response.sendRedirect(this.getBaseUrl());
			return;
		}
		//トークン確認して存在しなかったらトップページへ
		String token = this.request.getParameter("postNewToken");
		if (!this.csrfToken.checkToken("postNew", token)) {
			this.response.sendRedirect(this.getBaseUrl());
			return;
		}
		//エラー用変数とログインユーザー情報
		Map<String, String> errors = new HashMap<>();
		User loginUser = (User) this.session.getAttribute("loginUser");
		UserDAO userDAO = new UserDAO();
		//ログインユーザーのIDの閣員
		if (!userDAO.isUser(loginUser.getId())) {
			errors.put("submit", "ログイン状態が不正です。");
		}

		String userid = String.valueOf(loginUser.getId());
		String categoryid = this.request.getParameter("category");
		String title = this.request.getParameter("title");
		String text = this.request.getParameter("text");
		//タイトルチェック
		if (title != null && title.length() != 0) {
			if (title.length() < 2 || title.length() > 30) {
				errors.put("title", "タイトルは2文字以上30文字以内です。");
			}
		} else {
			errors.put("title", "タイトルは必須項目です。");
		}

		//カテゴリーチェック
		CategoryDAO caDAO = new CategoryDAO();
		Category category = caDAO.getCategoryid(categoryid);
		if (category == null) {
			errors.put("category", "カテゴリーの値が不正です。");
		} else if (loginUser.getTeamid() == 0 && category.isTeamFlag()) {
			errors.put("category", "このカテゴリーはチームに所属してる場合のみ選択可能です。");
		}
		//テキストチェック
		if (text != null && text.length() != 0) {
			if (text != null && (text.length() < 5 || text.length() > 1000)) {
				errors.put("text", "紹介文は5文字以上1000文字以内です。");
			}
		} else {
			errors.put("title", "紹介文は必須項目です。");
		}

		//エラーがなかったら登録
		if (errors.isEmpty()) {
			Map<String, String> post = new HashMap<>();
			post.put("userid", userid);
			post.put("categoryid", categoryid);
			post.put("title", title);
			post.put("text", text);
			PostDAO postDAO = new PostDAO();
			//トップページへ
			if (postDAO.InsertPost(post)) {
				this.response.sendRedirect(this.getBaseUrl());
				return;
			}
		}
		//エラーとフォームの値を登録してNewAcitionへ
		errors.put("submit", "投稿に失敗しました");
		this.request.setAttribute("errors", errors);
		this.request.setAttribute("categoryid", Integer.parseInt(categoryid));
		this.request.setAttribute("title", title);
		this.request.setAttribute("text", text);
		this.NewAction();
	}
}