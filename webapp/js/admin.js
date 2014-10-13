/*
 * AdminLTE JS
 * 
 */
$(function() {
    "use strict";

    //Enable sidebar toggle
    $("[data-toggle='offcanvas']").click(function(e) {
        e.preventDefault();

        //If window is small enough, enable sidebar push menu
        if ($(window).width() <= 992) {
            $('.row-offcanvas').toggleClass('active');
            $('.left-side').removeClass("collapse-left");
            $(".right-side").removeClass("strech");
            $('.row-offcanvas').toggleClass("relative");
        } else {
            //Else, enable content streching
            $('.left-side').toggleClass("collapse-left");
            $(".right-side").toggleClass("strech");
        }
    });

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
     * ADD SLIMSCROLL TO THE TOP NAV DROPDOWNS
     * ---------------------------------------
     */
    $(".navbar .menu").slimscroll({
        height: "200px",
        alwaysVisible: false,
        size: "3px"
    }).css("width", "100%");

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

    /* Sidebar tree view */
    $(".sidebar .treeview").tree();

    /* 
     * Make sure that the sidebar is streched full height
     * ---------------------------------------------
     * We are gonna assign a min-height value every time the
     * wrapper gets resized and upon page load. We will use
     * Ben Alman's method for detecting the resize event.
     * 
     **/
    function _fix() {
        //Get window height and the wrapper height
        var height = $(window).height() - $("body > .header").height() - ($("body > .footer").outerHeight() || 0);
        $(".wrapper").css("min-height", height + "px");
        var content = $(".wrapper").height();
        //If the wrapper height is greater than the window
        if (content > height)
            //then set sidebar height to the wrapper
            $(".left-side, html, body").css("min-height", content + "px");
        else {
            //Otherwise, set the sidebar to the height of the window
            $(".left-side, html, body").css("min-height", height + "px");
        }
    }
    //Fire upon load
    _fix();
    //Fire when wrapper is resized
    $(".wrapper").resize(function() {
        _fix();
        fix_sidebar();
    });

    //Fix the fixed layout sidebar scroll bug
    fix_sidebar();

    /*
     * We are gonna initialize all checkbox and radio inputs to 
     * iCheck plugin in.
     * You can find the documentation at http://fronteed.com/iCheck/
     */
    $("input[type='checkbox']:not(.simple), input[type='radio']:not(.simple)").iCheck({
        checkboxClass: 'icheckbox_minimal',
        radioClass: 'iradio_minimal'
    });

});

/* Specific script for back office */
$( document ).ready(function( $ ) {
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
		