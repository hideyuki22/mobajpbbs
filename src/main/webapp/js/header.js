$(function(){
    $('.dropmenu .button').click(function(){
		if( $(".sub-nav").css("display") == "none"){
        	$(".sub-nav").slideDown();
		}else{
			$(".sub-nav").slideUp();
		}
    });
});
window.addEventListener('load',function(){
	var logout = document.getElementById("logout");
	if(logout != null){
		logout.onsubmit = function(){
			return window.confirm("ログアウトしてもよろしいですか？");
		}
	}
})
