/* ****************************************************************
 *
 * BS 5.1 + Tabler
 *
 * ****************************************************************/
/* Specific script for back office */
function switchThemeMode( mode ){
	const switchMode = document.querySelector('#switch-darkmode'), 
		iconSwitch=switchMode.querySelector('.ti'), 
		themeBody = document.querySelector('body'); 
	
	if( mode === 'dark'){
		localStorage.setItem( 'lutece-tabler-theme', '' );
		if ( themeBody.dataset.bsTheme === 'dark' ){ 
			themeBody.dataset.bsTheme = '' ;
			iconSwitch.classList.remove('ti-sun');
			iconSwitch.classList.add('ti-moon');
		}
	} else{
		localStorage.setItem( 'lutece-tabler-theme', 'dark' );
		iconSwitch.classList.remove('ti-moon');
		iconSwitch.classList.add('ti-sun');
		themeBody.dataset.bsTheme = 'dark';
	}
}

function themeMode( ){
	let currentTheme = '', iconSwitch='';
	const switchMode = document.querySelector('#switch-darkmode'), 
		luteceTablerTheme=localStorage.getItem('lutece-tabler-theme'),
		themeBody = document.querySelector('body'),
		themeBodyTheme = themeBody.dataset.bsTheme === 'dark' ? 'dark' : '';
		  
	if( luteceTablerTheme === null ){
		localStorage.setItem( 'lutece-tabler-theme', themeBodyTheme )
	} else {
		if( switchMode != undefined  ){ 
			iconSwitch=switchMode.querySelector('.ti'); 
			if( themeBodyTheme != luteceTablerTheme ){
				localStorage.setItem( 'lutece-tabler-theme', luteceTablerTheme )
				switchThemeMode( themeBodyTheme )
			} else if ( luteceTablerTheme === 'dark' ){
				iconSwitch.classList.remove('ti-moon');
				iconSwitch.classList.add('ti-sun');
			}
		}
	}
	
	if( switchMode != undefined  ){
		switchMode.addEventListener( 'click', function(){
			currentTheme = localStorage.getItem( 'lutece-tabler-theme' )
			switchThemeMode( currentTheme )
		})

		switchMode.addEventListener( 'keydown', ( keyboardEvent ) => {
			switch (keyboardEvent.key) {
				case 'Enter':
					keyboardEvent.preventDefault();
					currentTheme = localStorage.getItem( 'lutece-tabler-theme' )
					switchThemeMode( currentTheme )
					break;
			}
		})
	}
	
}

function themeMenu( ){
	const switchMode = document.querySelector('#switch-menu');
	const menuHeader = document.querySelector( '#lutece-layout-wrapper' );
	const defaultMenu = menuHeader.classList[0];
	let localMenu =  localStorage.getItem( 'lutece-tabler-theme-menu' );
	
	if( localMenu === null ){ 
		localMenu = defaultMenu 
	} else if ( localMenu != defaultMenu ) {
		menuHeader.classList.remove( defaultMenu )
		menuHeader.classList.add( localMenu )
	}
	if( switchMode != null ){
		switchMode.addEventListener( 'click', (e) => {	
			if ( menuHeader.classList[0]  === 'header' ) {
				menuHeader.classList.add( 'aside' )
				menuHeader.classList.remove( 'header' )
				localStorage.setItem('lutece-tabler-theme-menu', 'aside');
			} else {
				localStorage.setItem('lutece-tabler-theme-menu', 'header');
				menuHeader.classList.add( 'header' )
				menuHeader.classList.remove( 'aside') 
				menuHeader.classList.remove( 'collapsed') 
			}
		});
	}

	document.querySelector('body').classList.remove( 'loading' )
	document.querySelector('body').classList.add( 'loaded' )
}

function setSkipLinks( ){
	const skipLinks = document.querySelectorAll('nav > .lutece-skip-links > li > a');
	if( skipLinks != null ){
		const mainUrl = window.location.href.split('#')[0];
		skipLinks.forEach( ( link ) => {
			link.href = mainUrl + link.getAttribute( 'href' ).substring( link.getAttribute( 'href' ).indexOf( '#' ) );
		})
	}
}

function readMode( ){
	let defaultReadMode = document.querySelector('body').getAttribute('dir')
	const switchReadMode =  document.querySelector( '#lutece-layout-wrapper .lutece-header #lutece-rtl');
	const localReadMode =  localStorage.getItem( 'lutece-bo-readmode' );
	if( localReadMode != null ){ 
		document.querySelector('body').setAttribute('dir', localReadMode )
	} else if( defaultReadMode != null ) {
		document.querySelector('body').setAttribute('dir',defaultReadMode)
	} else {
		document.querySelector('body').removeAttribute('dir')
	}
	if( switchReadMode != null ){
		switchReadMode.addEventListener( "click", function(e){
			const readModeBtn = switchReadMode.querySelector('.ti'); 
			defaultReadMode = document.querySelector('body').getAttribute('dir')
			if( defaultReadMode === null ){
				document.querySelector('body').setAttribute('dir','rtl')
				localStorage.setItem( 'lutece-bo-readmode', 'rtl' );
				readModeBtn.classList.toggle('ti-text-direction-rtl')
				readModeBtn.classList.toggle('ti-text-direction-ltr')
			} else {
				document.querySelector('body').removeAttribute('dir')
				localStorage.removeItem( 'lutece-bo-readmode' );
				readModeBtn.classList.toggle('ti-text-direction-ltr')
				readModeBtn.classList.toggle('ti-text-direction-rtl')
			} 

		})
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
	if ( document.querySelector( '#lutece-layout-wrapper' ) != null ){
		themeMenu();
		themeMode();
		readMode();

		setSkipLinks( )
	}
	
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
