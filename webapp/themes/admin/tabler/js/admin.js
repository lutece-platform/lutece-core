/* ****************************************************************
 *
 * BS 5.1 + Tabler
 *
 * ****************************************************************/
/* Specific script for back office */
function themeMode( mode ){
	const switchMode = document.querySelector('#switch-darkmode'), childSwitch=switchMode.querySelector('span'), iconSwitch=switchMode.querySelector('.ti'), themeBody = document.querySelector('body');
	if( mode != 'dark'){
		childSwitch.textContent = 'sombre';
		if ( themeBody.classList.contains('theme-dark') ){ 
			themeBody.classList.remove('theme-dark') ;
			iconSwitch.classList.remove('ti-sun');
			iconSwitch.classList.add('ti-moon');
		}
	} else{
		iconSwitch.classList.remove('ti-moon');
		iconSwitch.classList.add('ti-sun');
		childSwitch.textContent = 'clair';
		themeBody.classList.add('theme-dark');
	}
}

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

document.addEventListener( "DOMContentLoaded", function(){
	var popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'))
		var popoverList = popoverTriggerList.map(function (popoverTriggerEl) {
		return new bootstrap.Popover(popoverTriggerEl, {container: 'body', sanitize : false, placement: 'left'})
	})

	const tgCheck = document.querySelectorAll('.toggleCheck')
	tgCheck.forEach( (tg) => {
    	tg.addEventListener( 'click', ( el ) => {
			const isChecked = el.getAttribute( 'data-check' ) === 'check' ? true : false;
			$("input:checkbox").prop('checked', isChecked );
		});
	});

	document.querySelectorAll('[data-toggle="modal"]').forEach( el => { 
		el.setAttribute( 'data-bs-toggle', 'modal' );
		el.setAttribute( 'data-bs-target', el.getAttribute('data-target') );
	});
	
	document.querySelectorAll('[data-toggle="dropdown"]').forEach( el =>  { 
		el.setAttribute( 'data-bs-toggle', 'dropdown' );
		el.setAttribute( 'data-bs-target', el.getAttribute('data-target') );
	});
	
	document.querySelectorAll('[data-toggle="collapse"]').forEach( el => { 
		el.setAttribute( 'data-bs-toggle', 'collapse' );
		el.setAttribute( 'data-bs-target', el.getAttribute('data-target') );
	});
})
