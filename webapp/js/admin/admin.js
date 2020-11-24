/*
 * AdminLTE JS
 *
 */

/* Specific script for back office */
$( function(){
	var nCounter = "";
	var nMax = "";

	// Count effect
	$('.small-box .inner h3').each( function () {
		nCounter = $(this).text();
		var sVal = "";
		var thisTXT = $(this).text().split("/");
		if ( thisTXT.length > 1 ){
			nCounter = thisTXT[0];
			sVal = " / " + thisTXT[1];
		}
		if ( $.isNumeric( nCounter ) ) {
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

	// Count effect dyn-info
	$('.info-box .info-box-number').each( function () {
		nCounter = $(this).text();
		var sVal = "";
		var thisTXT = $(this).text().split("/");

		if ( thisTXT.length > 1 ){
			nCounter = thisTXT[0];
			nMax = thisTXT[1]
			sVal = " / " + thisTXT[1];
		}

		if ( $.isNumeric( nCounter ) && $.isNumeric( nMax ) ) {
			$(this).prop('Counter',0).animate({
				Counter: nCounter
			}, {
				duration: 1000,
				easing: 'swing',
				step: function (now) {
					$(this).text( Math.ceil(now) + sVal );
					var pg = "width:" + ( Math.ceil(now) / nMax * 100 ) + "%" ;
					$(this).next().children().attr("style",  pg );
				}
			});
		}
	});

	//Make the dashboard widgets sortable Using jquery UI
	$(".lutece-dashboard").sortable({
	    placeholder: "sort-highlight",
	    connectWith: ".lutece-dashboard",
	    handle: ".box-header, .info-box-icon",
	    forcePlaceholderSize: true,
	    zIndex: 999999
	  });
	  $(".lutece-dashboard .box-header, .lutece-dashboard .info-box-icon").css("cursor", "move");

		//Make the table tr sortable Using jquery UI
		/*
		$('table.table-sortable tbody').sortable({
	    helper: fixWidthHelper
	}).disableSelection();
  $("table.table-sortable tr").css("cursor", "move");

	function fixWidthHelper(e, ui) {
	    ui.children().each(function() {
	        $(this).width($(this).width());
	    });
	    return ui;
	}*/



	// File Input Style
	$(":file").not(".noBootstrapFilestyle")
                .addClass("filestyle")
                .filestyle({buttonText: "&nbsp;Parcourir"});

	/** 	MENUS		 **/
	/** MENU TO ACTIVATE **/
	if( $("header").attr("data-menu")=="top" ){
            $("#menu-top").attr("id","menu-left");
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

	// Admin responsive preview
	function _fix() {
		var h = $(window).height();
        var w = $(window).width();
        $("#preview").css({
			width: (w - 30) + "px",
            height: (h - 50) + "px"
			});
        }
		_fix();
        $(window).resize(function() {
			_fix();
        });

        function iframe_width(width) {
			$("#preview").animate({width: width}, 500);
        }

        $("#display-full").click(function(e){
			e.preventDefault();
            iframe_width("100%");
        });

        $("#display-940").click(function(e){
			e.preventDefault();
            iframe_width("940px");
        });

        $("#display-480").click(function(e){
			e.preventDefault();
            iframe_width("480px");
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
			$(this).children().toggleClass('fa-arrows-alt').toggleClass('fa-remove');

		});
	}

	// Filter function
	var nb=0;
    $("#search_elements").on("keyup", function () {
        var search = $("#search_elements").val();
		if ( $("select#items_per_page").length > 0 ){
			var s1 = parseInt($("select#items_per_page:first option:selected").val());
			var s2 = parseInt($("select#items_per_page:first").attr("data-max-item"));

			if( s1 < s2 && nb ==0 ) {
				nb++;
				$(this).parent().after('<span class="alert alert-warning">Attention : Vous n\'avez pas sélectionner l\'ensemble des lignes  !! <button id="showall" class="btn btn-primary btn-xs">Tous afficher</button></span>')
				$("button#showall").click( function(){
					$("select#items_per_page:first").prop("")
				});
			 }
		}
        $(".element-box").each(function (index) {
            var elementName = $(this).attr("data-element").toLowerCase();
            if (elementName.match(search) == null) {
                $(this).parent().slideUp(200).fadeOut(500);
            } else {
                $(this).parent().slideDown(200).fadeIn(500);
            }
        });
    });

});

// Toggle collapse buttons
$('[data-toggle="collapse"]').click(function() {
	if ($(this).find("i").hasClass("fa-minus")){
    $(this).find("i").toggleClass("fa-plus");
    }
	else if ($(this).find("i").hasClass("fa-plus")){
    $(this).find("i").toggleClass("fa-minus");
    }
});
