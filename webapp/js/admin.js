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
	
	// File Input Style 
	$(":file").addClass("filestyle");
	$(":file").filestyle({buttonText: "&nbsp;Parcourir"});
	
	/** 	MENUS		 **/
	/** MENU TO ACTIVATE **/
		if( $("header").attr("data-menu")=="top" ){
			$("#menu-top").attr("id","menu-right");
		} else if( $("header").attr("data-menu")=="right") {
			$("#top").toggle();
			$("#menu-right").toggleClass("visible-xs");
		} else {
			$("#menu-left").toggleClass("visible-xs");
			$("#top").toggle();
		}
		
	/** MENU RIGHT **/
		var $lateral_menu_trigger = $('#menu-right'),$content_wrapper = $('#admin-wrapper'),$navigation = $('header');

		//open-close lateral menu clicking on the menu icon
		$lateral_menu_trigger.on('click', function(event){
			event.preventDefault();
				
			$lateral_menu_trigger.toggleClass('is-clicked');
			$navigation.toggleClass('lateral-menu-is-open');
			$content_wrapper.toggleClass('lateral-menu-is-open').one('webkitTransitionEnd otransitionend oTransitionEnd msTransitionEnd transitionend', function(){
				// firefox transitions break when parent overflow is changed, so we need to wait for the end of the trasition to give the body an overflow hidden
				$('body').toggleClass('overflow-hidden');
			});
			$('#cd-lateral-nav').toggleClass('lateral-menu-is-open');
			
			//check if transitions are not supported - i.e. in IE9
			if($('html').hasClass('no-csstransitions')) {
				$('body').toggleClass('overflow-hidden');
			}
		});

		//close lateral menu clicking outside the menu itself
		$content_wrapper.on('click', function(event){
		if( !$(event.target).is('#menu-right, #menu-right span') ) {
				$lateral_menu_trigger.removeClass('is-clicked');
				$navigation.removeClass('lateral-menu-is-open');
				$content_wrapper.removeClass('lateral-menu-is-open').one('webkitTransitionEnd otransitionend oTransitionEnd msTransitionEnd transitionend', function(){
					$('body').removeClass('overflow-hidden');
				});
				$('#cd-lateral-nav').removeClass('lateral-menu-is-open');
				//check if transitions are not supported
				if($('html').hasClass('no-csstransitions')) {
					$('body').removeClass('overflow-hidden');
				}
			}
		});

		//open (or close) submenu items in the lateral menu. Close all the other open submenu items.
		$('.item-has-children').children('a').on('click', function(event){
			event.preventDefault();
			$(this).toggleClass('submenu-open').next('.sub-menu').slideToggle(200).end().parent('.item-has-children').siblings('.item-has-children').children('a').removeClass('submenu-open').next('.sub-menu').slideUp(200);
		});
		
	/** END MENU RIGHT **/		
	
	/** MENU LEFT **/	
		// Off Canvas Menu Begin
		// Calling the function
		$(function() {
			$('#menu-left').click(function( e ) {
				toggleNavigation();
				e.preventDefault();
			});
			
			$('#nav>h2#title').click(function( e ) {
				toggleNavigation();
			});
		});

		// The toggleNav function itself
		function toggleNavigation() {
			if ($('#admin-wrapper').hasClass('display-nav')) {
				// Close Nav
				$('#admin-wrapper').removeClass('display-nav');
			} else {
				// Open Nav
				$('#admin-wrapper').addClass('display-nav');
			}
		}

		// Sliding codes
		$("#toggle > li > div").click(function (e) {
			e.preventDefault();
			if (false == $(this).next().is(':visible')) {
				$('#toggle ul').slideUp();
			}
			var $currIcon=$(this).find("span.the-btn");
			$("span.the-btn").not($currIcon).addClass('fa-plus').removeClass('fa-minus');
			$currIcon.toggleClass('fa-minus fa-plus');
			$(this).next().slideToggle();
			$("#toggle > li > div").removeClass("active");
			$(this).addClass('active');
		});
		// Off Canvas Menu End

	/** END MENU LEFT **/	
	/** 	END MENUS		 **/	
	
	$(".portlet-type").on('click', function(e) {
		// Stop the link default behaviour.
		e.preventDefault();
		// Set the iframe src with the clicked link href.
		$('#preview').attr('src', $(this).children().attr('href') );
	});
	
	// Admin Preview fullscreen
	if ( $("#fullscreen").length > 0 ){
		$("#fullscreen").on('click', function(e) {
			// Stop the link default behaviour.
			e.preventDefault();
			// Set the iframe src with the clicked link href.
			$('body').toggleClass("bs-fixed-body");
			$('.content-header').toggle();
			$('.page-header').toggle();
			$('header').toggle();
			$(this).children().toggleClass('glyphicon-fullscreen');
			$(this).children().toggleClass('glyphicon-remove');
		});	
	}
});
		