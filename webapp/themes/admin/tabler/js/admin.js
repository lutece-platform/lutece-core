/* ****************************************************************
 *
 * BS 5.1 + Tabler
 *
 * ****************************************************************/
const themeRoot = document.querySelector('html'); 

/* Specific script for back office */
function switchThemeMode( mode ){
	const themeSwitchers = document.querySelectorAll('.hide-theme-dark,.hide-theme-light'); 
	themeRoot.dataset.bsTheme = mode;
	
	themeSwitchers.forEach( (iconSwitch) => {
        iconSwitch.addEventListener('click', (e) => {
            e.preventDefault();
            if( e.currentTarget.classList.contains('hide-theme-dark') ){
                themeRoot.dataset.bsTheme = 'dark';
            } else {
                themeRoot.dataset.bsTheme = 'light'; 
            }
            localStorage.setItem( 'lutece-tabler-theme', themeRoot.dataset.bsTheme )
        });
		iconSwitch.addEventListener( 'keydown', ( keyboardEvent ) => {
			switch (keyboardEvent.key) {
				case 'Enter':
					keyboardEvent.preventDefault();
					currentTheme = localStorage.getItem( 'lutece-tabler-theme' )
					switchThemeMode( currentTheme )
					break;
			}
		})
    });
	
}

function themeMode( ){
    let luteceTablerTheme=localStorage.getItem('lutece-tabler-theme')
		  
	if( luteceTablerTheme === null ){
        luteceTablerTheme = 'light';
		localStorage.setItem( 'lutece-tabler-theme', luteceTablerTheme )
	} 
    switchThemeMode( luteceTablerTheme )
	
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
    const mainMenu = document.getElementById('main-menu');
    const mainNav = document.getElementById('main-nav');
    const userMenu = mainNav.querySelector('.user-initials');
    const userName = userMenu.dataset.username;

	// Set the main menu as active
	const mainMenus = mainMenu.querySelectorAll('.dropdown-item');
	mainMenus.forEach((link) => {
		const activeMenu = sessionStorage.getItem( 'lutece-admin-active-menu' );
		if( activeMenu !== null && activeMenu !== '' ){
			const parentMenu = link.closest('.nav-item.dropdown');
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

// 	const defaultMenu = menuHeader.classList[0];
// 	let localMenu =  localStorage.getItem( 'lutece-tabler-theme-menu' );
// 	let localMenuState =  localStorage.getItem( 'lutece-tabler-theme-menu-state' );
	
// 	if( localMenu === null ){ 
// 		localMenu = defaultMenu 
// 	} else if ( localMenu != defaultMenu ) {
// 		menuHeader.classList.remove( defaultMenu )
// 		menuHeader.classList.add( localMenu )
// 		if( localMenu === 'aside'){
// 			if( localMenuState != null && localMenuState != '' ){
// 				menuHeader.classList.add( localMenuState )
// 			} else {
// 				localStorage.setItem('lutece-tabler-theme-menu-state', '');
// 			}
// 		}
// 	}
// 	if( switchMode != null ){
// 		switchMode.addEventListener( 'click', (e) => {	
// 			if ( menuHeader.classList[0]  === 'header' ) {
// 				menuHeader.classList.add( 'aside' )
// 				menuHeader.classList.remove( 'header' )
// 				localStorage.setItem('lutece-tabler-theme-menu', 'aside');
// 			} else {
// 				localStorage.setItem('lutece-tabler-theme-menu', 'header');
// 				menuHeader.classList.add( 'header' )
// 				menuHeader.classList.remove( 'aside') 
// 				menuHeader.classList.remove( 'collapsed') 
// 			}
// 		});
// 	}

// 	const btnAsideCollapse = document.querySelector('#aside-header-collapse');
// 	btnAsideCollapse.addEventListener( 'click', (e) => {	
// 		//menuHeader.classList.toggle('collapsed')
// 			if ( !menuHeader.classList.contains('collapsed') ) {
// 				menuHeader.classList.add( 'collapsed' )
// 				localStorage.setItem('lutece-tabler-theme-menu-state', 'collapsed');
// 			} else {
// 				localStorage.setItem('lutece-tabler-theme-menu-state', '');
// 				menuHeader.classList.remove( 'collapsed') 
// 			}
// 	});

	themeRoot.classList.remove( 'loading' )
	themeRoot.classList.add( 'loaded' )
}

function readMode( ){
	let defaultReadMode = themeRoot.getAttribute('dir')
	const localReadMode =  localStorage.getItem( 'lutece-bo-readmode' );
	if( localReadMode != null ){ 
		themeRoot.setAttribute('dir', localReadMode )
	} else if( defaultReadMode != null ) {
		themeRoot.setAttribute('dir',defaultReadMode)
	} else {
		themeRoot.removeAttribute('dir')
	}
	// if( switchReadMode != null ){
	// 	switchReadMode.addEventListener( "click", function(e){
	// 		const readModeBtn = switchReadMode.querySelector('.ti'); 
	// 		defaultReadMode = themeRoot.getAttribute('dir')
	// 		if( defaultReadMode === null ){
	// 			themeRoot.setAttribute('dir','rtl')
	// 			localStorage.setItem( 'lutece-bo-readmode', 'rtl' );
	// 			readModeBtn.classList.toggle('ti-text-direction-rtl')
	// 			readModeBtn.classList.toggle('ti-text-direction-ltr')
	// 		} else {
	// 			themeRoot.removeAttribute('dir')
	// 			localStorage.removeItem( 'lutece-bo-readmode' );
	// 			readModeBtn.classList.toggle('ti-text-direction-ltr')
	// 			readModeBtn.classList.toggle('ti-text-direction-rtl')
	// 		} 

	// 	})
	// }
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
