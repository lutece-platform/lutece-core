/**
 * Lutece XSS Bypass - Base64 encoding for form fields
 *
 * Fields with data-xss-bypass="true" will have their value
 * Base64-encoded before form submission so that the global
 * XSS sanitizer filter does not alter their content.
 *
 * Server-side, use StringUtil.decodeXssBypass() to decode.
 *
 * Rich text editors must register a save hook via
 * window.luteceEditorSaveHooks.push( function(){ ... } )
 * so that their content is synced to the textarea before encoding.
 */
window.luteceEditorSaveHooks = window.luteceEditorSaveHooks || [];
( function( )
{
    document.addEventListener( 'DOMContentLoaded', function( )
	{
        document.querySelectorAll( 'form' ).forEach( function( form )
		{
            form.addEventListener('submit', function( )
			{
                // Sync rich text editor content to the underlying textarea before encoding
                window.luteceEditorSaveHooks.forEach( function( hook )
				{
                    hook( );
                });
                form.querySelectorAll( '[data-xss-bypass="true"]' ).forEach( function(field )
				{
                    var value = field.value;
                    if ( value )
						{
                        var encoded = btoa( unescape( encodeURIComponent( value ) ) );
                        var hidden = document.createElement( 'input' );
                        hidden.type = 'hidden';
                        hidden.name = field.name;
                        hidden.value = encoded;
                        form.appendChild( hidden );
                        field.removeAttribute( 'name' );
                    }
                });
            });
        });
    });
})();
