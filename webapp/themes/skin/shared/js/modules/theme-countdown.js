/*
 * Countdown (cCountdown macro, lutece-core): updates every [data-countdown-to] block once per second.
 * The macro renders the remaining time on the server, so the block reads right without JavaScript; this script only
 * keeps it running. The units are aria-hidden: screen readers read the label and the end date, not every second.
 */
( () => {
	const SECOND = 1000;
	const UNITS = [
		[ 'days', 86400 ],
		[ 'hours', 3600 ],
		[ 'minutes', 60 ],
		[ 'seconds', 1 ]
	];

	const pad = ( n ) => String( n ).padStart( 2, '0' );

	const update = ( block, end ) => {
		let remaining = Math.max( 0, Math.floor( ( end - Date.now( ) ) / SECOND ) );
		if ( remaining === 0 ) {
			block.classList.add( 'is-expired' );
			return false;
		}
		UNITS.forEach( ( [ name, size ] ) => {
			const value = Math.floor( remaining / size );
			remaining -= value * size;
			const number = block.querySelector( `[data-countdown-unit="${name}"]` );
			if ( number ) {
				number.textContent = name === 'days' ? String( value ) : pad( value );
			}
		} );
		return true;
	};

	const start = ( block ) => {
		const end = Date.parse( block.dataset.countdownTo );
		if ( Number.isNaN( end ) ) {
			return;
		}
		if ( !update( block, end ) ) {
			return;
		}
		const timer = setInterval( ( ) => {
			if ( !update( block, end ) ) {
				clearInterval( timer );
			}
		}, SECOND );
	};

	const init = ( ) => document.querySelectorAll( '[data-countdown-to]' ).forEach( start );

	if ( document.readyState === 'loading' ) {
		document.addEventListener( 'DOMContentLoaded', init );
	} else {
		init( );
	}
} )( );
