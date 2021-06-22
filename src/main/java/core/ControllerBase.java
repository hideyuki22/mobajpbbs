package core;

import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Team;
import model.User;

public abstract class ControllerBase {
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected Map<String, String> values;
	protected HttpSession session;
	protected Login login;
	protected ServletContext application;
	protected RequestDispatcher dispatcher;
	protected CsrfToken csrfToken;
	protected Map<String,String> AWSMap;

	public ControllerBase(ApplicationCore appCore) {
		//値を入れる
		this.request = appCore.getRequest();
		this.response = appCore.getResponse();
		//valuesの値のセット
		this.values = appCore.getValues();
		this.session = this.request.getSession();
		this.application = appCore.getApplication();
		//セッションからログイン状態を入れる
		this.login = new Login(this.session);
		//ベースURLのセット
		this.request.setAttribute("url", this.getBaseUrl());
		//セッションからcsrfトークンを入れる
		this.csrfToken = new CsrfToken(this.session);
		//AWSデータがあればセットする
		this.AWSMap = appCore.getAWSMap();
		ImageFile.setAWS_REGION(AWSMap.containsKey("AWS_REGION") ? AWSMap.get("AWS_REGION"):null);
		ImageFile.setAWS_ACCESS_KEY_ID(AWSMap.containsKey("AWS_ACCESS_KEY_ID") ? AWSMap.get("AWS_ACCESS_KEY_ID"):null);
		ImageFile.setAWS_SECRET_ACCESS_KEY(AWSMap.containsKey("AWS_SECRET_ACCESS_KEY") ? AWSMap.get("AWS_SECRET_ACCESS_KEY"):null);
		ImageFile.setAWS_BACKET_NAME(AWSMap.containsKey("AWS_BACKET_NAME") ? AWSMap.get("AWS_BACKET_NAME"):null);
		//UserとTeamのデフォル画像URL設定
		User.setDefaultImageUrl(getBaseUrl());
		Team.setDefaultImageUrl(getBaseUrl());


	}

	public void forward(String url) throws Exception {
		//urlにフォワード
		this.dispatcher = this.request.getRequestDispatcher(url);
		this.dispatcher.forward(this.request, this.response);
	}

	public HttpSession getSession() {
		return this.session;
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}

	public ServletContext getApplication() {
		return this.application;
	}

	public void setApplication(ServletContext application) {
		this.application = application;
	}

	public Login getLogin() {
		return this.login;
	}

	public void setLogin(Login login) {
		this.login = login;
	}

	public String getBaseUrl() {
		return "http://" + this.request.getServerName() + ":" + this.request.getServerPort()
				+ this.request.getContextPath();
	}
	public String getImageBaseUrl() {
		return "https://" + ImageFile.getAWS_BACKET_NAME() + ".s3." + ImageFile.getAWS_REGION() + ".amazonaws.com/";
	}

	public HttpServletRequest getRequest() {
		return this.request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return this.response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public Map<String, String> getValues() {
		return this.values;
	}

	public void setValues(Map<String, String> values) {
		this.values = values;
	}
}