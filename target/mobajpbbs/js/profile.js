$(function(){

    jQuery('.post').click(function() {
        location.href = jQuery(this).attr('data-url');
    });
    jQuery('.user').click(function() {
        location.href = jQuery(this).attr('data-url');
    });
})