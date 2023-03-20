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

function setCounters( speed, counters  ){
	counters.forEach( counter => {
		const animate = () => {
			let nCounter = counter.innerText;
			let sVal = '';
			let thisTXT = counter.innerText.split( '/' );
			if ( thisTXT.length > 1 ){
				nCounter = thisTXT[0];
				sVal = ' / ' + thisTXT[1];
			}
			if ( typeof nCounter === 'number' ) {
				const time = nCounter / speed;
				if( data < value) {
					counter.innerText = Math.ceil( data + time );
					setTimeout(animate, 1);
				} else {
					counter.innerText = value;
				}
			}
		}
		animate();
	});
}

document.addEventListener( "DOMContentLoaded", function(){
	var popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'))
		var popoverList = popoverTriggerList.map(function (popoverTriggerEl) {
		return new bootstrap.Popover(popoverTriggerEl, {container: 'body', sanitize : false, placement: 'left'})
	})
	
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

	const boxCount = document.querySelectorAll('.box-widget .counter')
	setCounters( 200, boxCount );

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
