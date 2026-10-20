/**
 * Lutece integration of Autumn Note, a back office rich text editor.
 *
 * Creates the editors of a page with the options of Lutece, of the page (config of @initEditor) and of the editor profile, in that order,
 * and adds the Lutece buttons of lutece-editor-insert.js: in a toolbar group, lutece, luteceImage, lutecePdf, lutecePageLink,
 * luteceNewsletterBlog, luteceUploadPdf are taken as they are, luteceAll stands for lutece followed by the buttons the site offers, and a
 * button the site does not offer is left out.
 * When the plugin library offers it (lutece-editor-upload.js), images are uploaded into its image space instead of being embedded in base64,
 * and luteceUploadPdf uploads a PDF file into its PDF space, then inserts a link to it.
 *
 * @author Lutece - Mairie de Paris
 */
window.LuteceAutumnNote = window.LuteceAutumnNote || ( function () {
	'use strict';

	const INPUT = 'autumnnote';

	const DEFAULT_TOOLBAR = [
		[ 'paragraphStyle' ],
		[ 'undo', 'redo' ],
		[ 'bold', 'italic', 'underline', 'strikethrough' ],
		[ 'foreColor', 'backColor' ],
		[ 'alignLeft', 'alignCenter', 'alignRight', 'alignJustify' ],
		[ 'ul', 'ol', 'outdent', 'indent' ],
		[ 'hr', 'link', 'image', 'video', 'table' ],
		[ 'luteceAll' ],
		[ 'codeview', 'fullscreen' ]
	];

	const BLOCK_LABELS = {
		en: { paragraph: 'Paragraph', heading: 'Heading' },
		fr: { paragraph: 'Paragraphe', heading: 'Titre' }
	};

	/**
	 * Expands luteceAll in each group and leaves out the buttons the site does not offer. A group left empty is removed.
	 */
	function expandToolbar( toolbar, available ) {
		return toolbar
			.map( group => LuteceEditorInsert.expandButtons( group, available ) )
			.filter( group => group.length > 0 );
	}

	/**
	 * Inserts HTML where the selection was when the bookmark was taken
	 */
	function insertAt( editor, bookmark, html ) {
		editor.focus( );
		if ( bookmark ) {
			editor.restoreSelectionBookmark( bookmark );
		}
		editor.insertHTML( html );
	}

	/**
	 * Uploads a PDF file chosen by the user, then inserts a link to it in place of the selection, its text the selected text or the file title
	 */
	function uploadPdf( editor ) {
		const bookmark = editor.getSelectionBookmark( );
		const text = String( window.getSelection( ) || '' );
		LuteceEditorUpload.pickFile( 'application/pdf' )
			.then( file => LuteceEditorUpload.upload( file, 'pdf' ) )
			.then( result => insertAt( editor, bookmark, LuteceEditorUpload.pdfLink( result, text ) ) )
			.catch( e => window.alert( e.message ) );
	}

	/**
	 * The Lutece buttons, as button definitions of Autumn Note
	 */
	function luteceButtons( lang, library ) {
		const texts = LuteceEditorInsert.labels( lang );
		const buttons = {};
		[ 'lutece', 'luteceImage', 'lutecePdf', 'lutecePageLink', 'luteceNewsletterBlog' ].forEach( name => {
			buttons[ name ] = {
				icon: name,
				tooltip: texts[ name ],
				action: editor => {
					const bookmark = editor.getSelectionBookmark( );
					const url = LuteceEditorInsert.serviceUrl( name, INPUT, library, String( window.getSelection( ) || '' ) );
					LuteceEditorInsert.openInsertService( lang, name, url, html => insertAt( editor, bookmark, html ) );
				}
			};
		} );
		buttons.luteceUploadPdf = { icon: 'luteceUploadPdf', tooltip: texts.luteceUploadPdf, action: editor => uploadPdf( editor ) };
		return buttons;
	}

	/**
	 * Uploads the images the image dialog of the editor inserts. Paste and drop go through onImageUpload, but a file chosen in the dialog is
	 * inserted as a blob: URL without it: after each change, every blob: image is uploaded, then its src becomes the URL of the document.
	 */
	function uploadDialogImages( editor ) {
		const editable = editor.layoutInfo && editor.layoutInfo.editable;
		if ( !editable ) {
			return;
		}
		const pending = new WeakSet( );
		const extension = type => ( { 'image/jpeg': '.jpg', 'image/png': '.png', 'image/gif': '.gif', 'image/webp': '.webp' } )[ type ] || '';
		editor.on( 'change', ( ) => {
			editable.querySelectorAll( 'img[src^="blob:"]' ).forEach( img => {
				if ( pending.has( img ) ) {
					return;
				}
				pending.add( img );
				fetch( img.src )
					.then( response => response.blob( ) )
					.then( blob => LuteceEditorUpload.upload( new File( [ blob ], ( img.alt || 'image' ) + extension( blob.type ), { type: blob.type } ), 'image' ) )
					.then( result => {
						img.src = result.location;
						// the editor keeps the textarea in sync on the input events of its editable area
						editable.dispatchEvent( new Event( 'input', { bubbles: true } ) );
					} )
					.catch( e => window.alert( e.message ) );
			} );
		} );
	}

	/**
	 * The options of Lutece: admin look, no asset from a CDN, the block formats of the TinyMCE editor
	 */
	function defaults( lang ) {
		const texts = BLOCK_LABELS[ lang ] || BLOCK_LABELS.en;
		return {
			height: 400,
			lang: lang,
			theme: document.documentElement.dataset.bsTheme === 'dark' ? 'dark' : 'light',
			useBootstrap: true,
			toolbarButtonClass: 'btn btn-sm btn-light',
			useFontAwesome: false,
			fontAwesomeAutoInject: false,
			codeHighlight: false,
			paragraphStyles: [
				{ value: 'p', label: texts.paragraph },
				{ value: 'h2', label: texts.heading + ' 2' },
				{ value: 'h3', label: texts.heading + ' 3' },
				{ value: 'h4', label: texts.heading + ' 4' },
				{ value: 'h5', label: texts.heading + ' 5' }
			],
			toolbar: DEFAULT_TOOLBAR
		};
	}

	/**
	 * Creates the editors of the fields matching settings.selector
	 *
	 * @param {Object} settings selector, insert (the insert services the page allows), config (of @initEditor), profileConfig,
	 *                          lang, library (media types and spaces of the library insert service)
	 */
	async function init( settings ) {
		if ( typeof AutumnNote === 'undefined' ) {
			return;
		}
		const fields = document.querySelectorAll( settings.selector );
		if ( fields.length === 0 ) {
			return;
		}
		const { buttons: available, upload } = await LuteceEditorInsert.getAvailable( settings.insert );
		const options = Object.assign( defaults( settings.lang ), settings.config, settings.profileConfig );
		if ( upload.image && !options.onImageUpload ) {
			// The images go to the image space of the library: the editor inserts the URLs returned
			options.onImageUpload = files => Promise.all( Array.from( files ).map( file => LuteceEditorUpload.upload( file, 'image' ).then( r => r.location ) ) );
		}
		options.toolbar = expandToolbar( Array.isArray( options.toolbar ) ? options.toolbar : DEFAULT_TOOLBAR, available );
		const lutece = luteceButtons( settings.lang, settings.library || {} );
		const icons = {};
		Object.keys( lutece ).forEach( name => {
			icons[ name ] = LuteceEditorInsert.icon( name );
		} );
		options.buttons = Object.assign( lutece, options.buttons );
		options.icons = Object.assign( icons, options.icons );
		fields.forEach( field => {
			const editor = AutumnNote.create( field, options );
			if ( upload.image ) {
				uploadDialogImages( editor );
			}
		} );
	}

	return { init, expandToolbar };
} )( );
