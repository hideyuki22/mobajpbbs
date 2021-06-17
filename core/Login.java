package core;

import javax.servlet.http.HttpSession;

public class Login {
	private HttpSession session;

	public HttpSession getSession() {
		return this.session;
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}

	public Login(HttpSession session) {
		this.session = session;
	}

	public boolean checkLogin() {
		if("true".equals(this.session.getAttribute("login"))){
			return true;
		}
		return false;
	}

	public void logout() {
		this.session.setAttribute("login", "false");
	}

	public void login() {
		this.session.setAttribute("login", "true");
	}
}