package servlet;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.ApplicationCore;


@WebServlet({"", "/index/*", "/account/*", "/post/*", "/user/*", "/edit/*", "/team/*"})

@MultipartConfig(
		maxFileSize = 1024*1024*5,
		maxRequestSize = 1024*1024*5,
		fileSizeThreshold = 1024*1024*10 //10MB
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

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		Map<String,String> AWSMap= new HashMap<>();
		AWSMap.put("AWS_REGION",System.getenv("AWS_REGION"));
		AWSMap.put("AWS_ACCESS_KEY_ID",System.getenv("AWS_ACCESS_KEY_ID"));
		AWSMap.put("AWS_SECRET_ACCESS_KEY",System.getenv("AWS_SECRET_ACCESS_KEY"));
		AWSMap.put("AWS_BACKET_NAME","mobajpbbs");
		new ApplicationCore(request, response, this.connInfo,AWSMap, this.getServletContext());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}
}

