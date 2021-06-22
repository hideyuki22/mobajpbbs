package controller;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Part;

import core.ApplicationCore;
import core.ControllerBase;
import core.ImageFile;
import dao.PostDAO;
import dao.RankDAO;
import dao.RollDAO;
import dao.UserDAO;
import model.Post;
import model.User;

public class AccountController extends ControllerBase {
	public AccountController(ApplicationCore appCore) {
		super(appCore);
	}

	public void MypageAction() throws Exception {
		//ログインしてなかったらトップページへ
		if (!this.login.checkLogin()) {
			this.response.sendRedirect(this.getBaseUrl());
			return;
		}
		//セッションからログインユーザー取得
		User loginUser = (User) this.session.getAttribute("loginUser");
		this.request.setAttribute("user", loginUser);
		//ランクマップとロールマップ取得
		RankDAO rankDAO = new RankDAO();
		Map<Integer, String> ranks = rankDAO.getRankList();
		this.request.setAttribute("ranks", ranks);
		RollDAO rollDAO = new RollDAO();
		Map<Integer, String> rolls = rollDAO.getRollList();
		this.request.setAttribute("rolls", rolls);
		//pageが存在していたら
		int page = 0;
		if (this.values.containsKey("page")) {
			page = Integer.parseInt((String) this.values.get("page"));
			//pageが1以上なら
			if (page > 0) {
				int prevPage = page - 1;
				this.request.setAttribute("prevPage", prevPage);
			}
		}
		//ポストリスト取得
		PostDAO postDAO = new PostDAO();
		ArrayList<Post> postList = postDAO.FetchAllUserPost(page, 5, loginUser.getId());
		this.request.setAttribute("postList", postList);
		ArrayList<Post> nextList = postDAO.FetchAllUserPost(page + 1, 5, loginUser.getId());
		//次のポストリストが存在したら
		if (nextList.size() > 0) {
			int nextPage = page + 1;
			this.request.setAttribute("nextPage", nextPage);
		}
		//トークン作成
		String accountMypageToken = this.csrfToken.genarateToken("accountMypage");
		this.request.setAttribute("accountMypageToken", accountMypageToken);
		this.forward("/WEB-INF/jsp/mypage.jsp");
}

