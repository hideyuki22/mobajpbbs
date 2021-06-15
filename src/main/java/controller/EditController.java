package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import core.ApplicationCore;
import core.ControllerBase;
import dao.CommentDAO;
import dao.PostDAO;
import model.Comment;
import model.Post;
import model.User;

public class EditController extends ControllerBase {
	public EditController(ApplicationCore appCore) {
		super(appCore);
	}

	public void PostAction() throws Exception {
		//ログインしてなかったらトップページへ
		if (!this.login.checkLogin()) {
			this.response.sendRedirect(this.getBaseUrl());
			return;
		}
		//valuesにidがなかったらトップページへ
		if (!this.values.containsKey("id")) {
			this.response.sendRedirect(this.getBaseUrl());
			return;
		}
		//postidとログインユーザー取得
		int postid = Integer.parseInt(this.values.get("id"));
		PostDAO postDAO = new PostDAO();
		User loginUser = (User) this.session.getAttribute("loginUser");
		//ポストのユーザーIDとログインユーザーIDが一致してなかったらマイページへ
		if (!postDAO.CheckPostUser(postid, loginUser.getId())) {
			this.response.sendRedirect(this.getBaseUrl() + "/account/mypage");
		}

		//ポストとコメント取得
		Post post = postDAO.getPost(postid);
		this.request.setAttribute("post", post);
		CommentDAO cmDAO = new CommentDAO();
		ArrayList<Comment> cmList = cmDAO.FetchAllComment(postid);
		cmList = Comment.CreateTreeList(cmList);
		this.request.setAttribute("cmList", cmList);
		//トークン作成
		String editPostToken = this.csrfToken.genarateToken("editPost");
		this.request.setAttribute("editPostToken", editPostToken);
		this.forward("/WEB-INF/jsp/postEdit.jsp");
	}

	public void DropAction() throws Exception {
		//ログインしてなかったらトップページへ
		if (!this.login.checkLogin()) {
			this.response.sendRedirect(this.getBaseUrl());
			return;
		}
		//POSTかつ全て削除からではなかったらトップページへ
		if (!this.request.getMethod().equals("POST") || !"全て削除".equals(this.request.getParameter("drop"))) {
			this.response.sendRedirect(this.getBaseUrl());
			return;
		}
		//postidとログインユーザー情報取得
		User loginUser = (User) this.session.getAttribute("loginUser");
		int postid = Integer.valueOf(this.request.getParameter("postid"));
		PostDAO postDAO = new PostDAO();
		//ポストのユーザーIDとログインユーザーIDが一致してなかったらマイページへ
		if (!postDAO.CheckPostUser(postid, loginUser.getId())) {
			this.response.sendRedirect(this.getBaseUrl() + "/account/mypage");
			return;
		}
		//トークン確認してなかったら編集ページへ
		String token = this.request.getParameter("editPostToken");
		if (!this.csrfToken.checkToken("editPost", token)) {
			this.response.sendRedirect(this.getBaseUrl() + "/edit/post/id/" + postid);
			return;
		}
		//削除が成功したらマイページへ
		if (postDAO.DeletePost(postid)) {
			this.response.sendRedirect(this.getBaseUrl() + "/account/mypage");
			return;
		}
		//失敗したら編集ページへ
		this.response.sendRedirect(this.getBaseUrl() + "/edit/post/id/" + postid);
	}

	public void UpdateAction() throws Exception {
		//ログインしてなかったらトップページへ
		if (!this.login.checkLogin()) {
			this.response.sendRedirect(this.getBaseUrl());
			return;
		}
		//POSTまた変更するからじゃなかったらトップページへ
		if (!this.request.getMethod().equals("POST") || !"変更する".equals(this.request.getParameter("edit"))) {
			this.response.sendRedirect(this.getBaseUrl());
			return;
		}
		//ログインユーザーとpostid取得
		User loginUser = (User) this.session.getAttribute("loginUser");
		int postid = Integer.valueOf(this.request.getParameter("postid"));
		PostDAO postDAO = new PostDAO();
		//ポストとユーザーIDとログインユーザーIDが一致してなかったらマイページへ
		if (!postDAO.CheckPostUser(postid, loginUser.getId())) {
			this.response.sendRedirect(this.getBaseUrl() + "/account/mypage");
			return;
		}
		//トークン確認して一致しなかったら編集画面へ
		String token = this.request.getParameter("editPostToken");
		if (!this.csrfToken.checkToken("editPost", token)) {
			this.response.sendRedirect(this.getBaseUrl() + "/edit/post/id/" + postid);
		}
		Map<String, String> errors = new HashMap<>();
		Post post = postDAO.getPost(postid);
		String title = this.request.getParameter("title");
		String text = this.request.getParameter("text");
		//タイトルアップデート
		if (title == null || title.length() >= 2 && title.length() <= 30) {
			if (!title.equals(post.getTitle())) {
				postDAO.UpdateString("title", title, postid);
			}
		} else {
			errors.put("title", "タイトルは2文字以上30文字以内です。");
		}
		//テキストアップデート
		if (text == null || text.length() >= 5 && text.length() <= 1000) {
			if (!text.equals(post.getText())) {
				postDAO.UpdateString("text", text, postid);
			}
		} else {
			errors.put("text", "タイトルは5文字以上1000文字以内です。");
		}
		//エラーがなかったら編集画面へ
		if (errors.isEmpty()) {
			this.response.sendRedirect(this.getBaseUrl() + "/edit/post/id/" + postid);
			return;
		}
		//エラーとpostid登録してPostActionへ
		this.request.setAttribute("errors", errors);
		this.values.put("id", String.valueOf(postid));
		this.PostAction();
	}

	public void DeleteAction() throws Exception {
		//ログインしてなかったらトップページへ
		if (!this.login.checkLogin()) {
			this.response.sendRedirect(this.getBaseUrl());
			return;
		}
		//POSTまた削除するではなかったらトップページへ
		if (!this.request.getMethod().equals("POST") || !"削除する".equals(this.request.getParameter("delete"))) {
			this.response.sendRedirect(this.getBaseUrl());
			return;
		}
		//ログインユーザー情報とpostid
		User loginUser = (User) this.session.getAttribute("loginUser");
		int postid = Integer.valueOf(this.request.getParameter("postid"));
		int commentid = Integer.valueOf(this.request.getParameter("commentid"));
		PostDAO postDAO = new PostDAO();
		//ポストのユーザーIDとログインユーザーIDが一致してなかったらマイページへ
		if (!postDAO.CheckPostUser(postid, loginUser.getId())) {
			this.response.sendRedirect(this.getBaseUrl() + "/account/mypage");
			return;
		}
		//トークン確認して存在しなかったら編集ページへ
		String token = this.request.getParameter("editPostToken");
		if (!this.csrfToken.checkToken("editPost", token)) {
			this.response.sendRedirect(this.getBaseUrl() + "/edit/post/id/" + postid);
			return;
		}
		//コメントを削除して編集画面へ
		CommentDAO cmDAO = new CommentDAO();
		cmDAO.DeleteComment(postid, commentid);
		this.response.sendRedirect(this.getBaseUrl() + "/edit/post/id/" + postid);
	}
}