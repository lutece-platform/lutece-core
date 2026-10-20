/**
 * Upload of images and PDF files from the rich text editors of the back office (TinyMCE, Autumn Note) into the spaces of the library.
 *
 * The plugin library answers jsp/admin/plugins/library/EditorUpload.jsp: getUploadConfig tells which kinds of file the user may upload (the
 * document plugin enabled, the media type and space of the kind set in the datastore, the right to create such a document there) and gives a
 * token; upload creates the document and returns its URL. Without the plugin, nothing is available and the editors keep their own behaviour.
 *
 * @author Lutece - Mairie de Paris
 */
window.LuteceEditorUpload = window.LuteceEditorUpload || ( function () {
	'use strict';

	const UPLOAD_URL = 'jsp/admin/plugins/library/EditorUpload.jsp';
	const CONFIG_URL = UPLOAD_URL + '?action=getUploadConfig';
	const NONE = Object.freeze( { image: false, pdf: false } );

	let configSync = null;
	let configPromise = null;

	function parse( text ) {
		try {
			const config = JSON.parse( text );
			return { image: config.image === true, pdf: config.pdf === true, token: config.token };
		} catch ( e ) {
			return NONE;
		}
	}

	/**
	 * The kinds of file the user may upload, read synchronously once per page, for the editor templates that build their toolbar synchronously
	 *
	 * @deprecated a synchronous request holds the page until the answer: use getConfig
	 * @returns {{image: boolean, pdf: boolean}}
	 */
	function getConfigSync( ) {
		if ( configSync === null ) {
			try {
				const request = new XMLHttpRequest( );
				request.open( 'GET', CONFIG_URL, false );
				request.send( null );
				configSync = request.status === 200 ? parse( request.responseText ) : NONE;
			} catch ( e ) {
				configSync = NONE;
			}
		}
		return configSync;
	}

	/**
	 * The kinds of file the user may upload, read once per page
	 *
	 * @returns {Promise<{image: boolean, pdf: boolean}>}
	 */
	function getConfig( ) {
		if ( configPromise === null ) {
			configPromise = fetch( CONFIG_URL, { credentials: 'same-origin', cache: 'no-store' } )
				.then( response => response.ok ? response.text( ).then( parse ) : NONE )
				.catch( ( ) => NONE );
		}
		return configPromise;
	}

	/**
	 * Uploads a file, with a token read just before: a token is valid for one upload only
	 *
	 * @param {File|Blob} file the file
	 * @param {string} kind image or pdf
	 * @returns {Promise<{location: string, title: string}>} rejected with the message of the server on error
	 */
	async function upload( file, kind ) {
		const response = await fetch( CONFIG_URL, { credentials: 'same-origin', cache: 'no-store' } );
		const config = response.ok ? parse( await response.text( ) ) : NONE;
		if ( !config[ kind ] || !config.token ) {
			throw new Error( 'Upload not available' );
		}
		// A multipart request reaches the page through the multipart servlet of the core, which keeps only the fields of the body: the action
		// goes there, not in the query string
		const data = new FormData( );
		data.append( 'action', 'upload' );
		data.append( 'kind', kind );
		data.append( '_csrftoken', config.token );
		data.append( 'file', file, file.name || ( kind === 'pdf' ? 'document.pdf' : 'image.png' ) );
		const result = await fetch( UPLOAD_URL, { method: 'POST', body: data, credentials: 'same-origin' } );
		let json = null;
		try {
			json = await result.json( );
		} catch ( e ) {
			throw new Error( 'Upload failed (' + result.status + ')' );
		}
		if ( json.error || !json.location ) {
			throw new Error( json.error || 'Upload failed' );
		}
		return json;
	}

	/**
	 * Lets the user choose a file
	 *
	 * @param {string} accept the accept attribute of the file input, e.g. application/pdf
	 * @returns {Promise<File>} never settled when the user cancels
	 */
	function pickFile( accept ) {
		return new Promise( resolve => {
			const input = document.createElement( 'input' );
			input.type = 'file';
			input.accept = accept;
			input.addEventListener( 'change', ( ) => {
				if ( input.files && input.files.length > 0 ) {
					resolve( input.files[ 0 ] );
				}
			} );
			input.click( );
		} );
	}

	/**
	 * The link inserted for an uploaded PDF file
	 *
	 * @param {{location: string, title: string}} result the result of the upload
	 * @param {string} text the selected text, the title of the file when empty
	 * @returns {string} the HTML of the link
	 */
	function pdfLink( result, text ) {
		const escape = value => String( value ).replace( /[&<>"']/g, c => ( { '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' } )[ c ] );
		return `<a href="${escape( result.location )}">${escape( text && text.trim( ) ? text : result.title )}</a>`;
	}

	/**
	 * The images_upload_handler of TinyMCE 6
	 */
	function tinymceImageHandler( blobInfo ) {
		const blob = blobInfo.blob( );
		const file = new File( [ blob ], blobInfo.filename( ), { type: blob.type } );
		return upload( file, 'image' )
			.then( result => result.location )
			.catch( e => Promise.reject( { message: e.message, remove: true } ) );
	}

	return { getConfig, getConfigSync, upload, pickFile, pdfLink, tinymceImageHandler };
} )( );
