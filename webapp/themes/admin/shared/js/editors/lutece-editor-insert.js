/**
 * The Lutece buttons of the rich text editors of the back office that are not TinyMCE (Autumn Note, Milkdown): the insert services and the
 * PDF upload, their labels and icons, which of them the site offers, and the dialog that opens an insert service.
 *
 * Buttons: lutece (the dialog of all the insert services), luteceImage, lutecePdf, lutecePageLink, luteceNewsletterBlog (one insert service
 * each) and luteceUploadPdf (lutece-editor-upload.js). In a list of buttons, luteceAll stands for lutece followed by the buttons the site
 * offers, and a button the site does not offer is left out.
 *
 * The insert service sends its result back through window.luteceEditorInsert, read by admin/insert/insert_into_element(2).html.
 *
 * @author Lutece - Mairie de Paris
 */
window.LuteceEditorInsert = window.LuteceEditorInsert || ( function () {
	'use strict';

	const SERVICES_URL = 'jsp/admin/insert/GetAvailableInsertServicesData.jsp?input=tinymce&selected_text=&mode=2';
	const SERVICE_BUTTONS = [ 'luteceImage', 'lutecePdf', 'lutecePageLink', 'luteceNewsletterBlog', 'luteceUploadPdf' ];

	const LABELS = {
		en: {
			lutece: 'Add Lutece contents',
			luteceImage: 'Add site image',
			lutecePdf: 'Add site pdf',
			lutecePageLink: 'Add link to site page',
			luteceNewsletterBlog: 'Add link to blog post',
			luteceUploadPdf: 'Upload a pdf',
			close: 'Close'
		},
		fr: {
			lutece: 'Ajouter des contenus Lutece',
			luteceImage: 'Ajouter une image du site',
			lutecePdf: 'Ajouter un PDF du site',
			lutecePageLink: 'Ajouter un lien vers une page du site',
			luteceNewsletterBlog: 'Ajouter un lien vers un billet de blog',
			luteceUploadPdf: 'Téléverser un PDF',
			close: 'Fermer'
		}
	};

	// The symbols of lutece.svg, inline: an editor that sanitizes its icons (Milkdown) drops the <use> of an external file
	const SYMBOLS_SVG = {
		logo: {"attrs": "viewBox=\"0 0 36 36\"", "inner": "<g stroke-width=\"2\" fill=\"none\"><path stroke=\"#9d5694\" vector-effect=\"non-scaling-stroke\" d=\"M21.79 10.75q1.65-.82 3.65-.42.75.15.77-.61l.05-3.3q.01-.67-.66-.67h-3.32q-.49 0-.5.49l-.07 4.01\"/><path stroke=\"#ffa295\" vector-effect=\"non-scaling-stroke\" d=\"M21.71 10.25l-4.29-.03q-.41 0-.41-.41l-.02-4.41q0-.65-.65-.65h-4.46q-.63 0-.63.63v25.74q0 .63.63.63h14.37q.75 0 .75-.75v-3.25q0-.75-.75-.75h-8.81q-.5 0-.5-.5l.02-10.89a.86.85 90 00-.85-.86h-2.98q-.63 0-.63-.63V11q0-.75.75-.75h3q.68 0 .7.68l.07 3.34q.02.48.5.48h3.72q.49 0 .5-.5l.05-3.5\"/><path stroke=\"#9db3ff\" vector-effect=\"non-scaling-stroke\" d=\"M21.71 10.25l.08.5\"/></g><path fill=\"#ff442a\" d=\"M17.06 0h2.25Q33.7 2.22 36 16.56v3C34.13 31 23.2 38.95 11.97 34.76-6.44 27.88-2.19 2.39 17.06 0zm4.73 10.75q1.65-.82 3.65-.42.75.15.77-.61l.05-3.3q.01-.67-.66-.67h-3.32q-.49 0-.5.49l-.07 4.01-4.29-.03q-.41 0-.41-.41l-.02-4.41q0-.65-.65-.65h-4.46q-.63 0-.63.63v25.74q0 .63.63.63h14.37q.75 0 .75-.75v-3.25q0-.75-.75-.75h-8.81q-.5 0-.5-.5l.02-10.89a.86.85 90 00-.85-.86h-2.98q-.63 0-.63-.63V11q0-.75.75-.75h3q.68 0 .7.68l.07 3.34q.02.48.5.48h3.72q.49 0 .5-.5l.05-3.5z\"/><path fill=\"#fff\" d=\"M21.71 10.25l.08.5-.05 3.5q-.01.5-.5.5h-3.72q-.48 0-.5-.48l-.07-3.34q-.02-.68-.7-.68h-3q-.75 0-.75.75v3.12q0 .63.63.63h2.98a.86.85 90 01.85.86l-.02 10.89q0 .5.5.5h8.81q.75 0 .75.75V31q0 .75-.75.75H11.88q-.63 0-.63-.63V5.38q0-.63.63-.63h4.46q.65 0 .65.65l.02 4.41q0 .41.41.41l4.29.03z\"/><path fill=\"#3b67fe\" d=\"M21.79 10.75l-.08-.5.07-4.01q.01-.49.5-.49h3.32q.67 0 .66.67l-.05 3.3q-.02.76-.77.61-2-.4-3.65.42z\"/>"},
		luteceimage: {"attrs": "viewBox=\"0 0 24 24\" stroke-width=\"1\" stroke=\"currentColor\" fill=\"none\" stroke-linecap=\"round\" stroke-linejoin=\"round\"", "inner": "<path stroke=\"none\" d=\"M0 0h24v24H0z\" fill=\"none\"></path><path d=\"M15 8h.01\"></path><path d=\"M12.5 21h-6.5a3 3 0 0 1 -3 -3v-12a3 3 0 0 1 3 -3h12a3 3 0 0 1 3 3v6.5\"></path><path d=\"M3 16l5 -5c.928 -.893 2.072 -.893 3 0l4 4\"></path><path d=\"M14 14l1 -1c.653 -.629 1.413 -.815 2.13 -.559\"></path><path d=\"M19 16v6\"></path><path d=\"M22 19l-3 3l-3 -3\"></path>"},
		lutecepdf: {"attrs": "viewBox=\"0 0 24 24\" stroke-width=\"1\" stroke=\"currentColor\" fill=\"none\" stroke-linecap=\"round\" stroke-linejoin=\"round\"", "inner": "<path stroke=\"none\" d=\"M0 0h24v24H0z\" fill=\"none\"></path><path d=\"M14 3v4a1 1 0 0 0 1 1h4\"></path><path d=\"M5 12v-7a2 2 0 0 1 2 -2h7l5 5v4\"></path><path d=\"M5 18h1.5a1.5 1.5 0 0 0 0 -3h-1.5v6\"></path><path d=\"M17 18h2\"></path><path d=\"M20 15h-3v6\"></path><path d=\"M11 15v6h1a2 2 0 0 0 2 -2v-2a2 2 0 0 0 -2 -2h-1z\"></path>"},
		lutecepagelink: {"attrs": "viewBox=\"0 0 24 24\" stroke-width=\"1\" stroke=\"currentColor\" fill=\"none\" stroke-linecap=\"round\" stroke-linejoin=\"round\"", "inner": "<path stroke=\"none\" d=\"M0 0h24v24H0z\" fill=\"none\"></path><path d=\"M4 21v-4a3 3 0 0 1 3 -3h5\"></path><path d=\"M9 17l3 -3l-3 -3\"></path><path d=\"M14 3v4a1 1 0 0 0 1 1h4\"></path><path d=\"M5 11v-6a2 2 0 0 1 2 -2h7l5 5v11a2 2 0 0 1 -2 2h-9.5\"></path>"}
	};

	const SYMBOLS = {
		lutece: 'logo',
		luteceImage: 'luteceimage',
		lutecePdf: 'lutecepdf',
		lutecePageLink: 'lutecepagelink',
		luteceNewsletterBlog: 'lutecepagelink',
		luteceUploadPdf: 'lutecepdf'
	};

	let dialog = null;

	/**
	 * @param {string} lang fr or en
	 * @returns {Object} the labels of the buttons and of the dialog
	 */
	function labels( lang ) {
		return LABELS[ lang ] || LABELS.en;
	}

	/**
	 * @param {string} name a button
	 * @param {string} [label] the label the icon carries, as tooltip and accessible name, for a button that has none of its own
	 * @returns {string} its icon, an SVG of a symbol of lutece.svg
	 */
	function icon( name, label ) {
		const symbol = SYMBOLS_SVG[ SYMBOLS[ name ] ] || SYMBOLS_SVG.logo;
		const viewBox = ( symbol.attrs.match( /viewBox="([^"]+)"/ ) || [ '', '0 0 24 24' ] )[ 1 ];
		const attrs = symbol.attrs.replace( /\s*viewBox="[^"]+"/, '' );
		const escaped = label ? String( label ).replace( /[&<>"]/g, c => ( { '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;' } )[ c ] ) : '';
		const a11y = label ? `role="img" aria-label="${escaped}"><title>${escaped}</title>` : 'aria-hidden="true">';
		return `<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="${viewBox}" ${attrs} ${a11y}${symbol.inner}</svg>`;
	}

	/**
	 * The insert services the site offers to the user: plugin names, and the sub categories of those that have some (image, pdf...)
	 */
	async function fetchServices( ) {
		try {
			const response = await fetch( SERVICES_URL, { credentials: 'same-origin' } );
			return response.ok ? ( await response.text( ) ).trim( ).split( ',' ).map( s => s.trim( ) ).filter( Boolean ) : [];
		} catch ( e ) {
			return [];
		}
	}

	/**
	 * The buttons of the insert services both offered by the site and allowed by the page (insert of @initEditor)
	 */
	function availableButtons( services, allowed ) {
		const buttons = [];
		const library = services.includes( 'library' ) && allowed.includes( 'library' );
		if ( library && services.includes( 'image' ) ) {
			buttons.push( 'luteceImage' );
		}
		if ( library && services.includes( 'pdf' ) ) {
			buttons.push( 'lutecePdf' );
		}
		if ( services.includes( 'pagelinkservice' ) && allowed.includes( 'pagelinkservice' ) ) {
			buttons.push( 'lutecePageLink' );
		}
		if ( services.includes( 'newsletter-blog' ) && allowed.includes( 'newsletter-blog' ) ) {
			buttons.push( 'luteceNewsletterBlog' );
		}
		return buttons;
	}

	/**
	 * What the site offers to the editors of the page: the buttons of the insert services and of the upload, and the kinds of file uploaded
	 *
	 * @param {string[]} allowed the insert services the page allows (insert of @initEditor)
	 * @returns {Promise<{buttons: string[], upload: {image: boolean, pdf: boolean}}>}
	 */
	async function getAvailable( allowed ) {
		const upload = ( typeof LuteceEditorUpload !== 'undefined' ) ? await LuteceEditorUpload.getConfig( ) : { image: false, pdf: false };
		const buttons = availableButtons( await fetchServices( ), allowed || [] ).concat( upload.pdf ? [ 'luteceUploadPdf' ] : [] );
		return { buttons, upload };
	}

	/**
	 * Expands luteceAll in a list of buttons and leaves out the buttons the site does not offer. Items that are not strings are kept.
	 */
	function expandButtons( items, available ) {
		return items
			.flatMap( item => item === 'luteceAll' ? [ 'lutece' ].concat( available ) : [ item ] )
			.filter( item => typeof item !== 'string' || !SERVICE_BUTTONS.includes( item ) || available.includes( item ) );
	}

	/**
	 * The URL of the insert service of a button, the same as the lutece plugin of TinyMCE 6
	 *
	 * @param {string} name the button
	 * @param {string} input the name of the editor, passed to the insert service
	 * @param {Object} library the media types and spaces of the library insert service
	 * @param {string} selectedText the selected text
	 * @returns {string|null} the URL, null for a button without insert service
	 */
	function serviceUrl( name, input, library, selectedText ) {
		const selected = encodeURIComponent( selectedText || '' );
		const media = ( type, space ) => `jsp/admin/plugins/library/SelectMedia.jsp?plugin_name=library&media_type=${type || ''}&browser_id_space=${space || ''}`
			+ `&browser_selected_space_id=${space || ''}&input=${input}&mode=0&selected_text=`;
		switch ( name ) {
			case 'lutece':
				return `jsp/admin/insert/GetAvailableInsertServices.jsp?input=${input}&selected_text=${selected}&mode=0`;
			case 'luteceImage':
				return media( library.imageType, library.imageSpace );
			case 'lutecePdf':
				return media( library.pdfType, library.pdfSpace );
			case 'lutecePageLink':
				return `jsp/admin/insert/DisplayInsertService.jsp?insert_service_type=PAGE_INSERT_SERVICE&plugin_name=pagelinkservice&input=${input}&selected_text=${selected}`;
			case 'luteceNewsletterBlog':
				return `jsp/admin/insert/DisplayInsertService.jsp?insert_service_type=blog_selection_is&plugin_name=newsletter-blog&input=${input}&mode=0&selected_text=${selected}`;
			default:
				return null;
		}
	}

	/**
	 * The dialog shared by the editors of the page, created on first use. A native dialog: it needs no script of the admin theme, which some
	 * pages with an editor do not load (the forms of the portlets).
	 */
	function getDialog( closeLabel ) {
		if ( dialog ) {
			return dialog;
		}
		const element = document.createElement( 'dialog' );
		element.className = 'lutece-editor-dialog p-0 border-0 rounded shadow-lg';
		// The dialog takes the whole window, less a margin showing the backdrop; the frame takes the height left under the header. The flex
		// column is on an inner box: on the dialog itself, an inline display would show it while closed
		element.style.cssText = 'width:calc(100vw - 2rem);height:calc(100dvh - 2rem);max-width:none;max-height:none;overflow:hidden';
		element.innerHTML = `<div class="d-flex flex-column h-100">
<div class="d-flex align-items-center justify-content-between gap-2 border-bottom px-3 py-2">
	<h2 class="h3 m-0"></h2>
	<button type="button" class="btn-close" aria-label="${closeLabel}"></button>
</div>
<iframe class="d-block w-100 border-0 flex-grow-1" style="min-height:0" title=""></iframe>
</div>`;
		document.body.appendChild( element );
		const iframe = element.querySelector( 'iframe' );
		element.querySelector( '.btn-close' ).addEventListener( 'click', ( ) => element.close( ) );
		element.addEventListener( 'close', ( ) => {
			iframe.src = 'about:blank';
			window.luteceEditorInsert = undefined;
		} );
		dialog = {
			iframe,
			title: element.querySelector( 'h2' ),
			show: ( ) => element.showModal( ),
			hide: ( ) => element.close( )
		};
		return dialog;
	}

	/**
	 * Opens the insert service of a button
	 *
	 * @param {string} lang fr or en
	 * @param {string} name the button
	 * @param {string} url the URL of the insert service
	 * @param {function(string)} insert inserts the HTML returned by the insert service into the editor that opened it
	 */
	function openInsertService( lang, name, url, insert ) {
		const texts = labels( lang );
		const d = getDialog( texts.close );
		window.luteceEditorInsert = {
			insert,
			close( ) {
				d.hide( );
			}
		};
		d.title.textContent = texts[ name ] || '';
		d.iframe.title = texts[ name ] || '';
		d.iframe.src = url;
		d.show( );
	}

	return { SERVICE_BUTTONS, labels, icon, getAvailable, availableButtons, expandButtons, serviceUrl, openInsertService };
} )( );
