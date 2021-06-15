function checkDelete() {
	return window.confirm("削除してもよろしいですか？");
}
window.addEventListener('load',function(){
	//タイトルチェック
	let title = document.getElementById("title");
	let msgtitle = document.getElementById("msgtitle");

	title.onchange = function(){
		if(title.value ===""){
			msgtitle.innerHTML = "タイトルは必須項目です。";
		}else if(title.value.length < 2 || title.value.length > 30){
			msgtitle.innerHTML = "タイトルは2文字以上30文字以内です。";
		}else{
			msgtitle.innerHTML ="";
		}
	}
	//本文チェック
	let text = document.getElementById("text");
	let msgtext = document.getElementById("msgtext");

	text.onchange = function(){
		if(text.value ===""){
			msgtext.innerHTML = "本文は必須項目です。";
		}else if(text.value.length < 5 || text.value.length > 1000){
			msgtext.innerHTML = "本文は5文字以上1000文字以内です。";
		}else{
			msgtext.innerHTML ="";
		}
	}
	//送信時
	let edit = document.getElementById("edit");
	let msgedit = document.getElementById("msgedit");
	edit.onclick = function(){
		title.onchange();
		text.onchange();
		if(msgtitle.innerHTML === "" && msgtext.innerHTML === ""){
			msgedit.innerHTML ="";
			return window.confirm("この内容で変更してよろしいですか？");
		}else{
			msgedit.innerHTML = "未記入の項目があります。";
			alert("未記入の項目があります。");
			return false;
		}
	}
})