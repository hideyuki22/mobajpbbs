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
import dao.TeamDAO;
import dao.UserDAO;
import model.Team;
import model.User;

public class TeamController extends ControllerBase {
	public TeamController(ApplicationCore appCore) {
		super(appCore);
	}

	public void RegisterAction() throws Exception {
		//ログインしてなかったらトップページへ
		if (!this.login.checkLogin()) {
			this.response.sendRedirect(this.getBaseUrl());
			return;
		}
		//トークン作成
		String teamRegisterToken = this.csrfToken.genarateToken("teamRegister");
		this.request.setAttribute("teamRegisterToken", teamRegisterToken);
		this.forward("/WEB-INF/jsp/teamRegister.jsp");
	}

	public void UpdateAction() throws Exception {
		//ログインしてなかったらトップページへ
		if (!this.login.checkLogin()) {
			this.response.sendRedirect(this.getBaseUrl());
			return;
		}
		//ログインユーザーがチームに所属してなかったらトップページへ
		User loginUser = (User) this.session.getAttribute("loginUser");
		if (loginUser.getTeamid() == 0) {
			this.response.sendRedirect(this.getBaseUrl());
			return;
		}
		//POSTからじゃなかったらチームページへ
		if (!this.request.getMethod().equals("POST")) {
			this.response.sendRedirect(this.getBaseUrl() + "/team/edit");
			return;
		}
		//エラー用変数
		Map<String, String> errors = new HashMap<>();
		//valuesにitemがなかったらチームページへ
		if (!this.values.containsKey("item")) {
			this.response.sendRedirect(this.getBaseUrl() + "/team/edit");
			return;
		}
		//チーム情報取得
		TeamDAO teamDAO = new TeamDAO();
		Team team = teamDAO.getTeam(loginUser.getTeamid());
		String item = (String) this.values.get("item");
		switch (item) {
			case "reader" :
				//ログインユーザーがリーダーじゃなかったらチームページへ
				if (loginUser.getId() != team.getReader().getId()) {
					this.response.sendRedirect(this.getBaseUrl() + "/team/edit");
					return;
				}
				//譲渡するユーザーID取得
				int userid = Integer.valueOf(this.request.getParameter("userid"));
				UserDAO userDAO = new UserDAO();
				//ユーザーIDがリーダーじゃないかつチームに所属してるか確認
				if (userid != team.getReader().getId() && userDAO.isTeamUser(userid, team.getId())) {
					teamDAO.UpdateInt("reader_id", userid, team.getId());
				}
				break;
			case "name" :
				//リーダーじゃなかったらチームページへ
				if (loginUser.getId() != team.getReader().getId()) {
					this.response.sendRedirect(this.getBaseUrl() + "/team/edit");
					return;
				}
				//チーム名チェック
				String name = this.request.getParameter("name");
				if (name != null && name.length() >= 3 && name.length() <= 20) {
					if (name == team.getName()) {
						errors.put("name", "現在のチーム名と同じです。");
					} else if (teamDAO.isName(name)) {
						errors.put("name", "チーム名は使用済みです。");
					} else if (!teamDAO.UpdateString("name", name, team.getId())) {
						errors.put("name", "更新に失敗しました。");
					}
				} else {
					errors.put("name", "チーム名は3文字以上20文字以内です。");
				}
				break;
			case "text" :
				//紹介文チェック
				String text = this.request.getParameter("text");
				if ((text == null || "".equals(text) || text.length() >= 5) && text.length() <= 500) {
					if (text.equals(team.getText())) {
						errors.put("text", "現在の紹介文と同じです。");
					} else if (!teamDAO.UpdateString("text", text, team.getId())) {
						errors.put("text", "更新に失敗しました。");
					}
				} else {
					errors.put("text", "紹介文は5文字以上20文字以内です。");
				}
		}
		//エラーがなかったらチームページへ
		if (errors.isEmpty()) {
			this.response.sendRedirect(this.getBaseUrl() + "/team/edit");
			return;
		}
		//エラーを登録してEditActionへ
		this.request.setAttribute("errors", errors);
		this.EditAction();
	}

