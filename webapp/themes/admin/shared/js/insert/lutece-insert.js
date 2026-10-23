/**
 * The behaviours of the insert service screens (insert_service.css), set by data attributes, so that the templates hold no script of their own:
 *
 * - data-lutece-insert-views on a group of buttons [data-view]: grid or list. data-target: the selector of the element whose data-view changes;
 *   data-storage-key: the key remembering the choice in the browser; data-default-view: the view without a remembered choice.
 * - data-lutece-insert-select on a form: its [data-lutece-insert-item] buttons are selected by a click, a double click submits the form.
 *   data-fields on an item: the values of the hidden fields of the form it sets, as JSON ({"id_document":"30"}); data-label: its name.
 *   data-multiple="true": several items, each adding its fields to [data-lutece-insert-selected-fields]. [data-lutece-insert-submit] is
 *   disabled until an item is selected; [data-lutece-insert-selection] says what is selected, from data-text-none, data-text-selected
 *   (one item, followed by its label) and data-text-count (several, %n the number).
 * - data-lutece-insert-search on an input: filters the li of data-target on their data-search, shows data-nomatch when none matches, writes
 *   the count in data-count from data-text-one and data-text-many (%n the number).
 * - data-lutece-insert-tabs on a tablist: its [role="tab"] buttons show the panel of their aria-controls; arrow keys, Home and End move.
 * - data-lutece-insert-icon on an img: when the image cannot be loaded, it is hidden and the element that follows it is shown instead.
 *
 * @author Lutece - Mairie de Paris
 */
