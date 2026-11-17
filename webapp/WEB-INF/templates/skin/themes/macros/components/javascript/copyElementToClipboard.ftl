<#--
Macro: copyElementToClipboard

Description: Generates a JavaScript snippet that attaches click-to-copy behavior to matching elements, with optional success and error feedback messages. Each element becomes a keyboard-reachable button (Enter or Space copies), the text is written with the asynchronous Clipboard API, and the result is announced to screen readers through a shared live region.

Parameters:
- selector (string, optional): CSS selector for the element(s) to make copyable. Default: '.copy-content'.
- class (string, optional): CSS class to add to the target element(s). Default: 'copy-icon'.
- showMsg (boolean, optional): Whether to display success/error messages after the copy operation. Default: true.
- msgDone (string, optional): Success message shown after copying. Default: '#i18n{portal.util.copy.done}'.
- msgError (string, optional): Error message shown if copy fails. Default: '#i18n{portal.util.copy.error}'.

Snippet:

    Basic usage with default selector:

    <@copyElementToClipboard />

    Custom selector with feedback disabled:

    <@copyElementToClipboard selector='.code-block' showMsg=false />

    Custom messages:

    <@copyElementToClipboard selector='.reference-number' msgDone='Copied!' msgError='Copy failed' />

-->
<#macro copyElementToClipboard selector='.copy-content' class='copy-icon' showMsg=true msgDone='#i18n{portal.util.copy.done}' msgError='#i18n{portal.util.copy.error}'>
<#-- Older callers pass the class already quoted ( class='"copy-icon"' ): the quotes are dropped -->
<#local cssClass = class?trim?remove_beginning('"')?remove_ending('"')?remove_beginning("'")?remove_ending("'") />
<script>
(() => {
	const msgDone = '${msgDone?js_string?no_esc}';
	const msgError = '${msgError?js_string?no_esc}';
	const timers = new WeakMap();
	let lastMessage = '';

	// Last resort for pages served over plain http, where navigator.clipboard does not exist
	const legacyCopy = text => {
		const field = Object.assign( document.createElement('textarea'), { value: text } );
		field.setAttribute('readonly', '');
		field.style.cssText = 'position:fixed;inset-block-start:0;opacity:0';
		document.body.append( field );
		field.select();
		try { return document.execCommand('copy'); } finally { field.remove(); }
	};

	// One live region per page: screen readers hear the result, focus stays on the element
	const getStatus = () => {
		let status = document.getElementById('copy-to-clipboard-status');
		if ( !status ) {
			status = document.createElement('span');
			status.id = 'copy-to-clipboard-status';
			status.className = 'visually-hidden';
			status.setAttribute('role', 'status');
			document.body.append( status );
		}
		return status;
	};

	const report = ( elem, ok ) => {
		let message = ok ? msgDone : msgError;
		// VoiceOver skips a message identical to the previous one: a no-break space makes it differ
		if ( message === lastMessage ) message += ' ';
		lastMessage = message;
		const status = getStatus();
		status.textContent = '';
		requestAnimationFrame( () => { status.textContent = message; } );
		<#if showMsg>
		// Repeated clicks restart the feedback instead of stacking timers
		const pending = timers.get( elem );
		if ( pending ) {
			clearTimeout( pending.timer );
		} else {
			timers.set( elem, { original: elem.textContent } );
		}
		const state = timers.get( elem );
		elem.classList.remove('msg-success', 'msg-error');
		elem.textContent = message;
		elem.classList.add( ok ? 'msg-success' : 'msg-error' );
		state.timer = setTimeout( () => {
			elem.textContent = state.original;
			elem.classList.remove('msg-success', 'msg-error');
			status.textContent = '';
			timers.delete( elem );
		}, 1200 );
		</#if>
	};

	const copy = async elem => {
		const pending = timers.get( elem );
		const text = ( pending ? pending.original : elem.innerText ).trim();
		if ( !text ) return;
		try {
			await navigator.clipboard.writeText( text );
			report( elem, true );
		} catch ( e ) {
			report( elem, legacyCopy( text ) );
		}
	};

	const init = () => {
		document.querySelectorAll('${( selector! )?js_string?no_esc}').forEach( elem => {
			<#if cssClass?has_content>elem.classList.add( ...'${cssClass?js_string?no_esc}'.split(' ') );</#if>
			elem.setAttribute('title', '${i18n('portal.util.labelCopy')?js_string?no_esc}');
			// The element acts as a button: reachable with Tab, activated with Enter or Space
			if ( !elem.matches('button, a[href], input, textarea, select') ) {
				elem.setAttribute('role', 'button');
				elem.setAttribute('tabindex', '0');
				elem.addEventListener('keydown', e => {
					if ( e.key === 'Enter' || e.key === ' ' ) {
						e.preventDefault();
						copy( elem );
					}
				});
			}
			elem.addEventListener('click', () => copy( elem ) );
		});
	};

	if ( document.readyState === 'loading' ) {
		document.addEventListener('DOMContentLoaded', init);
	} else {
		init();
	}
})();
</script>
</#macro>