	public void RemoveAction() throws Exception {
		//ログインしてなかったらトップページへ
		if (!this.login.checkLogin()) {
			this.response.sendRedirect(this.getBaseUrl());
			return;
		}
		//ログインユーザーがチームに所属してなかったらトップページへ
		User loginUser = (User) this.session.getAttribute("loginUser");
		if (loginUser.getTeamid() == 0) {
			this.response.sendRedirect(this.getBaseUrl());
			return;
		}
		//POSTまた追放するからじゃなかったらチームページへ
		if (!this.request.getMethod().equals("POST") || !"追放する".equals(this.request.getParameter("remove"))) {
			this.response.sendRedirect(this.getBaseUrl() + "/team/edit");
			return;
		}
		//トークン確認して存在しなかったらチームページへ
		String token = this.request.getParameter("teamEditToken");
		if (!this.csrfToken.checkToken("teamEdit", token)) {
			this.response.sendRedirect(this.getBaseUrl() + "/team/edit");
			return;
		}
		//ユーザーIDがリーダーじゃなかったらチームページへ
		TeamDAO teamDAO = new TeamDAO();
		Team team = teamDAO.getTeam(loginUser.getTeamid());
		if (loginUser.getId() != team.getReader().getId()) {
			this.response.sendRedirect(this.getBaseUrl() + "/team/edit");
			return;
		}
		UserDAO userDAO = new UserDAO();
		int userid = Integer.valueOf(this.request.getParameter("userid"));
		//ユーザーIDがリーダーじゃないかつチームに所属してるか確認
		if (userid != team.getReader().getId() && userDAO.isTeamUser(userid, team.getId())) {
			userDAO.UpdateNull("team_id", userid);
		}
		//チームページへ
		this.response.sendRedirect(this.getBaseUrl() + "/team/edit");
}

	public void LeaveAction() throws Exception {
		//ログインしてなかったらトップページへ
		if (!this.login.checkLogin()) {
			this.response.sendRedirect(this.getBaseUrl());
			return;
		}
		//ログインユーザーがチームに所属してなかったらトップページへ
		User loginUser = (User) this.session.getAttribute("loginUser");
		if (loginUser.getTeamid() == 0) {
			this.response.sendRedirect(this.getBaseUrl());
			return;
		}
		//POSTまたチームから脱退するからじゃなかったらチームページへ
		if (!this.request.getMethod().equals("POST")
				|| !"チームから脱退する".equals(this.request.getParameter("leave"))) {
			this.response.sendRedirect(this.getBaseUrl() + "/team/edit");
			return;
		}
		//トークン確認して存在しなかったらチームページへ
		String token = this.request.getParameter("teamEditToken");
		if (!this.csrfToken.checkToken("teamEdit", token)) {
			this.response.sendRedirect(this.getBaseUrl() + "/team/edit");
		}
		//チームとチームユーザーリスト
		TeamDAO teamDAO = new TeamDAO();
		Team team = teamDAO.getTeam(loginUser.getTeamid());
		UserDAO userDAO = new UserDAO();
		ArrayList<User> userList = userDAO.getTeamUserList(loginUser.getTeamid());
		this.request.setAttribute("userList", userList);
		//リーダーの場合は最後1人の場合のみできる
		if (loginUser.getId() == team.getReader().getId() && userList.size() != 1) {
			Map<String, String> errors = new HashMap<>();
			errors.put("leave", "脱退はリーダー譲渡後またメンバーが1人のみ場合可能です。");
			this.request.setAttribute("errors", errors);
			this.EditAction();
		} else {
			userDAO.UpdateNull("team_id", loginUser.getId());
			loginUser.setTeamid(0);
			this.session.setAttribute("loginUser", loginUser);
			//チームのユーザーが0になる場合はチームを削除する
			if (userList.size() == 1) {
				//チーム画像がデフォルじゃなかったら削除
				if (!team.getImage().equals(team.getDefaultImage())) {
					String[] parts = team.getImage().split("/");
					String DeleteFileName = parts[parts.length-1];
					ImageFile.DeleteImageFile(DeleteFileName);
				}

				teamDAO.DeleteTeam(team.getId());
			}
			//トップページへ
			this.response.sendRedirect(this.getBaseUrl());
		}
	}

	public void UploadAction() throws Exception {
		//ログインしてなかったらトップページへ
		if (!this.login.checkLogin()) {
			this.response.sendRedirect(this.getBaseUrl());
			return;
		}
		//ログインユーザーがチームに所属してなかったらトップページへ
		User loginUser = (User) this.session.getAttribute("loginUser");
		if (loginUser.getTeamid() == 0) {
			this.response.sendRedirect(this.getBaseUrl());
			return;
		}
		//POSTするからじゃなかったらチームページへ
		if (!this.request.getMethod().equals("POST")) {
			this.response.sendRedirect(this.getBaseUrl() + "/team/edit");
			return;
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
				this.EditAction();
				return;

			}
			//ファイルが最大サイズを超えていたら
			if (part.getSize() > (long) maxSize) {
				throw new Exception();
			}
		} catch (Exception e) {
			errors.put("image", "ファイルのサイズは1MB以下です。");
			this.request.setAttribute("errors", errors);
			this.EditAction();
			return;
		}
		//トークンが存在しなかったらチームページへ
		String token = this.request.getParameter("teamEditToken");
		if (!this.csrfToken.checkToken("teamEdit", token)) {
			this.response.sendRedirect(this.getBaseUrl() + "/team/edit");
			return;
		}

