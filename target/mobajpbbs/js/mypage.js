//ランクロール送信時
function checkChange(){
	return window.confirm("変更してもよろしいですか？");
}
window.addEventListener('load',function(){

	//画像表示
	let upload = document.getElementById("upload");
	let image = document.getElementById("image");
	let msgimg = document.getElementById("msgimg");

	upload.onchange = function(){
		let file = upload.files[0];

		//許可する拡張子
		let extensionList = [".JPG", ".JEPG", ".PNG",".GIF",".BMP",".TIFF"];
		if(file === undefined){
			msgimg.innerHTML = "ファイルが選択されていません。";
		}else{
			let filename = file.name;
			let extension = filename.substring(filename.lastIndexOf(".")).toUpperCase();
			//ファイルのサイズ
			let maxSize = 1024 * 1024;//1MB
			if(extensionList.indexOf(extension) === -1){
				msgimg.innerHTML = "拡張子が許可されていません。";
			}else if(file.size > maxSize){
				msgimg.innerHTML = "画像のサイズは1MB以下です。";
			}else{
			// ファイルのブラウザ上でのURLを取得する
		    let blobUrl = window.URL.createObjectURL(file);
		    image.src = blobUrl;
			msgimg.innerHTML = "";
			}
		}
	}
	//画像送信時
	let imagechange =document.getElementById("imagechange");
	imagechange.onclick = function(){
		upload.onchange();
		if(msgimg.innerHTML !== ""){
			alert(msgimg.innerHTML);
		}else{
			return window.confirm("変更してもよろしいですか？");
		}
		return false;
	}

	//表示名チェック
	let name = document.getElementById("name");
	let msgname = document.getElementById("msgname");
	let loginUserName = name.value;

	name.onchange = function(){

		if(name.value ===loginUserName){
				msgname.innerHTML = "";
		}else if(name.value.length < 2 || name.value.length > 20){
			msgname.innerHTML = "表示名は2文字以上20文字以内です。";
		}else{
			//ID検索6

			let req = new Request(url+"/account/name",{
				method : "POST",
				body:"name="+name.value,
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
	//表示名送信時
	let namechange = document.getElementById("namechange");
	namechange.onclick = function(){
		name.onchange();
		if(name.value ===loginUserName){
			msgname.innerHTML = "現在の表示名と同じです。";
		}else if(msgname.innerHTML === ""){
			return window.confirm("変更してもよろしいですか？");
		}
		alert(msgname.innerHTML);
		return false;
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
			msgmail.innerHTML ="";
		}
	}
	//メールアドレス送信時
	let mailchange = document.getElementById("mailchange");
	mailchange.onclick = function(){
		mail.onchange();
		if(msgmail.innerHTML === ""){
			return window.confirm("変更してもよろしいですか？");
		}else{
			alert(msgmail.innerHTML);
			return false;
		}
	}

	//パスワードチェック
	let pass = document.getElementById("pass");
	let msgpass = document.getElementById("msgpass");
	let verification = document.getElementById("verification");
	let pattern =/^\w{6,20}$/;

	pass.onchange = function(){
		if(pattern.test(pass.value) === false){
			msgpass.innerHTML = "パスワードは半角英数字_6文字以上20文字以内です。";
		}else if(pass.value !== verification.value){
			msgpass.innerHTML = "パスワードが一致しません。";
		}else{
			msgpass.innerHTML = "";

		}
	verification.onchange= pass.onchange;
	}
	//パスワード送信時
	let passchange = document.getElementById("passchange");
	passchange.onclick = function(){
		pass.onchange();
		if(msgpass.innerHTML === ""){
			return window.confirm("変更してもよろしいですか？");
		}else{
			alert(msgpass.innerHTML);
			return false;
		}
	}

	//紹介文チェック
	let text = document.getElementById("text");
	let msgtext = document.getElementById("msgtext");
	let nowtext = text.value;
	text.onchange = function(){
		if(text.value ===""){
			msgtext.innerHTML = "";
		}else if(text.value.length < 5 || text.value.length > 1000){
			msgtext.innerHTML = "紹介文は5文字以上1000文字以内です。";
		}else{
			msgtext.innerHTML ="";
		}
	}
	//紹介文送信時
	let textchange = document.getElementById("textchange");
	textchange.onclick = function(){
		text.onchange();
		if(text.value === nowtext){
			msgtext.innerHTML = "現在の紹介文と同じです。";
		}else if(msgtext.innerHTML === ""){
			return window.confirm("変更してもよろしいですか？");
		}
			alert(msgtext.innerHTML);
			return false;

	}
})
