package core;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.servlet.http.HttpSession;


public class CsrfToken {
	private static int TOKEN_LENGTH = 16;
	private Map<String, LinkedList<String>> tokens = new HashMap<>();
	private HttpSession session;


	@SuppressWarnings("unchecked")
	public CsrfToken(HttpSession session) {
		this.session = session;
		//セッションにトークンがあれが
		if (session.getAttribute("tokens") != null) {
			this.tokens = (Map<String, LinkedList<String>>) session.getAttribute("tokens");
		}

	}

	public String genarateToken(String tokenName) {
		byte[] tk = new byte[TOKEN_LENGTH];
		StringBuffer buf = new StringBuffer();
		SecureRandom random = null;

		try {
			random = SecureRandom.getInstance("SHA1PRNG");
			random.nextBytes(tk);

			for (int i = 0; i < tk.length; ++i) {
				buf.append(String.format("%02x", tk[i]));
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		LinkedList<String> tokenList = new LinkedList<>();
		//同じトークン名のリストがあれば取得
		if (this.tokens.containsKey(tokenName) && !this.tokens.get(tokenName).isEmpty()) {
			tokenList = (LinkedList<String>) this.tokens.get(tokenName);
		}
		//トークンのサイズが5以上なら1番目を削除
		if (tokenList.size() > 4) {
			tokenList.remove(0);
		}
		//リストの最後に登録
		tokenList.add(buf.toString());
		System.out.println("トークンサイズ:"+tokenList.size());
		this.tokens.put(tokenName, tokenList);
		//セッションにマップを戻す
		this.session.setAttribute("tokens", this.tokens);
		return buf.toString();
	}

	public Map<String, LinkedList<String>> getTokens() {
		return this.tokens;
	}

	public void setTokens(Map<String, LinkedList<String>> tokens) {
		this.tokens = tokens;
	}

	public boolean checkToken(String tokenName, String token) {
		if (this.tokens.containsKey(tokenName) && !((LinkedList<String>) this.tokens.get(tokenName)).isEmpty()) {
			int key = this.tokens.get(tokenName).indexOf(token);
			//トークンが存在すればそのトークンを削除してtrueを返す
			if (key != -1) {
				this.tokens.get(tokenName).remove(key);
				System.out.println(String.valueOf(this.tokens.get(tokenName).size())+ "個");
				this.session.setAttribute("tokens", this.tokens);
				return true;
			}
		}

		return false;
	}
}