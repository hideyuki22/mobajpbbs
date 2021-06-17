package core;

import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public abstract class ControllerBase {
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected Map<String, String> values;
	protected HttpSession session;
	protected Login login;
	protected ServletContext application;
	protected RequestDispatcher dispatcher;
	protected CsrfToken csrfToken;

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
	}

	public void forward(String url) throws Exception {
		//urlにフォワード
		this.dispatcher = this.request.getRequestDispatcher(url);
		System.out.println("フォワード:"+url);
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