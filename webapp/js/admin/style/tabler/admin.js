/*
 * BS 5.1 + Tabler
 *
 */
/* Specific script for back office */
function themeMode(mode){
	var switchMode=$('#switch-darkmode');
	if( mode != 'dark'){
		switchMode.children('span').text('sombre');
		if( $('body').hasClass('theme-dark') ) $('body').removeClass('theme-dark');
	} else{
		switchMode.children('span').text('clair');
		$('body').addClass('theme-dark');
		switchMode.children('.ti').removeClass('ti-moon').addClass('ti-sun');
	}
}

$( function(){
	var nCounter = "";
	var luteceTheme=localStorage.getItem('theme-bo-lutece');
	// Set Mode
	themeMode( luteceTheme );
	$('.box-widget .counter').each( function () {
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

	// Disable Double Click on submit Buttons -> * NOT WORKING WITH IE *
	// let numForms = document.forms.length;
	// if( numForms > 0 ){
	// 	let aForms = Array.from(document.forms);
	// 	aForms.forEach( function(form){
	// 		form.addEventListener( 'submit', function(e) {
	// 			e.submitter.setAttribute('disabled', 'disabled');
	// 		}, false);
	// 	});
	// }
		
	// Sortable Widgets
	const dashSortables = [].slice.call(document.querySelectorAll('.dashboard-widgets .widget-col'));

	// Loop through each nested sortable element
	for ( var i = 0; i < dashSortables.length; i++) {
		var sortableDash = new Sortable( dashSortables[i], {
			group: 'widget-dashboard',
			swapThreshold: 0.65,
			draggable: '.box-widget',
			store: {
				get: function (sortable) {
					var order = localStorage.getItem(sortable.options.group.name);
					return order ? order.split('|') : [];
				},
				set: function (sortable) {
					var order = sortable.toArray();
					localStorage.setItem(sortable.options.group.name, order.join('|'));
				}
			}
		});
	}

	$(".widget-col > .card > .card-header, .widget-col > .card .avatar, .widget-col > .card .info-box-icon").css("cursor", "move");

	$('#switch-darkmode').on('click', function(){
		var boTheme=localStorage.getItem('theme-bo-lutece');
		if( boTheme === 'dark'){
			localStorage.setItem('theme-bo-lutece','default');
			themeMode('default');
		} else{
			localStorage.setItem('theme-bo-lutece','dark');
			themeMode('dark');
		}
	})

	// Page Header button Management
	if( $('.skip-header').length > 0 ){
		var title=$('.skip-header .card-title').html();
		$('.page-title').html(title);
		$('.skip-header .card-title').remove();
		var headerContent = $('.skip-header').html();
		$('.skip-header').remove();
		$('#page-header-buttons').prepend(headerContent);
	}

	// File Input Style
	$(":file").not(".noBootstrapFilestyle")
		.addClass("filestyle")
		.filestyle({buttonText: "&nbsp;Parcourir"});

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
			// Set preview fulscreen
			$('#preview').toggleClass('open');
			$(this).toggleClass('open');
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

	$(".wrapper").show();

	// Translate data-toggle attr to data-bs-toggle
	$('[data-toggle="modal"]').each( function() { 
		$(this).attr( 'data-bs-toggle', 'modal' );
		$(this).attr( 'data-bs-target', $(this).attr('data-target') );
	});

	// Translate data-toggle attr to data-bs-dropdown
	$('[data-toggle="dropdown"]').each( function() { 
		$(this).attr( 'data-bs-toggle', 'dropdown' );
		$(this).attr( 'data-bs-target', $(this).attr('data-target') );
	});

	// Toggle collapse buttons
	$('[data-toggle="collapse"]').each( function() { 
		$(this).attr( 'data-bs-toggle', 'collapse' );
		$(this).attr( 'data-bs-target', $(this).attr('data-target') );
	});
	
	$('[data-toggle="collapse"]').click(function() {
		if ($(this).find("i").hasClass("fa-minus")){
			$(this).find("i").addClass("fa-plus").removeClass("fa-minus");
		}
		else if ($(this).find("i").hasClass("fa-plus")){
			$(this).find("i").addClass("fa-minus").removeClass("fa-plus");
		}
	});
	
	var popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'))
		var popoverList = popoverTriggerList.map(function (popoverTriggerEl) {
		return new bootstrap.Popover(popoverTriggerEl, {container: 'body', sanitize : false, placement: 'left'})
	})

});

/* Pretty print file size */
function prettySize( bytes, separator=' ', postFix=''){
if (bytes) {
	const sizes = ['Octets', 'Ko', 'Mo', 'Go', 'To'];
	const i = Math.min(parseInt(Math.floor(Math.log(bytes) / Math.log(1024)).toString(), 10), sizes.length - 1);
	return `${(bytes / (1024 ** i)).toFixed(i ? 1 : 0)}${separator}${sizes[i]}${postFix}`;
}
return 'n/a';
}

/* Manage progress bar  */
function progress( bar, complexity, valid ){
	bar.toggleClass('progress-bar-success', valid);
	bar.toggleClass('progress-bar-danger', !valid);
	bar.css({'width': complexity + '%'});
	bar.html( Math.round( complexity ) + '%');
}

/* Tab management for advanced user parameters */
function manageAdminFeatureTab( hc ){
	if( hc != undefined ){
		$(hc).parents('.collapse').addClass('show');
		$('.nav li').removeClass('active');
		var k='a[href="' + hc + '"]';
		$(k).parent().addClass('active');
		$('.tab-pane').removeClass('active').removeClass('in');
		$(hc).addClass('active show');
		$('html, body').animate({scrollTop: $(hc).offset().top}, 800);	
	}
	
	$('[data-bs-toggle="tab"]').on('shown.bs.tab', function(e) {
		Cookies.set('technnicaltab', e.target.hash, { sameSite: 'Strict' } )
	});
	
	$('[data-bs-toggle="collapse"]').on('click', function(e){
		Cookies.remove('technnicaltab');
	});
}
