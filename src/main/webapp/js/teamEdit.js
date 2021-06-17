function checkChange(){
	return window.confirm("リーダーに任命してもよろしいですか？");
}
function checkDelete(){
	return window.confirm("追放するしてもよろしいですか？");
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
			alert("アップロード");
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
	if(name !== null){
		let teamName = name.value;
		name.onchange = function(){

			if(name.value === teamName){
				msgname.innerHTML = "";
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
		//表示名送信時
		let namechange = document.getElementById("namechange");
		namechange.onclick = function(){
			name.onchange();
			if(name.value ===teamName){
				msgname.innerHTML = "現在のチーム名と同じです。";
			}else if(msgname.innerHTML === ""){
				return window.confirm("変更してもよろしいですか？");
			}
			alert(msgname.innerHTML);
			return false;
		}
	}
	//紹介文チェック
	let text = document.getElementById("text");
	let msgtext = document.getElementById("msgtext");
	let nowtext = text.value;
	text.onchange = function(){
		alert();
		if(text.value ===""){
			msgtext.innerHTML = "";
		}else if(text.value.length < 5 || text.value.length > 1000){
			msgtext.innerHTML = "紹介分は5文字以上1000文字以内です。";
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
	//チームを抜ける時
	let leave = document.getElementById("leave");
	let loginUserid = document.getElementById("loginuserid").value;
	let readerid = document.getElementById("readerid").value;
	let msgleave = document.getElementById("msgleave");
	let menberCount = document.getElementsByClassName("menber").length;

	leave.onclick = function(){
		if(loginUserid === readerid){
			if(menberCount >1){
				msgleave.innerHTML = "脱退はリーダー譲渡後またメンバーが1人のみ場合可能です。";
			}else{
				return window.confirm("脱退後チームは自動的に解散されますがよろしいですか？");
			}
		}else{
			return window.confirm("脱退してもよろしいですか？");
		}
		alert(msgleave.innerHTML);
		return false;
	}
})