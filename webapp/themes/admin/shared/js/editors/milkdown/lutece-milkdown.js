/**
 * Lutece integration of Milkdown (Crepe), a back office rich text editor that edits Markdown.
 *
 * The field keeps HTML, as for the other editors: the HTML of the textarea is parsed into the document through the schema of the editor,
 * and on every change the document is serialized back to HTML into the textarea. What Markdown cannot express (classes, styles, div,
 * iframes...) is not kept: the editor suits new or simple content.
 *
 * The options of Lutece, of the page (config of @initEditor) and of the editor profile apply in that order: features (on/off by feature
 * name), featureConfigs (merged per feature), placeholder, height, and luteceButtons, the Lutece buttons of the top bar and of the slash menu
 * (lutece-editor-insert.js: lutece, luteceImage, lutecePdf, lutecePageLink, luteceNewsletterBlog, luteceUploadPdf, luteceAll; default
 * luteceAll). When the plugin library offers it (lutece-editor-upload.js), the images of the image block are uploaded into its image space.
 *
 * @author Lutece - Mairie de Paris
 */
window.LuteceMilkdown = window.LuteceMilkdown || ( function () {
	'use strict';

	const INPUT = 'milkdown';

	const TEXTS = {
		en: {
			placeholder: 'Type / to insert a block',
			paragraph: 'Text',
			heading: 'Heading'
		},
		fr: {
			placeholder: 'Tapez / pour insérer un bloc',
			paragraph: 'Paragraphe',
			heading: 'Titre',
			blockEdit: {
				textGroup: {
					label: 'Texte', text: { label: 'Paragraphe' }, h1: null, h2: { label: 'Titre 2' }, h3: { label: 'Titre 3' },
					h4: { label: 'Titre 4' }, h5: { label: 'Titre 5' }, h6: null, quote: { label: 'Citation' }, divider: { label: 'Séparateur' }
				},
				listGroup: { label: 'Listes', bulletList: { label: 'Liste à puces' }, orderedList: { label: 'Liste numérotée' }, taskList: { label: 'Liste de tâches' } },
				advancedGroup: { label: 'Avancé', image: { label: 'Image' }, codeBlock: { label: 'Bloc de code' }, table: { label: 'Tableau' }, math: null }
			},
			toolbar: { boldLabel: 'Gras', italicLabel: 'Italique', strikethroughLabel: 'Barré', codeLabel: 'Code', linkLabel: 'Lien' },
			imageBlock: {
				inlineUploadButton: 'Téléverser', inlineUploadPlaceholderText: 'ou coller un lien', inlineConfirmButton: 'Valider',
				blockUploadButton: 'Téléverser un fichier', blockUploadPlaceholderText: 'ou coller un lien', blockConfirmButton: 'Valider',
				blockCaptionPlaceholderText: 'Légende de l’image'
			}
		}
	};

	/**
	 * The HTML of the document, without the attributes the schema adds for its own use
	 */
	function cleanHtml( container ) {
		container.querySelectorAll( '*' ).forEach( el => {
			[ ...el.attributes ].forEach( attr => {
				if ( attr.name.startsWith( 'data-' ) ) {
					el.removeAttribute( attr.name );
				}
			} );
			if ( /^H[1-6]$/.test( el.tagName ) ) {
				el.removeAttribute( 'id' );
			}
			if ( el.tagName === 'IMG' && el.getAttribute( 'title' ) === el.getAttribute( 'alt' ) ) {
				el.removeAttribute( 'title' );
			}
			if ( ( el.tagName === 'TD' || el.tagName === 'TH' ) && /^text-align:\s*left;?$/.test( el.getAttribute( 'style' ) || '' ) ) {
				el.removeAttribute( 'style' );
			}
		} );
		// A list item or a cell holding a single paragraph holds its text directly
		container.querySelectorAll( 'li, td, th' ).forEach( el => {
			if ( el.children.length === 1 && el.firstElementChild.tagName === 'P' && el.childNodes.length === 1 ) {
				el.replaceChildren( ...el.firstElementChild.childNodes );
			}
		} );
		// The empty paragraphs the editor keeps at the end of the document
		while ( container.lastElementChild && container.lastElementChild.tagName === 'P' && container.lastElementChild.textContent.trim( ) === ''
				&& !container.lastElementChild.querySelector( 'img' ) ) {
			container.lastElementChild.remove( );
		}
		return container.innerHTML;
	}

	/**
	 * The editor, the document of the editor as HTML, and the insertion of HTML in place of the selection
	 */
	function bindDocument( kit, crepe ) {
		const run = fn => crepe.editor.action( ctx => fn( ctx.get( kit.editorViewCtx ), ctx.get( kit.schemaCtx ) ) );
		return {
			getHtml( ) {
				return run( ( view, schema ) => {
					const container = document.createElement( 'div' );
					container.appendChild( kit.DOMSerializer.fromSchema( schema ).serializeFragment( view.state.doc.content, { document } ) );
					return cleanHtml( container );
				} );
			},
			insertHtml( html ) {
				run( ( view, schema ) => {
					const container = document.createElement( 'div' );
					container.innerHTML = html;
					view.dispatch( view.state.tr.replaceSelection( kit.DOMParser.fromSchema( schema ).parseSlice( container ) ).scrollIntoView( ) );
					view.focus( );
				} );
			},
			selectedText( ) {
				return run( view => view.state.doc.textBetween( view.state.selection.from, view.state.selection.to, ' ' ) );
			},
			clearCurrentBlockText( ) {
				run( view => {
					const { $from } = view.state.selection;
					if ( $from.parent.isTextblock && $from.parent.content.size > 0 ) {
						view.dispatch( view.state.tr.delete( $from.start( ), $from.end( ) ) );
					}
				} );
			}
		};
	}

	/**
	 * Runs a Lutece button: opens its insert service, or uploads a PDF file
	 */
	function runButton( name, lang, library, doc ) {
		const text = doc.selectedText( );
		if ( name === 'luteceUploadPdf' ) {
			LuteceEditorUpload.pickFile( 'application/pdf' )
				.then( file => LuteceEditorUpload.upload( file, 'pdf' ) )
				.then( result => doc.insertHtml( LuteceEditorUpload.pdfLink( result, text ) ) )
				.catch( e => window.alert( e.message ) );
			return;
		}
		LuteceEditorInsert.openInsertService( lang, name, LuteceEditorInsert.serviceUrl( name, INPUT, library, text ), html => doc.insertHtml( html ) );
	}

	/**
	 * The Lutece group of the top bar. Its buttons have no label of their own: the icon carries it, as tooltip and accessible name.
	 */
	function buildTopBarGroup( builder, names, lang, library, doc ) {
		if ( names.length === 0 ) {
			return;
		}
		const texts = LuteceEditorInsert.labels( lang );
		const group = builder.addGroup( 'lutece', 'Lutece' );
		names.forEach( name => group.addItem( name, {
			icon: LuteceEditorInsert.icon( name, texts[ name ] ),
			active: ( ) => false,
			onRun: ( ) => runButton( name, lang, library, doc )
		} ) );
	}

	/**
	 * The Lutece group of the slash menu: the text typed after / is cleared first, as the items of the editor do
	 */
	function buildSlashMenuGroup( builder, names, lang, library, doc ) {
		if ( names.length === 0 ) {
			return;
		}
		const texts = LuteceEditorInsert.labels( lang );
		const group = builder.addGroup( 'lutece', 'Lutece' );
		names.forEach( name => group.addItem( name, {
			label: texts[ name ] || name,
			icon: LuteceEditorInsert.icon( name ),
			onRun: ( ) => {
				doc.clearCurrentBlockText( );
				runButton( name, lang, library, doc );
			}
		} ) );
	}

	/**
	 * The configuration of the features: the texts of the language, the upload of the images, the heading levels of the TinyMCE editor
	 */
	function featureConfigs( lang, upload ) {
		const texts = TEXTS[ lang ] || TEXTS.en;
		const headings = [ { label: texts.paragraph, level: null } ].concat( [ 2, 3, 4, 5 ].map( level => ( { label: `${texts.heading} ${level}`, level } ) ) );
		const configs = {
			placeholder: { text: texts.placeholder, mode: 'block' },
			'block-edit': Object.assign( { textGroup: { h1: null, h6: null } }, texts.blockEdit ),
			toolbar: Object.assign( {}, texts.toolbar ),
			'image-block': Object.assign( {}, texts.imageBlock ),
			'top-bar': { headingOptions: headings }
		};
		if ( upload.image ) {
			const onUpload = file => LuteceEditorUpload.upload( file, 'image' ).then( result => result.location );
			Object.assign( configs[ 'image-block' ], { onUpload, inlineOnUpload: onUpload, blockOnUpload: onUpload } );
		}
		return configs;
	}

	/**
	 * Creates an editor in place of a textarea
	 */
	async function createEditor( kit, field, options, available, lang, library ) {
		const root = document.createElement( 'div' );
		root.className = 'lutece-milkdown';
		root.style.setProperty( '--lutece-milkdown-height', ( options.height || 300 ) + 'px' );
		field.insertAdjacentElement( 'afterend', root );
		field.hidden = true;

		const initial = document.createElement( 'div' );
		initial.innerHTML = field.value;
		const crepe = new kit.CrepeBuilder( { root, defaultValue: { type: 'html', dom: initial } } );
		const doc = bindDocument( kit, crepe );
		const luteceNames = LuteceEditorInsert.expandButtons( Array.isArray( options.luteceButtons ) ? options.luteceButtons : [ 'luteceAll' ], available );

		const configs = featureConfigs( lang, options.upload );
		Object.keys( options.featureConfigs || {} ).forEach( key => {
			configs[ key ] = Object.assign( configs[ key ] || {}, options.featureConfigs[ key ] );
		} );
		if ( options.placeholder ) {
			configs.placeholder.text = options.placeholder;
		}
		const ownTopBar = configs[ 'top-bar' ].buildTopBar;
		configs[ 'top-bar' ].buildTopBar = builder => {
			if ( typeof ownTopBar === 'function' ) {
				ownTopBar( builder );
			}
			buildTopBarGroup( builder, luteceNames, lang, library, doc );
		};
		const ownMenu = configs[ 'block-edit' ].buildMenu;
		configs[ 'block-edit' ].buildMenu = builder => {
			if ( typeof ownMenu === 'function' ) {
				ownMenu( builder );
			}
			buildSlashMenuGroup( builder, luteceNames, lang, library, doc );
		};

		const features = Object.assign( { 'block-edit': true, toolbar: true, 'top-bar': true, 'link-tooltip': true, 'image-block': true, 'list-item': true,
			placeholder: true, table: true, cursor: true }, options.features );
		const byName = { 'block-edit': 'blockEdit', toolbar: 'toolbar', 'top-bar': 'topBar', 'link-tooltip': 'linkTooltip', 'image-block': 'imageBlock',
			'list-item': 'listItem', placeholder: 'placeholder', table: 'table', cursor: 'cursor' };
		Object.keys( byName ).forEach( name => {
			if ( features[ name ] ) {
				crepe.addFeature( kit.features[ byName[ name ] ], configs[ name ] );
			}
		} );

		crepe.on( listener => listener.markdownUpdated( ( ) => {
			field.value = doc.getHtml( );
		} ) );
		await crepe.create( );
		return crepe;
	}

	/**
	 * Creates the editors of the fields matching settings.selector
	 *
	 * @param {Object} settings selector, insert (the insert services the page allows), config (of @initEditor), profileConfig,
	 *                          lang, library (media types and spaces of the library insert service)
	 */
	async function init( settings ) {
		const kit = window.LuteceMilkdownKit;
		const fields = document.querySelectorAll( settings.selector );
		if ( !kit || fields.length === 0 ) {
			return;
		}
		const { buttons: available, upload } = await LuteceEditorInsert.getAvailable( settings.insert );
		const options = Object.assign( { height: 300 }, settings.config, settings.profileConfig, { upload } );
		for ( const field of fields ) {
			await createEditor( kit, field, options, available, settings.lang, settings.library || {} );
		}
	}

	return { init, cleanHtml };
} )( );
