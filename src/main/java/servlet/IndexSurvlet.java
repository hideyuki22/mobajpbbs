package servlet;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.ApplicationCore;


@WebServlet({"", "/index/*", "/account/*", "/post/*", "/user/*", "/edit/*", "/team/*"})

@MultipartConfig(
		location = "D:\\TEMP",//保存�?��?
		maxFileSize = 1024*1024*5,
		maxRequestSize = 1024*1024*5,
		fileSizeThreshold = 1024*1024*5 //5mb
		)
public class IndexSurvlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	   String url = "jdbc:postgresql://" + System.getenv("DATABASE_HOST") + ":" +
			   System.getenv("DATABASE_PORT") +"/" + System.getenv("DATABASE_NAME");

	   String user = System.getenv("DATABASE_USER");
	   String pass = System.getenv("DATABASE_PASSWORD");
	   String[] connInfo = new String[]{
				"org.postgresql.Driver",	//ドライバー
				url,//データベース
				user,//ユーザー
				pass//パスワード
		};
	/*String[] connInfo = new String[]{
			"org.h2.Driver",	//ドライバー
			"jdbc:h2:tcp://localhost/~/mobajpbbs",//データベース
			"sa",//ユーザー
			"ghui56"//パスワード
	};*/
	/*String[] connInfo = new String[]{
			"org.h2.Driver",	//ドライバー
			"jdbc:h2:tcp://localhost/~/test",//データベース
			"sa",//ユーザー
			""//パスワード
	};*/


	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.out.println(url);
		request.setCharacterEncoding("UTF-8");
		new ApplicationCore(request, response, this.connInfo, this.getServletContext());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}
}

