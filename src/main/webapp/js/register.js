window.addEventListener('load',function(){
	//ログインIDチェック
	let loginid = document.getElementById("loginid");
	let msgid = document.getElementById("msgid");
	let pattern =/^\w{6,20}$/;

	loginid.onchange = function(){

		if(loginid.value === ""){
				msgid.innerHTML = "ログインIDは必須項目です。";
		}else if(pattern.test(loginid.value) === false){
			msgid.innerHTML = "ログインIDは半角英数字_6文字以上20文字以内です。";
		}else{
			//ID検索6

			let req = new Request(url+"/account/loginid",{
				method : "POST",
				body:"loginid="+loginid.value,
				headers: new Headers({'Content-type' : 'application/x-www-form-urlencoded' }),
			});

			fetch(req).then(function(response){
			    if (response.ok) {
			        console.log(response.url); //レスポンスのURL
			        console.log(response.status); //レスポンスのHTTPステータスコード
					response.text().then(function(text){

						if(text.trim() === "false"){
							msgid.innerHTML = "そのログインIDは使用済みです。";
						}else{
							msgid.innerHTML = "";
						}
					});
				}
			});
		}
	}

	//パスワードチェック
	let pass = document.getElementById("pass");
	let msgpass = document.getElementById("msgpass");
	let verification = document.getElementById("verification");
	let msgver = document.getElementById("msgver");
	pass.onchange = function(){
		if(pass.value === ""){
			msgpass.innerHTML = "パスワードは必須項目です。"
			msgver.innerHTML = "";
		}else if(pattern.test(pass.value) === false){
			msgpass.innerHTML = "パスワードは半角英数字_6文字以上20文字以内です。";
			msgver.innerHTML = "";
		}else if(pass.value !== verification.value){
			msgpass.innerHTML = "";
			msgver.innerHTML = "パスワードが一致しません。";
		}else{
			msgpass.innerHTML = "";
			msgver.innerHTML = "";
		}
	verification.onchange= pass.onchange;
	}

	//メールアドレスチェック
	let mail = document.getElementById("mail");
	let msgmail = document.getElementById("msgmail")
	let mailPattern = /^([a-zA-Z0-9])+([a-zA-Z0-9\._-])*@([a-zA-Z0-9_-])+([a-zA-Z0-9\._-]+)+$/;
	mail.onchange = function(){
		if(mail.value === ""){
			msgmail.innerHTML = "メールアドレスは必須項目です。";
		}else if(mailPattern.test(mail.value) === false){
			msgmail.innerHTML = "メールアドレスの値が不正です。";
		}else{
			msgmail.innerHTML = "";
		}
	}

	//表示名チェック
	let name = document.getElementById("name");
	let msgname = document.getElementById("msgname");

	name.onchange = function(){

		if(name.value === ""){
				msgname.innerHTML = "表示名は必須項目です。";
		}else if(name.value.length < 2 || name.value.length > 20){
			msgname.innerHTML = "表示名は2文字以上20文字以内です。";
		}else{
			//ID検索6
			let req = new Request(url+"/account/name",{
				method : "POST",
				body:"name="+name.values,
				headers: new Headers({'Content-type' : 'application/x-www-form-urlencoded' }),
			});

			fetch(req).then(function(response){
			    if (response.ok) {
			        console.log(response.url); //レスポンスのURL
			        console.log(response.status); //レスポンスのHTTPステータスコード
					response.text().then(function(text){

						if(text.trim() === "false"){
							msgname.innerHTML = "表示名は使用済みです。";
						}else{
							msgname.innerHTML = "";
						}
					});
				}
			});
		}
	}

	//送信時
	let register = document.getElementById("register");
	let msgreg = document.getElementById("msgreg");
	register.onsubmit = function(){
		loginid.onchange();
		pass.onchange();
		mail.onchange();
		name.onchange();
		if(msgid.innerHTML === "" && msgpass.innerHTML === "" && msgver.innerHTML === "" && msgmail.innerHTML === "" && msgname.innerHTML === ""){
			msgreg.innerHTML = "";
			return window.confirm("この内容で登録してよろしいですか？");
		}

		msgreg.innerHTML = "未記入の項目があります。";
		alert("未記入の項目があります。");
		return false;
	}
})