	public void UpdateAction() throws Exception {
		//ログインまたはPOSTではなかったらトップページへ
		if (!this.login.checkLogin() || !this.request.getMethod().equals("POST")) {
			this.response.sendRedirect(this.getBaseUrl());
			return;
		}
		//トークンが登録されていなかったらマイページへ
		String token = this.request.getParameter("accountMypageToken");
		if (!this.csrfToken.checkToken("accountMypage", token)) {
			this.response.sendRedirect(this.getBaseUrl() + "/account/mypage");
			return;
		}
		//エラーメッセージ用
		Map<String, String> errors = new HashMap<>();
		//ログインユーザーリスト取得
		User loginUser = (User) this.session.getAttribute("loginUser");
		UserDAO userDAO = new UserDAO();
		//valuesにitemがなかったらマイページへ
		if (!this.values.containsKey("item")) {
			this.response.sendRedirect(this.getBaseUrl() + "/account/mypage");
			return;
		}
		String item = this.values.get("item");

		switch (item) {
			case "mail" :
				//フォームの値と正規表現
				String mail = this.request.getParameter("mail");
				String mailPttern = "^([a-zA-Z0-9])+([a-zA-Z0-9\\._-])*@([a-zA-Z0-9_-])+([a-zA-Z0-9\\._-]+)+$";
				if (mail != null && !mail.matches(mailPttern)) {
					errors.put("mail", "メールアドレスの値が不正です。");
				} else if (mail.equals(loginUser.getMail())) {
					errors.put("mail", "現在のメールアドレスと同じです。");
				} else if (!userDAO.UpdateString("mail", mail, loginUser.getId())) {
					errors.put("name", "更新に失敗しました。");
				} else {
					loginUser.setMail(mail);
				}
				break;
			case "name" :

				//フォームの値
				String name = this.request.getParameter("name");
				if (name != null && (name.length() < 2 || name.length() > 20)) {
					errors.put("name", "表示名は2文字以上20文字以内です。");
				} else if (name.equals(loginUser.getName())) {
					errors.put("name", "現在の表示名と同じです。");
				} else if (userDAO.isName(name)) {
					errors.put("name", "表示名は使用済みです。");
				} else if (!userDAO.UpdateString("name", name, loginUser.getId())) {
					errors.put("name", "更新に失敗しました。");
				} else {
					loginUser.setName(name);
				}

				break;
			case "pass" :
				//フォームの値と正規表現
				String pass = this.request.getParameter("pass");
				String newpass = this.request.getParameter("newpass");
				String verification = this.request.getParameter("verification");
				String pattern = "^\\w{6,20}$";
				if (newpass != null && !newpass.matches(pattern)) {
					errors.put("pass", "パスワードは半角英数字_6文字以上20文字以内です。");
				} else if (!newpass.equals(verification)) {
					errors.put("pass", "パスワードが一致しません。");
				} else if (userDAO.CheckLogin(loginUser.getLoginid(), pass) == null) {
					errors.put("pass", "現在のパスワードが間違っています。");
				} else if (!userDAO.UpdateString("pass", newpass, loginUser.getId())) {
					errors.put("pass", "更新に失敗しました。");
				}

				break;
			case "rank" :
				if (this.request.getParameter("rank") != null) {
					//フォーム値
					int rankid = Integer.valueOf(this.request.getParameter("rank"));
					RankDAO rankDAO = new RankDAO();
					System.out.println("rank:" + rankid);
					if (loginUser.getRank() == rankid) {
						errors.put("rank", "現在のランクと同じです。");
					} else if (!rankDAO.isRank(rankid)) {
						errors.put("rank", "ランクの値が不正です。");
					} else if (!userDAO.UpdateInt("rank_id", rankid, loginUser.getId())) {
						errors.put("rank", "更新に失敗しました。");
					} else {
						loginUser.setRank(rankid);
					}
				}
				break;
			case "roll" :
				if (this.request.getParameter("roll") != null) {
					//フォーム値
					int rollid = Integer.valueOf(this.request.getParameter("roll"));
					RollDAO rollDAO = new RollDAO();
					if (loginUser.getRoll() == rollid) {
						errors.put("rank", "現在のロールと同じです。");
					} else if (!rollDAO.isRoll(rollid)) {
						errors.put("roll", "ロールの値が不正です。");
					} else if (!userDAO.UpdateInt("roll_id", rollid, loginUser.getId())) {
						errors.put("roll", "更新に失敗しました。");
					} else {
						loginUser.setRoll(rollid);
					}
				}
				break;
			case "text" :

				//フォーム値取得
				String text = this.request.getParameter("text");
				if ((text == null || "".equals(text) || text.length() >= 5) && text.length() <= 500) {
					if (text.equals(loginUser.getText())) {
						errors.put("text", "現在の紹介文と同じです。");
					} else if (!userDAO.UpdateString("text", text, loginUser.getId())) {
						errors.put("text", "更新に失敗しました。");
					} else {
						loginUser.setText(text);
					}
				} else {
					errors.put("text", "紹介文は5文字以上20文字以内です。");
				}
		}
		//エラーが存在しなかったらログインユーザー情報をセッションに登録してマイページへ
		if (errors.isEmpty()) {
			this.session.setAttribute("loginUser", loginUser);
			this.response.sendRedirect(this.getBaseUrl() + "/account/mypage");
			return;
		}
		//エラーが存在したら登録してMypageActionへ
		this.request.setAttribute("errors", errors);
		this.MypageAction();
	}

