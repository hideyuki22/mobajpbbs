window.addEventListener('load',function(){
	//コメントチェ�?ク
	let text = document.getElementById("text");
	let msgtext = document.getElementById("msgtext");

	text.onchange = function(){
		if(text.value.length < 5 || text.value.length > 1000){
			msgtext.innerHTML = "コメント5文字以上1000文字以内です。";
		}else{
			msgtext.innerHTML ="";
		}
	}
	//送信�?
	let comment = document.getElementById("comment");
	comment.onsubmit = function(){
		text.onchange();
		if(msgtext.innerHTML === ""){
			return window.confirm("コメントしてよろしいですか?");
		}
		alert(msgtext.innerHTML);
		return false;
	}
})
$(function(){
		let textarea = document.getElementById("text");
	$(".comment-text").each(function(){
		let text = $(this).html();
		let regex = /(&gt;&gt;\d+)/g;

		$(this).html(text.replace(regex,"<span class=\"blue\">$1</span>"));
	});
	$(".blue").click(function(){
		let className =  "#comment_"+$(this).text().substr(2);
   		$("html,body").animate({scrollTop:$(className).offset().top},200);
	});
		$(".reply").click(function(){
		let id = $(this).parent("tr").children("td:nth-child(1)").text();

   		$("html,body").animate({scrollTop:$(".comment").offset().top},200);

		let text = textarea.value;
		let regex = /^(>>\d+)/;
		if(regex.test(text)){
			text = ">>"+id+text;
		}else{
			text=">>"+id+"\n"+text;
		}
		textarea.value = text;
	});
})