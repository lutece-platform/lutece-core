/*
 * BULMA JS
 *
 */
/* Specific script for back office */
$( function(){
	var nCounter = "";

	// Count effect
	$('.box .content h3 span').each( function () {
		nCounter = $(this).text();
		var sVal = "";
		var thisTXT = $(this).text().split(" ");
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

	// Toggle collapse buttons
	$('[data-toggle="collapse"]').click(function() {
		if ($(this).find("i").hasClass("fa-minus")){
			$(this).find("i").addClass("fa-plus").removeClass("fa-minus");
			$(this).parents('.card').children('.card-content').toggleClass('is-hidden');
		}
		else if ($(this).find("i").hasClass("fa-plus")){
			$(this).parents('.card').children('.card-content').toggleClass('is-hidden');
			$(this).find("i").addClass("fa-minus").removeClass("fa-plus");
		}
	});

	/* Card collapsible */
	$('.card.collapsed-box .card-content').toggle();
	$('.card.collapsed-box').click(function() {
		$(this).children('.card-content').toggle();
	});

	// 
	$('.file.has-name > .file-label > .file-input').change(function() {
		console.log('Init');
		var names='', oFiles = $(this)[0].files, nFiles = oFiles.length;
  		for (var nFileId = 0; nFileId < nFiles; nFileId++) {
    		names += oFiles[nFileId].name;
			console.log( names );
  		}
		  $(this).parent().children('.file-name').html( names );
	});

	// Toggle Modal
	$('[data-toggle="modal"]').click(function(e) {
		e.preventDefault();
		var $modal=$(this).data('target');
		$($modal).toggleClass('is-active');
		var modalSrc = $(this).data('url'),
		modalTitle = $(this).data('modal-title');
		$($modal).find('.modal-card-title').html( modalTitle );
		if( $($modal).find('#modalIframe').length > 0 ){
			$($modal).find('#modalIframe').attr('src', modalSrc ).css('margin-top','-60px').css('height','400px');
		}
	});

	$('[data-dismiss="modal"]').click(function(e) {
		e.preventDefault();
		$(this).parents('.modal').toggleClass('is-active');
	});

	// Toggle dismiss alert
	$('[data-dismiss="alert"]').click(function() {
		$(this).parents('.notification').remove();
	});

	// Toggle dropdown alert
	$('[data-toggle="dropdown"]').click( function(e) {
		e.preventDefault();
		$(this).parents('.dropdown').toggleClass('is-active');
	});

	/* Tabs Panel Default */
	$('.tabs').each( function(index) {
		var $tabParent = $(this);
		var $tabs = $tabParent.find('li');
		var $contents = $tabParent.next('.tab-content').find('.tab-pane');
		$tabs.each( function(){
			$(this).click(function(e) {
				if(  $contents.length > 0 ){
					e.preventDefault();
					var curIndex = $(this).index();
					// toggle tabs
					$tabs.removeClass('is-active');
					$tabs.eq(curIndex).addClass('is-active');
					// toggle contents
					$contents.removeClass('show').removeClass('active');
					$contents.eq(curIndex).addClass('show');
				}
			});
		});
	});

	/* Tabs Panel Vertical */
	$('.menu-list').each( function(index) {
		// var $tabParent = $('.tabs-parent');
		var $tabs = $(this).find('li');
		var $contents = $(this).parents('.tabs-parent').find('.tab-content > .tab-pane');
		$tabs.click(function(e) {
			e.preventDefault();
		  	var curIndex = $(this).index();
		  	// toggle tabs
		  	$tabs.removeClass('is-active');
		  	$tabs.eq(curIndex).addClass('is-active');
		  	// toggle contents
		  	$contents.removeClass('active').removeClass('show');
		  	$contents.eq(curIndex).addClass('active').addClass('show');
		});
	});

	// Admin Features
	var admfeatlink = $('#admin_features .menu-list > li > a');
	if ( admfeatlink.length > 0 ){
		admfeatlink.each( function( ) {
			$(this).on('click', function( e ) {
			 	Cookies.set('technnicaltab',  e.target.hash , { sameSite: 'Strict' } );
			});
		})
	} 
	
	$('#technical_settings .accordion .accordion-header').on('click', function(e){
		Cookies.remove('technnicaltab');
	});

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
bar.attr( 'aria-valuenow', Math.round( complexity ));
bar.toggleClass('is-success', valid);
bar.toggleClass('is-danger', !valid);
bar.attr( 'value', Math.round( complexity ));
}

/* Tab management for advanced user parameters */
function manageAdminFeatureTab( hc ){
	if( hc != undefined ){
		$(hc).parents('.accordion').addClass('is-active');
		$('.menu-list li').removeClass('is-active');
		var k='a[href="' + hc + '"]';
		$(k).parent().addClass('is-active');
		$('.tab-pane').removeClass('is-active show');
		$(hc).addClass('show');
		$('html, body').animate({scrollTop: $(hc).offset().top}, 800);	
	}
}	