	public void UploadAction() throws Exception {

		//ログインしてなかったらトップページへ
		if (!this.login.checkLogin()) {
			this.response.sendRedirect(this.getBaseUrl());
			return;
		}
		//POSTからじゃなかったらトップページへ
		if (!this.request.getMethod().equals("POST")) {
			this.response.sendRedirect(this.getBaseUrl());
		}
		//エラー用変数
		HashMap<String,String> errors = new HashMap<>();

		Part part;
		try {
			int maxSize = 1024*1024; //1MB
			part = this.request.getPart("upload");
			System.out.println("partsize:" + part.getSize());
			//ファイルのサイズ0なら
			if (part.getSize() == 0) {
				errors.put("image", "ファイルが選択されていません。");
				this.request.setAttribute("errors", errors);
				this.MypageAction();
				return;

			}
			//ファイルが最大サイズを超えていたら
			if (part.getSize() > maxSize) {
				throw new Exception();
			}
		} catch (Exception e) {
			errors.put("image", "ファイルのサイズは1MB以下です。");
			this.request.setAttribute("errors", errors);
			this.MypageAction();
			return;
		}
		//トークンが存在しなかったらトップページへ
		String token = this.request.getParameter("accountMypageToken");
		if (!this.csrfToken.checkToken("accountMypage", token)) {
			this.response.sendRedirect(this.getBaseUrl());
			return;
		}

		try {
			UserDAO userDAO = new UserDAO();
			//ログインユーザー情報とアップロードされたファイル名
			User loginUser = (User) this.session.getAttribute("loginUser");
			String uploadname = Paths.get(part.getSubmittedFileName()).getFileName().toString();
			//拡張子チェック
			if (!ImageFile.CheckExtension(uploadname)) {
				throw new Exception("拡張子が対応しておりません。");
			}
			//保存するファイル名とパス
			String filename = ImageFile.GenerateFileName(loginUser.getId(), uploadname);
			String path = this.application.getRealPath("/upload") + File.separator + filename;
			part.write(path);

			//画像かチェック
			if (!ImageFile.CheckImage(path)) {
				ImageFile.DeleteImage(path);
				throw new Exception("アップロードされたファイルは画像ではありません。");
			}
			//画像のアップロード
			File file = new File(path);
			ImageFile.UploadFile(file);
			//pathの方の画像削除
			ImageFile.DeleteImage(path);
			//データベース更新
			if (!userDAO.UpdateString("image", this.getImageBaseUrl()+filename, loginUser.getId())) {
				ImageFile.DeleteImageFile(filename);
				throw new Exception("データベース更新に失敗しました");
			}
			//デフォルト画像ではなかったらS3の画像を削除する
			if (!loginUser.getImage().equals(loginUser.getDefaultImage())) {
				String[] parts = loginUser.getImage().split("/");
				String DeleteFileName = parts[parts.length-1];
				ImageFile.DeleteImageFile(DeleteFileName);
			}
			//ログインユーザー情報を更新してセッションに登録
			loginUser.setImage(getImageBaseUrl() + filename);
			this.session.setAttribute("loginUser", loginUser);
		} catch (Exception e) {
			//エラーメッセージを登録してMypageActoinへ
			errors.put("image", e.getMessage());
			this.request.setAttribute("errors", errors);
			this.MypageAction();
		}
		//成功したらマイページへ
		this.response.sendRedirect(this.getBaseUrl() + "/account/mypage");
	}

	public void RegisterAction() throws Exception {
		//ログインしてたらトップページへ
		if (this.login.checkLogin()) {
			this.response.sendRedirect(this.getBaseUrl());
		}
		//トークンを作成する
		String accountRegisterToken = this.csrfToken.genarateToken("accountRegister");
		this.request.setAttribute("accountRegisterToken", accountRegisterToken);
		this.forward("/WEB-INF/jsp/register.jsp");
	}

