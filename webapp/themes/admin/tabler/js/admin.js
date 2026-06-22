/* ****************************************************************
 *
 * BS 5.3 + Tabler 1.4
 *
 * ****************************************************************/
const themeRoot = document.querySelector('html'); 

/* Specific script for back office */

/* Wire up the theme mode (dark/light) switchers. Restoring the stored value is handled by themeMenu() */
function themeMode( ){
	const themeSwitchers = document.querySelectorAll('.hide-theme-dark,.hide-theme-light');
	themeSwitchers.forEach( (iconSwitch) => {
		iconSwitch.addEventListener('click', (e) => {
			e.preventDefault();
			const mode = e.currentTarget.classList.contains('hide-theme-dark') ? 'dark' : 'light';
			themeRoot.dataset.bsTheme = mode;
			localStorage.setItem( 'lutece-tabler-theme', mode )
		});
		iconSwitch.addEventListener( 'keydown', ( keyboardEvent ) => {
			if( keyboardEvent.key === 'Enter' ){
				keyboardEvent.preventDefault();
				iconSwitch.click();
			}
		})
	});
}

/* Extract user initials from full name */
function getUserInitials(fullName) {
    if (!fullName || typeof fullName !== 'string') {
        return '';
    }
    
    // Clean the name and split by spaces
    const nameParts = fullName.trim().split(/\s+/);
    
    if (nameParts.length === 0) {
        return '';
    } else if (nameParts.length === 1) {
        // Single name: take first two characters
        return nameParts[0].substring(0, 2).toUpperCase();
    } else if (nameParts.length === 2) {
        // First and last name: take first character of each
        return (nameParts[0].charAt(0) + nameParts[1].charAt(0)).toUpperCase();
    } else {
        // Multiple names: take first character of first and last name
        return (nameParts[0].charAt(0) + nameParts[nameParts.length - 1].charAt(0)).toUpperCase();
    }
}

function themeMenu( ){
	// Single source of truth for restoring user stored preferences on load.
	// Theme mode : default to light — the dark/light icons are CSS-driven from data-bs-theme on <html>
	let storedTheme = localStorage.getItem( 'lutece-tabler-theme' );
    if( storedTheme === null ){
		storedTheme = 'light';
		localStorage.setItem( 'lutece-tabler-theme', storedTheme )
	}
	themeRoot.dataset.bsTheme = storedTheme;
	// Read direction : the user toggle persists in localStorage and applies to <html> (also done pre-paint by the head
	// script); the server-rendered global readmode applies to <body>. Re-apply for safety, then sync the toggle icon.
	if( localStorage.getItem( 'lutece-bo-readmode' ) === 'rtl' ){
		themeRoot.setAttribute( 'dir', 'rtl' );
	}
	if( themeRoot.getAttribute('dir') === 'rtl' || document.body.getAttribute('dir') === 'rtl' ){
		const readModeBtn = document.querySelector( '#lutece-rtl .ti' );
        const userDropdownMenu = document.querySelector( '.dropdown-menu-arrow' );
		if( readModeBtn != null ){
            if( userDropdownMenu != null ){
                userDropdownMenu.classList.add( 'dropdown-menu-start' );
                userDropdownMenu.classList.remove( 'dropdown-menu-end' );
            }
			readModeBtn.classList.remove( 'ti-text-direction-rtl' );
			readModeBtn.classList.add( 'ti-text-direction-ltr' );
		}
	}

	const mainMenu = document.getElementById('main-menu');
	if( mainMenu != null ){
		const mainNav = document.getElementById('main-nav');
		const userMenu = mainNav.querySelector('.user-initials');
		const userName = userMenu != null ? userMenu.dataset.username : '';

		// Set the main menu as active
		const mainMenus = mainMenu.querySelectorAll('.dropdown-item, .nav-item .nav-link');
		mainMenus.forEach((link) => {
			const activeMenu = sessionStorage.getItem( 'lutece-admin-active-menu' );
			if( activeMenu !== null && activeMenu !== '' ){
				const parentMenu = link.closest('.nav-item');
				if (link.id === activeMenu) {
					parentMenu.classList.add('active');
					link.classList.add('active');
				}
			}
			link.addEventListener('click', (e) => {
				sessionStorage.setItem( 'lutece-admin-active-menu', link.id )
			});
		});

		// Extract user initials from userName
		if (userName && userMenu) {
			const initials = getUserInitials(userName);
			userMenu.textContent = initials;
		}

	}
}