		try {
			//アップロードされたファイル名
			String uploadname  = Paths.get(part.getSubmittedFileName()).getFileName().toString();
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
			TeamDAO teamDAO = new TeamDAO();
			Team team = teamDAO.getTeam(loginUser.getTeamid());
			if (!teamDAO.UpdateString("image", this.getImageBaseUrl()+filename, loginUser.getTeamid())) {
				ImageFile.DeleteImageFile(filename);
				throw new Exception("データベース更新に失敗しました");
			}
			//デフォルト画像ではなかったら削除する
			if (!team.getImage().equals(team.getDefaultImage())) {
				String[] parts = team.getImage().split("/");
				String DeleteFileName = parts[parts.length-1];
				ImageFile.DeleteImageFile(DeleteFileName);
			}
		} catch (Exception e) {
			//エラーメッセージを登録してEditActionへ
			errors.put("image",e.getMessage());
			this.request.setAttribute("errors", errors);
			this.EditAction();
			return;
		}
		//トークンを作成
		String teamRegisterToken = this.csrfToken.genarateToken("teamRegister");
		this.request.setAttribute("teamRegisterToken", teamRegisterToken);
		this.response.sendRedirect(this.getBaseUrl() + "/team/edit");
	}

	public void EditAction() throws Exception {
		//ログインしてなかったらトップページへ
		if (!this.login.checkLogin()) {
			this.response.sendRedirect(this.getBaseUrl());
			return;
		}
		//ログインユーザーがチームに所属してなかったらトップページへ
		User loginUser = (User) this.session.getAttribute("loginUser");
		if (loginUser.getTeamid() == 0) {
			this.response.sendRedirect(this.getBaseUrl());
			return;
		}
		//チーム情報とユーザーリスト取得
		TeamDAO teamDAO = new TeamDAO();
		Team team = teamDAO.getTeam(loginUser.getTeamid());
		this.request.setAttribute("team", team);
		UserDAO userDAO = new UserDAO();
		ArrayList<User> userList = userDAO.getTeamUserList(loginUser.getTeamid());
		this.request.setAttribute("userList", userList);
		//トークン作成
		String teamEditToken = this.csrfToken.genarateToken("teamEdit");
		this.request.setAttribute("teamEditToken", teamEditToken);
		this.forward("/WEB-INF/jsp/teamEdit.jsp");

	}

	public void InspectionAction() throws Exception {
		//
		if (!this.request.getMethod().equals("POST") ||
				!"チーム登録".equals(this.request.getParameter("register"))) {
			this.response.sendRedirect(this.getBaseUrl());
			return;
		}
		Map<String, String> errors = new HashMap<>();
		String name = this.request.getParameter("name");
		String text = this.request.getParameter("text");
		TeamDAO teamDAO = new TeamDAO();

		if (name == null || "".equals(name)) {
			errors.put("name", "チーム名は必須項目です。");
		}else if (name.length() < 3 || name.length() > 20) {
			errors.put("name", "チーム名は3文字以上20文字以内です。");
		}else if (teamDAO.isName(name)) {
					errors.put("name", "チーム名は使用済みです。");
		}


		if (text != null && !"".equals(text) && (text.length() < 5 || text.length() > 500)) {
			errors.put("text", "紹介文は5文字以上20文字以内です。");
		}

		Part part =null;

		try {
			int maxSize = 1024*1024; //1MB
			part = this.request.getPart("upload");
			//ファイルが最大サイズを超えていたら
			if (part.getSize() > (long) maxSize) {

				throw new Exception();
			}
		} catch (Exception var12) {
			errors.put("image", "ファイルのサイズは1MB以下です。");
		}
		//トークンが存在しなかったらトップページへ
		String token = this.request.getParameter("teamRegisterToken");
		if (errors.isEmpty() && !this.csrfToken.checkToken("teamRegister", token)) {
			this.response.sendRedirect(this.getBaseUrl());
			return;
		}
		//ログインユーザー情報
		User loginUser = (User) this.session.getAttribute("loginUser");
		String filename = "";
		//エラーがないかつ画像が存在したら
		if (errors.isEmpty() && part != null && part.getSize() > 0) {
			try {
				//アップロードされたファイル名
				String uploadname = Paths.get(part.getSubmittedFileName()).getFileName().toString();
				//拡張子チェック
				if (!ImageFile.CheckExtension(uploadname)) {
					throw new Exception("拡張子が対応しておりません。");
				}
				//保存するファイル名とパス
				filename = ImageFile.GenerateFileName(loginUser.getId(), uploadname);
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
			} catch (Exception e) {
				errors.put("image", e.getMessage());
			}
		}
		//エラーがなかったらSQL更新
		if (errors.isEmpty()) {
			Map<String, String> teamMap = new HashMap<>();
			teamMap.put("userid", String.valueOf(loginUser.getId()));
			teamMap.put("name", name);
			teamMap.put("text", text);
			teamMap.put("image",!"".equals(filename) ? this.getImageBaseUrl()+filename : "");
			//ログイン情報更新
			if (teamDAO.InsertTeam(teamMap)) {
				UserDAO userDAO = new UserDAO();
				loginUser = userDAO.getUser(loginUser.getId());
				this.session.setAttribute("loginUser", loginUser);
				this.response.sendRedirect(this.getBaseUrl());
				return;
			}

			errors.put("submit", "登録に失敗しました");
		}
		//エラーとフォーム値を登録してRegisterActionへ
		this.request.setAttribute("errors", errors);
		this.request.setAttribute("name", name);
		this.request.setAttribute("text", text);
		this.RegisterAction();
	}

	public void ShowAction() throws Exception {
		//valuesにidが存在しなかったらトップページへ
		if (!this.values.containsKey("id")) {
			this.response.sendRedirect(this.getBaseUrl());
			return;
		}
		//teamidからチーム情報取得
		int teamid = Integer.parseInt((String) this.values.get("id"));
		TeamDAO teamDAO = new TeamDAO();
		Team team = teamDAO.getTeam(teamid);
		//チーム情報が存在しなかったらトップページへ
		if (team == null) {
			this.response.sendRedirect(this.getBaseUrl());
			return;
		}
		//チームのユーザーリスト取得
		this.request.setAttribute("team", team);
		UserDAO userDAO = new UserDAO();
		ArrayList<User> userList = userDAO.getTeamUserList(teamid);
		this.request.setAttribute("userList", userList);
		//トークン作成
		String teamJoinToken = this.csrfToken.genarateToken("teamJoin");
		this.request.setAttribute("teamJoinToken", teamJoinToken);
		//ログインしてなかったらトークン作成
		if (!this.login.checkLogin()) {
			String indexLoginToken = this.csrfToken.genarateToken("indexLogin");
			this.request.setAttribute("indexLoginToken", indexLoginToken);
		}

		this.forward("/WEB-INF/jsp/teamShow.jsp");
	}

	public void JoinAction() throws Exception {
		int teamid = Integer.parseInt(this.request.getParameter("teamid"));
		TeamDAO teamDAO = new TeamDAO();
		//チームが存在しなかったらトップページへ
		if (!teamDAO.isTeam(teamid)) {
			this.response.sendRedirect(this.getBaseUrl());
			return;
		}
		//ログインしてなかったらチームページへ
		if (!this.login.checkLogin()) {
			this.response.sendRedirect(this.getBaseUrl() + "/team/show/id/" + teamid);
			return;
		}
		//POSTまた加入するからでなかったらチームページへ
		if (!this.request.getMethod().equals("POST") || !"加入する".equals(this.request.getParameter("join"))) {
			this.response.sendRedirect(this.getBaseUrl() + "/team/show/id/" + teamid);
			return;
		}
		//トークンが存在しなかったチームページへ
		String token = this.request.getParameter("teamJoinToken");
		if (!this.csrfToken.checkToken("teamJoin", token)) {
			this.response.sendRedirect(this.getBaseUrl() + "/team/show/id/" + teamid);
			return;
		}
		//ログインユーザーがチームに所属していたらチームページへ
		User loginUser = (User) this.session.getAttribute("loginUser");
		if (loginUser.getTeamid() != 0) {
			this.response.sendRedirect(this.getBaseUrl() + "/team/show/id/" + teamid);
			return;
		}
		//SQL更新してセッションを更新する
		UserDAO userDAO = new UserDAO();
		userDAO.UpdateInt("team_id", teamid, loginUser.getId());
		loginUser.setTeamid(teamid);
		this.session.setAttribute("loginUser", loginUser);
		this.response.sendRedirect(this.getBaseUrl() + "/team/show/id/" + teamid);


	}
	//AJAX用
	public void NameAction() throws Exception {
		if (!this.request.getMethod().equals("POST")) {
			this.response.sendRedirect(this.getBaseUrl());
			return;
		}
		//nameが存在していたらtrue
		String name = this.request.getParameter("name");
		TeamDAO teamDAO = new TeamDAO();
		String data;
		if (teamDAO.isName(name)) {
			data = "false";
		} else {
			data = "true";
		}

		this.request.setAttribute("data", data);
		this.forward("/WEB-INF/jsp/ajax.jsp");
	}
}