	public void InspectionAction() throws Exception {
		//ログインしてたらトップページへ
		if (this.login.checkLogin()) {
			this.response.sendRedirect(this.getBaseUrl());
			return;
		}

		//POSTまた新規登録でなかったらトップページへ
		if (!this.request.getMethod().equals("POST") || !"新規登録".equals(this.request.getParameter("register"))) {
			this.response.sendRedirect(this.getBaseUrl());
			return;
		}
		//トークンが登録されてなかったら登録画面へ
		String token = this.request.getParameter("accountRegisterToken");
		if (!this.csrfToken.checkToken("accountRegister", token)) {
				this.response.sendRedirect(this.getBaseUrl() + "/account/register");
				return;
		}
		//フォーム値とエラー用変数
		Map<String, String> errors = new HashMap<>();
		String loginid = this.request.getParameter("loginid");
		String pass = this.request.getParameter("pass");
		String verification = this.request.getParameter("verification");
		String mail = this.request.getParameter("mail");
		String name = this.request.getParameter("name");
		String pattern = "^\\w{6,20}$";
		UserDAO userDAO = new UserDAO();
		//ログインIDチェック
		if (loginid != null && !"".equals(loginid)) {
			if (!loginid.matches(pattern)) {
				errors.put("loginid", "ログインIDは半角英数字_6文字以上20文字以内です。");
			} else if (userDAO.isLoginid(loginid)) {
				errors.put("loginid", "そのログインIDは使用済みです。");
			}
		} else {
			errors.put("loginid", "ログインIDは必須項目です。");
		}
		//パスワードチェック
		if (pass != null && !"".equals(pass)) {
			if (!pass.matches(pattern)) {
				errors.put("pass", "パスワードは半角英数字_6文字以上20文字以内です。");
			} else if (!pass.equals(verification)) {
				errors.put("verification", "パスワードが一致しません。");
			}
		} else {
			errors.put("pass", "パスワードは必須項目です。");
		}
		//メールチェック
		String mailPttern = "^([a-zA-Z0-9])+([a-zA-Z0-9\\._-])*@([a-zA-Z0-9_-])+([a-zA-Z0-9\\._-]+)+$";
		if (mail != null && !"".equals(mail)) {
			if (!mail.matches(mailPttern)) {
				errors.put("mail", "メールアドレスの値が不正です。");
			}
		} else {
			errors.put("mail", "メールアドレスは必須項目です。");
		}
		//表示名チェック
		if (name != null && !"".equals(name)) {
			if (name.length() >= 2 && name.length() <= 20) {
				if (userDAO.isName(name)) {
					errors.put("name", "表示名は使用済みです。");
				}
			} else {
				errors.put("name", "表示名は2文字以上20文字以内です。");
			}
		} else {
			errors.put("name", "表示名は必須項目です。");
		}
		//エラーがなかったら登録
		if (errors.isEmpty()) {
			Map<String, String> userMap = new HashMap<>();
			userMap.put("loginid", loginid);
			userMap.put("pass", pass);
			userMap.put("mail", mail);
			userMap.put("name", name);
			//成功したらトップページへ
			if (userDAO.InsertUser(userMap)) {
				this.response.sendRedirect(this.getBaseUrl());
				return;
			}
		}
		//登録できなかったらフォーム値とエラーを登録する
		errors.put("submit", "登録に失敗しました");
		this.request.setAttribute("errors", errors);
		this.request.setAttribute("loginid", loginid);
		this.request.setAttribute("mail", mail);
		this.request.setAttribute("name", name);
		this.RegisterAction();
	}
	//Ajax用ログインID検索
	public void LoginidAction() throws Exception {
		//POST以外だったらトップページへ
		if (!this.request.getMethod().equals("POST")) {
			this.response.sendRedirect(this.getBaseUrl());
		}

		String loginid = this.request.getParameter("loginid");

		UserDAO userDAO = new UserDAO();
		String data;
		if (userDAO.isLoginid(loginid)) {
			data = "false";
		} else {
			data = "true";
		}

		this.request.setAttribute("data", data);
		this.forward("/WEB-INF/jsp/ajax.jsp");
	}
	//Ajax用ログインID検索
	public void NameAction() throws Exception {
		//POST以外だったらトップページへ
		if (!this.request.getMethod().equals("POST")) {
			this.response.sendRedirect(this.getBaseUrl());
		}

		String name = this.request.getParameter("name");
		UserDAO userDAO = new UserDAO();
		String data;
		if (userDAO.isName(name)) {
			data = "false";
		} else {
			data = "true";
		}

		this.request.setAttribute("data", data);
		this.forward("/WEB-INF/jsp/ajax.jsp");
	}
}