function setSkipLinks( ){
	const skipLinks = document.querySelectorAll('nav > .skip-links > a');
	if( skipLinks != null ){ 
		const mainUrl = window.location.href.split('#')[0];
		skipLinks.forEach( ( link ) => {
			link.href = mainUrl + link.getAttribute( 'href' ).substring( link.getAttribute( 'href' ).indexOf( '#' ) );
		})
	}
}

/* Wire up the read direction (rtl/ltr) toggle. Restoring the stored value is handled by themeMenu() */
function readMode( ){
	const switchReadMode =  document.querySelector( 'header.navbar #lutece-rtl');
    const userDropdownMenu = document.querySelector( '.dropdown-menu-arrow' );
	// Single source of truth for restoring user stored preferences on load.
	if( switchReadMode != null ){
		switchReadMode.addEventListener( "click", function(e){
			const readModeBtn = switchReadMode.querySelector('.ti');
			if( themeRoot.getAttribute('dir') === 'rtl' ){
                if( userDropdownMenu != null ){
                    userDropdownMenu.classList.remove( 'dropdown-menu-start' );
                    userDropdownMenu.classList.add( 'dropdown-menu-end' );
                }
				themeRoot.removeAttribute('dir')
				localStorage.removeItem( 'lutece-bo-readmode' );
			} else {
				themeRoot.setAttribute('dir','rtl')
                if( userDropdownMenu != null ){
                    userDropdownMenu.classList.remove( 'dropdown-menu-end' );
                    userDropdownMenu.classList.add( 'dropdown-menu-start' );                   
                }
				localStorage.setItem( 'lutece-bo-readmode', 'rtl' );
			}
			readModeBtn.classList.toggle('ti-text-direction-rtl')
			readModeBtn.classList.toggle('ti-text-direction-ltr')
		})
	}
}

/* Carry the non-sensitive UI preferences (theme mode, read direction) through logout.
   The logout Clear-Site-Data header wipes localStorage, so we pass the stored values as query
   params; AdminHeaderSessionLess.jsp re-seeds them on the landing page after the wipe. */
function wireLogout( ){
	const logoutLink = document.getElementById( 'lutece-admin-logout' );
	if( logoutLink != null ){
		logoutLink.addEventListener( 'click', ( e ) => {
			e.preventDefault();
			const url = new URL( logoutLink.href, window.location.href );
			const theme = localStorage.getItem( 'lutece-tabler-theme' );
			if( theme !== null ){
				url.searchParams.set( 'lutece-tabler-theme', theme );
			}
			if( localStorage.getItem( 'lutece-bo-readmode' ) === 'rtl' ){
				url.searchParams.set( 'lutece-bo-readmode', 'rtl' );
			}
			window.location.assign( url.toString() );
		});
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
    themeMenu();
    themeMode();
    readMode();
	wireLogout( );
	setSkipLinks( )

	var popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'))
		var popoverList = popoverTriggerList.map(function (popoverTriggerEl) {
		return new bootstrap.Popover(popoverTriggerEl, {container: 'body', sanitize : false, placement: 'left'})
	})

	const tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]')
	const tooltipList = [...tooltipTriggerList].map(tooltipTriggerEl => new bootstrap.Tooltip(tooltipTriggerEl))

	const tgCheck = document.querySelectorAll('.toggleCheck')
	tgCheck.forEach( (tg) => {
    	tg.addEventListener( 'click', ( el ) => {
			const isChecked = el.getAttribute( 'data-check' ) === 'check' ? true : false;
			document.querySelectorAll('input[type="checkbox"]').forEach(checkbox => {
				checkbox.checked = isChecked;
			});
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

	themeRoot.classList.remove( 'loading' )
	themeRoot.classList.add( 'loaded' )
})
