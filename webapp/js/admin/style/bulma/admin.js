/*
 * BULMA
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
