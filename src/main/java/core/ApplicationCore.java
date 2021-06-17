package core;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ApplicationCore {
	private HttpServletRequest request;
	private HttpServletResponse response;
	private Map<String, String> values = new HashMap<>();
	private ServletContext application;

	public ServletContext getApplication() {
		return this.application;
	}

	public void setApplication(ServletContext application) {
		this.application = application;
	}

	public ApplicationCore(HttpServletRequest request, HttpServletResponse response, String[] connInfo,
			ServletContext application) {
		this.request = request;
		this.response = response;
		this.application = application;
		//データベース情報を入れる
		DAOBase.setConnInfo(connInfo);
		String pathInfo = request.getRequestURI();
		String contextPath = request.getContextPath();
		pathInfo = pathInfo.substring(contextPath.length());
		System.out.println("URI:" + pathInfo);
		// URLを/ごとに区切る
		ArrayList<String> param = new ArrayList<>(Arrays.asList(pathInfo.split("/")));
		//デフォルトコントローラー名
		String controllerName = "controller.IndexController";
		//デフォルトアクション名
		String actionName = "IndexAction";
		int paramSize = param.size();
		System.out.println(String.valueOf("size:" + paramSize));
		//1文字目だけ大文字
		String fm = "%1S%s";
		//2以上ならコントローラー名にする
		if (paramSize > 1) {
			controllerName = "controller."
					+ String.format(fm, ((String) param.get(1)).substring(0, 1), ((String) param.get(1)).substring(1))
					+ "Controller";
		}
		//3以上ならコントローラー名にする
		if (paramSize > 2) {
			actionName = String.format(fm, ((String) param.get(2)).substring(0, 1),
					((String) param.get(2)).substring(1)) + "Action";
		}
		//4以上ならキーと値に設定してvaluesに入れる
		for (int i = 3; i + 1 < paramSize; i += 2) {
			this.values.put((String) param.get(i), (String) param.get(i + 1));
		}

		System.out.println("コントローラー名:" + controllerName);
		System.out.println("アクション名:" + actionName);

		try {
			//コントローラー名のクラスを探す
			Class<?> cls = Class.forName(controllerName);
			Class<?>[] args_types = new Class[]{ApplicationCore.class};
			Object[] args = new Object[]{this};
			//アクション名をメソッドにする
			Method method = cls.getMethod(actionName);
			method.invoke(cls.getDeclaredConstructor(args_types).newInstance(args));
		} catch (ClassNotFoundException e) {
			e.getStackTrace();
		} catch (Exception e) {
			e.getStackTrace();
		}

	}

	public Map<String, String> getValues() {
		return this.values;
	}

	public void setValues(Map<String, String> values) {
		this.values = values;
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
}