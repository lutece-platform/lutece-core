/**
 * Admin Tabs State Manager
 *
 * Manages tab state persistence via sessionStorage and URL hash navigation.
 * Activated on tab containers with the attribute data-keep-state="true".
 *
 * URL hash pattern: #panel-{index} where index is 0-based.
 * sessionStorage key: lutece-admin-tab-{containerId}
 */
( function() {
	'use strict';

	const STORAGE_PREFIX = 'lutece-admin-tab-';
	const HASH_PATTERN = /^#panel-(\d+)$/;

	function getStorageKey( containerId ) {
		return STORAGE_PREFIX + window.location.pathname + '-' + containerId;
	}

	function activateTab( tabEl ) {
		if ( tabEl && typeof bootstrap !== 'undefined' ) {
			const bsTab = new bootstrap.Tab( tabEl );
			bsTab.show();
		}
	}

	function getTabLinks( container ) {
		return Array.from( container.querySelectorAll( '[data-bs-toggle="tab"]' ) );
	}

	function initTabContainer( container, index ) {
		const containerId = container.id || ( 'tabs-' + index );
		if ( !containerId ) return;

		const tabLinks = getTabLinks( container );
		if ( tabLinks.length === 0 ) return;

		// 1. URL hash has highest priority
		const hash = window.location.hash;
		const hashMatch = HASH_PATTERN.exec( hash );
		let restored = false;

		if ( hashMatch ) {
			const index = parseInt( hashMatch[1], 10 );
			if ( index >= 0 && index < tabLinks.length ) {
				activateTab( tabLinks[index] );
				restored = true;
			}
		}

		// 2. sessionStorage fallback
		if ( !restored ) {
			const storedTabId = sessionStorage.getItem( getStorageKey( containerId ) );
			if ( storedTabId ) {
				const storedTab = tabLinks.find( function( link ) {
					return link.id === storedTabId || link.getAttribute( 'aria-controls' ) === storedTabId;
				});
				if ( storedTab ) {
					activateTab( storedTab );
					restored = true;
				}
			}
		}

		// Listen for tab changes and persist to sessionStorage
		container.addEventListener( 'shown.bs.tab', function( event ) {
			const activeTab = event.target;
			const panelId = activeTab.getAttribute( 'aria-controls' ) || activeTab.id;
			if ( panelId ) {
				sessionStorage.setItem( getStorageKey( containerId ), panelId );
			}

			// Update URL hash with panel index, preserving the full URL
			const index = tabLinks.indexOf( activeTab );
			if ( index >= 0 ) {
				const newHash = '#panel-' + index;
				if ( window.location.hash !== newHash ) {
					var url = window.location.pathname + window.location.search + newHash;
					history.replaceState( null, '', url );
				}
			}
		});
	}

	// Handle hash changes (e.g., back/forward navigation or manual URL edit)
	function onHashChange() {
		const hash = window.location.hash;
		const hashMatch = HASH_PATTERN.exec( hash );
		if ( !hashMatch ) return;

		const index = parseInt( hashMatch[1], 10 );
		document.querySelectorAll( '[data-keep-state="true"]' ).forEach( function( container ) {
			const tabLinks = getTabLinks( container );
			if ( index >= 0 && index < tabLinks.length ) {
				activateTab( tabLinks[index] );
			}
		});
	}

	function init() {
		var containers = document.querySelectorAll( '[data-keep-state="true"]' );
		containers.forEach( function( container, index ) {
			initTabContainer( container, index );
		});
		window.addEventListener( 'hashchange', onHashChange );
	}

	document.addEventListener( 'DOMContentLoaded', init );

})();