window.LuteceInsert = window.LuteceInsert || ( function () {
	'use strict';

	const storage = {
		get( key ) {
			try { return localStorage.getItem( key ); } catch ( e ) { return null; }
		},
		set( key, value ) {
			try { localStorage.setItem( key, value ); } catch ( e ) { /* not remembered */ }
		}
	};

	function initViews( group ) {
		const target = document.querySelector( group.dataset.target );
		if ( !target ) {
			return;
		}
		const buttons = [ ...group.querySelectorAll( '[data-view]' ) ];
		const views = buttons.map( button => button.dataset.view );
		const setView = ( view, remember ) => {
			target.dataset.view = view;
			buttons.forEach( button => button.setAttribute( 'aria-pressed', String( button.dataset.view === view ) ) );
			if ( remember && group.dataset.storageKey ) {
				storage.set( group.dataset.storageKey, view );
			}
		};
		const stored = group.dataset.storageKey ? storage.get( group.dataset.storageKey ) : null;
		setView( views.includes( stored ) ? stored : ( group.dataset.defaultView || target.dataset.view || views[ 0 ] ), false );
		buttons.forEach( button => button.addEventListener( 'click', ( ) => setView( button.dataset.view, true ) ) );
		group.hidden = false;
	}

	function initSelect( form ) {
		const items = [ ...form.querySelectorAll( '[data-lutece-insert-item]' ) ];
		const multiple = form.dataset.multiple === 'true';
		const submits = [ ...form.querySelectorAll( '[data-lutece-insert-submit]' ) ];
		const selection = form.querySelector( '[data-lutece-insert-selection]' );
		const selectedFields = form.querySelector( '[data-lutece-insert-selected-fields]' );
		const fieldsOf = item => {
			try { return JSON.parse( item.dataset.fields || '{}' ); } catch ( e ) { return {}; }
		};
		const isSelected = item => item.getAttribute( 'aria-pressed' ) === 'true';

		const update = ( ) => {
			const selected = items.filter( isSelected );
			if ( multiple && selectedFields ) {
				selectedFields.replaceChildren( ...selected.flatMap( item => Object.entries( fieldsOf( item ) ).map( ( [ name, value ] ) => {
					const input = document.createElement( 'input' );
					input.type = 'hidden';
					input.name = name;
					input.value = value;
					return input;
				} ) ) );
			} else {
				const fields = selected.length ? fieldsOf( selected[ 0 ] ) : fieldsOf( items[ 0 ] || form );
				Object.keys( fields ).forEach( name => {
					const input = form.querySelector( `input[type="hidden"][name="${CSS.escape( name )}"]` );
					if ( input ) {
						input.value = selected.length ? fields[ name ] : '';
					}
				} );
			}
			submits.forEach( submit => {
				submit.disabled = selected.length === 0;
			} );
			if ( selection ) {
				selection.textContent = '';
				if ( selected.length === 0 ) {
					selection.textContent = selection.dataset.textNone || '';
				} else if ( selected.length === 1 || !multiple ) {
					const name = document.createElement( 'b' );
					name.textContent = selected[ 0 ].dataset.label || '';
					selection.append( ( selection.dataset.textSelected || '' ) + ' ', name );
				} else {
					selection.textContent = ( selection.dataset.textCount || '%n' ).replace( '%n', selected.length );
				}
			}
		};

		items.forEach( item => {
			item.setAttribute( 'aria-pressed', String( isSelected( item ) ) );
			item.addEventListener( 'click', ( ) => {
				const on = !isSelected( item );
				if ( !multiple ) {
					items.forEach( other => other.setAttribute( 'aria-pressed', 'false' ) );
				}
				item.setAttribute( 'aria-pressed', String( on ) );
				update( );
			} );
			if ( !multiple ) {
				item.addEventListener( 'dblclick', ( ) => {
					items.forEach( other => other.setAttribute( 'aria-pressed', String( other === item ) ) );
					update( );
					if ( submits[ 0 ] ) {
						form.requestSubmit( submits[ 0 ] );
					}
				} );
			}
		} );
		update( );
	}

	function initSearch( input ) {
		const list = document.querySelector( input.dataset.target );
		if ( !list ) {
			return;
		}
		const count = input.dataset.count ? document.querySelector( input.dataset.count ) : null;
		const noMatch = input.dataset.nomatch ? document.querySelector( input.dataset.nomatch ) : null;
		const filter = ( ) => {
			const query = input.value.trim( ).toLowerCase( );
			let shown = 0;
			list.querySelectorAll( ':scope > li' ).forEach( li => {
				const match = !query || ( li.dataset.search || li.textContent ).toLowerCase( ).includes( query );
				li.hidden = !match;
				shown += match ? 1 : 0;
			} );
			if ( noMatch ) {
				noMatch.hidden = shown > 0;
			}
			if ( count ) {
				const text = shown === 1 ? count.dataset.textOne : count.dataset.textMany;
				count.textContent = ( text || '%n' ).replace( '%n', shown );
			}
		};
		input.addEventListener( 'input', filter );
		input.addEventListener( 'keydown', event => {
			if ( event.key === 'Enter' ) {
				event.preventDefault( );
			}
		} );
		filter( );
	}

	function initTabs( tablist ) {
		const tabs = [ ...tablist.querySelectorAll( '[role="tab"]' ) ];
		const select = tab => tabs.forEach( other => {
			const selected = other === tab;
			other.setAttribute( 'aria-selected', String( selected ) );
			other.tabIndex = selected ? 0 : -1;
			const panel = document.getElementById( other.getAttribute( 'aria-controls' ) );
			if ( panel ) {
				panel.hidden = !selected;
			}
		} );
		tabs.forEach( ( tab, index ) => {
			tab.addEventListener( 'click', ( ) => select( tab ) );
			tab.addEventListener( 'keydown', event => {
				const moves = { ArrowDown: 1, ArrowRight: 1, ArrowUp: -1, ArrowLeft: -1 };
				let next = null;
				if ( event.key in moves ) {
					next = tabs[ ( index + moves[ event.key ] + tabs.length ) % tabs.length ];
				} else if ( event.key === 'Home' ) {
					next = tabs[ 0 ];
				} else if ( event.key === 'End' ) {
					next = tabs[ tabs.length - 1 ];
				}
				if ( next ) {
					event.preventDefault( );
					next.focus( );
					select( next );
				}
			} );
		} );
		select( tabs.find( tab => tab.getAttribute( 'aria-selected' ) === 'true' ) || tabs[ 0 ] );
	}

	function initIcon( img ) {
		const fallback = ( ) => {
			img.hidden = true;
			if ( img.nextElementSibling ) {
				img.nextElementSibling.hidden = false;
			}
		};
		if ( img.complete && img.naturalWidth === 0 ) {
			fallback( );
		} else {
			img.addEventListener( 'error', fallback, { once: true } );
		}
	}

	function init( root ) {
		const scope = root || document;
		scope.querySelectorAll( '[data-lutece-insert-views]' ).forEach( initViews );
		scope.querySelectorAll( 'form[data-lutece-insert-select]' ).forEach( initSelect );
		scope.querySelectorAll( '[data-lutece-insert-search]' ).forEach( initSearch );
		scope.querySelectorAll( '[data-lutece-insert-tabs]' ).forEach( initTabs );
		scope.querySelectorAll( 'img[data-lutece-insert-icon]' ).forEach( initIcon );
	}

	if ( document.readyState === 'loading' ) {
		document.addEventListener( 'DOMContentLoaded', ( ) => init( ) );
	} else {
		init( );
	}

	return { init };
} )( );
