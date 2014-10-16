/*
 * AdminLTE JS
 * 
 */
$(function() {
    "use strict";

    //Add hover support for touch devices
    $('.btn').bind('touchstart', function() {
        $(this).addClass('hover');
    }).bind('touchend', function() {
        $(this).removeClass('hover');
    });

    //Activate tooltips
    $("[data-toggle='tooltip']").tooltip();

    /*     
     * Add collapse and remove events to boxes
     */
    $("[data-widget='collapse']").click(function() {
        //Find the box parent        
        var box = $(this).parents(".box").first();
        //Find the body and the footer
        var bf = box.find(".box-body, .box-footer");
        if (!box.hasClass("collapsed-box")) {
            box.addClass("collapsed-box");
            //Convert minus into plus
            $(this).children(".fa-minus").removeClass("fa-minus").addClass("fa-plus");
            bf.slideUp();
        } else {
            box.removeClass("collapsed-box");
            //Convert plus into minus
            $(this).children(".fa-plus").removeClass("fa-plus").addClass("fa-minus");
            bf.slideDown();
        }
    });

   
    /*
     * INITIALIZE BUTTON TOGGLE
     * ------------------------
     */
    $('.btn-group[data-toggle="btn-toggle"]').each(function() {
        var group = $(this);
        $(this).find(".btn").click(function(e) {
            group.find(".btn.active").removeClass("active");
            $(this).addClass("active");
            e.preventDefault();
        });

    });

    $("[data-widget='remove']").click(function() {
        //Find the box parent        
        var box = $(this).parents(".box").first();
        box.slideUp();
    });

   
});

/* Specific script for back office */
$( document ).ready(function( $ ) {

	// Count effect 
	$('.small-box .inner h3').each( function () {
		var nCounter = $(this).text();
		var sVal = "";
		var thisTXT = $(this).text().split("/");
		if ( thisTXT.length > 1 ){
			nCounter = thisTXT[0];
			sVal = " / " + thisTXT[1];
		} 
		if ( !isNaN( nCounter ) ) {
			$(this).prop('Counter',0).animate({
				Counter: nCounter
			}, {
				duration: 1000,
				easing: 'swing',
				step: function (now) {
					$(this).text( Math.ceil(now) + sVal );
				}
			});
		} 
	});

	if ( $("textarea#document_summary").length > 0 ){
		// Limit to 250 the text in summary
		var limit=250;
		$("textarea#document_summary").after("<span id=\"charLimit\"></span>");
		
		$("textarea#document_summary").keypress(function(e) {
			
		if( e.charCode >= 48 ){
			var nCar = $(this).val().length;
			if( nCar >= limit){
				$("span#charLimit").html("<span class=\"label label-danger\"><i class=\"fa fa-frown-o\"></i>&nbsp; Attention limite atteinte !</alert>");
				return false;
				} else {
					$("span#charLimit").html("<strong>" + eval(nCar + 1) + " caract&egrave;res</strong>");
				}
			}
		});
		
		$("textarea#document_summary").keyup(function(e) {
		if( e.keyCode == 8 ){
			var nCar = $(this).val().length;
			if( nCar >= limit){
				$("span#charLimit").html("<span class=\"label label-danger\"><i class=\"fa fa-frown-o\"></i>&nbsp; Attention limite atteinte !</alert>");
				return false;
				} else {
					$("span#charLimit").html("<strong>" + eval(nCar + 1) + " caract&egrave;res</strong>");
				}
			}
		});
		
		$("textarea#document_summary").blur(function(e) {
			var txt = $(this).val();
			if( txt.length> limit){
				$("span#charLimit").html("<span class=\"label label-danger\"><i class=\"fa fa-frown-o\"></i>&nbsp; Attention limite atteinte !</alert>");
				$(this).val( txt.substr(0,limit) );
				var txtLenght = $(this).val().length;
				$("span#charLimit").html("<strong>" + txtLenght + " caract&egrave;res</strong>");
			}
		});
	}
});
		