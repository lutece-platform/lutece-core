/*
 * Lutece actions (LUT-33153): the behaviours the templates used to write in event handler attributes (onclick,
 * onchange, onerror) or in javascript: URLs, declared with data attributes instead, so that no template writes
 * JavaScript in its HTML and a Content Security Policy can forbid inline handlers.
 *
 *   data-lutece-action="back"         on a link: goes back in the history when the previous page is a page of this
 *                                     site, else follows the href of the link (the fallback page)
 *   data-lutece-action="close"        on a link or a button: closes the window (a popup)
 *   data-lutece-action="hide-parent"  on a button: hides its parent element (a dismissible badge)
 *   data-lutece-action="submit"       on a field: submits its form when its value changes
 *   data-lutece-confirm="message"     on a link or a button: asks the message, and cancels the click when refused
 *   data-lutece-fallback-src="url"    on an image: shows this relative URL when the image cannot be loaded
 *
 * The same file is served to the back office (themes/admin/shared/js/util) and to the front office
 * (themes/skin/shared/js/util): keep both copies identical.
 */
( function( ) {
    'use strict';

    /**
     * Tells whether the previous page of the history is a page of this site.
     *
     * @return {boolean} true when going back stays on this site
     */
    function hasPreviousPageOnSite( ) {
        if ( window.history.length < 2 || !document.referrer ) {
            return false;
        }
        try {
            return new URL( document.referrer ).origin === window.location.origin;
        } catch ( e ) {
            return false;
        }
    }

    /**
     * Tells whether a URL is relative to this site: no scheme, no host.
     *
     * @param {string} url the URL
     * @return {boolean} true for a relative URL
     */
    function isRelativeUrl( url ) {
        return typeof url === 'string' && url.length > 0 && !/^[a-z][a-z0-9+.-]*:/i.test( url ) && url.indexOf( '//' ) !== 0
            && url.indexOf( '\\' ) < 0;
    }

    /**
     * Runs the action of a clicked element.
     *
     * @param {Event} event the click
     */
    function onClick( event ) {
        const confirmed = event.target.closest( '[data-lutece-confirm]' );
        if ( confirmed && !window.confirm( confirmed.getAttribute( 'data-lutece-confirm' ) ) ) {
            event.preventDefault( );
            event.stopImmediatePropagation( );
            return;
        }
        const element = event.target.closest( '[data-lutece-action]' );
        if ( !element ) {
            return;
        }
        switch ( element.getAttribute( 'data-lutece-action' ) ) {
            case 'back':
                if ( hasPreviousPageOnSite( ) ) {
                    event.preventDefault( );
                    window.history.back( );
                }
                break;
            case 'close':
                event.preventDefault( );
                window.close( );
                break;
            case 'hide-parent':
                event.preventDefault( );
                if ( element.parentElement ) {
                    element.parentElement.hidden = true;
                }
                break;
            default:
                break;
        }
    }

    /**
     * Submits the form of a changed field that asks for it.
     *
     * @param {Event} event the change
     */
    function onChange( event ) {
        const field = event.target.closest( '[data-lutece-action="submit"]' );
        if ( field && field.form ) {
            if ( typeof field.form.requestSubmit === 'function' ) {
                field.form.requestSubmit( );
            } else {
                field.form.submit( );
            }
        }
    }

    /**
     * Replaces an image by its fallback, once.
     *
     * @param {HTMLImageElement} image the image that cannot be loaded
     */
    function useFallback( image ) {
        const fallback = image.getAttribute( 'data-lutece-fallback-src' );
        image.removeAttribute( 'data-lutece-fallback-src' );
        if ( isRelativeUrl( fallback ) ) {
            image.src = fallback;
        }
    }

    /**
     * Replaces an image that cannot be loaded by its fallback.
     *
     * @param {Event} event the error, caught in the capture phase since it does not bubble
     */
    function onError( event ) {
        if ( event.target instanceof HTMLImageElement && event.target.hasAttribute( 'data-lutece-fallback-src' ) ) {
            useFallback( event.target );
        }
    }

    /**
     * Replaces the images that failed before this script was loaded (it is loaded at the end of the page).
     */
    function replaceFailedImages( ) {
        document.querySelectorAll( 'img[data-lutece-fallback-src]' ).forEach( function( image ) {
            if ( image.complete && image.naturalWidth === 0 ) {
                image.decode( ).catch( function( ) {
                    useFallback( image );
                } );
            }
        } );
    }

    document.addEventListener( 'click', onClick );
    document.addEventListener( 'change', onChange );
    document.addEventListener( 'error', onError, true );
    if ( document.readyState === 'loading' ) {
        document.addEventListener( 'DOMContentLoaded', replaceFailedImages );
    } else {
        replaceFailedImages( );
    }
}( ) );
