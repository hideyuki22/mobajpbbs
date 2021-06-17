window.addEventListener('load',function(){
	//表示名チェック
	let name = document.getElementById("name");
	let msgname = document.getElementById("msgname");

	name.onchange = function(){

	if(name.value === ""){
		msgname.innerHTML = "チーム名は必須項目です。";
		}else if(name.value.length < 3 || name.value.length > 20){
			msgname.innerHTML = "チーム名は3文字以上20文字以内です。";
		}else{
			//ID検索6

			let req = new Request(url+"/team/name",{
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
							msgname.innerHTML = "チーム名は使用済みです。";
						}else{
							msgname.innerHTML = "";
						}
					});
				}
			});
		}
	}
	//画像表示
	let upload = document.getElementById("upload");
	let image = document.getElementById("image");
	let msgimg = document.getElementById("msgimg");

	upload.onchange = function(){
		let file = upload.files[0];
		//許可する拡張子
		let extensionList = [".JPG", ".JEPG", ".PNG",".GIF",".BMP",".TIFF"];
		if(file === undefined){
			msgimg.innerHTML = "";
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
	//紹介文チェック
	let text = document.getElementById("text");
	let msgtext = document.getElementById("msgtext");

	text.onchange = function(){
		if(text.value ===""){
			msgtext.innerHTML = "";
		}else if(text.value.length < 5 || text.value.length > 1000){
			msgtext.innerHTML = "紹介文は5文字以上1000文字以内です。";
		}else{
			msgtext.innerHTML ="";
		}
	}
	//送信時
	let register = document.getElementById("register");
	let msgreg = document.getElementById("msgreg");
	register.onsubmit = function(){
		name.onchange();
		upload.onchange();
		text.onchange();
		if(msgname.innerHTML === "" && msgimg.innerHTML === ""){
			msgreg.innerHTML = "";
			return window.confirm("この内容で登録してよろしいですか？");
		}

		msgreg.innerHTML = "未記入の項目があります。";
		alert("未記入の項目があります。");
		return false;
	}
})