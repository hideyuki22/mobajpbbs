//加入送信時
function checkJoin(){
	return window.confirm("加入してもよろしいですか？");
}
$(function(){

    jQuery('.menber').click(function() {
        location.href = jQuery(this).attr('data-url');
    });
})