/*
 * Front office Markdown editor (markdown-text-editor), loaded by util/editor/editor_markdown.html.
 * The editor keeps the native textarea: the form posts Markdown, which IEditorBbcodeService.parse turns into HTML on the server.
 */
( function ( window, document ) {
	'use strict';

	const LABELS = {
		fr: {
			'Editor toolbar': "Barre d'outils de l'éditeur",
			'Markdown editor': 'Éditeur Markdown',
			'Write your markdown...': 'Saisissez votre texte...',
			'Heading': 'Titre',
			'Heading level': 'Niveau de titre',
			'Bold': 'Gras',
			'Italic': 'Italique',
			'Strikethrough': 'Barré',
			'Blockquote': 'Citation',
			'Unordered list': 'Liste à puces',
			'Ordered list': 'Liste numérotée',
			'Check list': 'Liste de cases à cocher',
			'Indent': 'Augmenter le retrait',
			'Outdent': 'Diminuer le retrait',
			'Code': 'Code',
			'Code Block': 'Bloc de code',
			'Horizontal Rule': 'Ligne horizontale',
			'Table': 'Tableau',
			'Link': 'Lien',
			'Link text': 'Texte du lien',
			'URL': 'URL',
			'Image': 'Image',
			'Image link': 'Image',
			'Alt text': 'Texte alternatif',
			'Uploading...': 'Envoi en cours...',
			'Upload failed': "L'envoi a échoué",
			'Undo': 'Annuler',
			'Redo': 'Rétablir',
			'Preview': 'Aperçu',
			'Apply': 'Appliquer',
			'Close': 'Fermer',
			'Variables': 'Variables',
			'Insert variable': 'Insérer une variable'
		}
	};

	// The tools whose HTML the server keeps (see EditorBbcodeService): no check list, its checkboxes would be removed
	const DEFAULT_TOOLBAR = [ 'heading', 'bold', 'italic', 'strikethrough', 'blockquote', 'ul', 'ol', 'link', 'image', 'code', 'codeblock', 'table', 'hr', 'undo', 'redo', 'preview' ];

	function theme( ) {
		const value = document.documentElement.getAttribute( 'data-bs-theme' );
		return value === 'dark' ? 'dark' : 'light';
	}

	function create( textarea, options ) {
		if ( textarea.dataset.luteceMarkdownEditor ) {
			return;
		}
		textarea.dataset.luteceMarkdownEditor = 'true';
		const labels = LABELS[ options.lang ] || {};
		new window.MarkdownEditor( textarea, {
			mode: options.mode || 'hybrid',
			toolbar: options.toolbar || DEFAULT_TOOLBAR,
			theme: theme( ),
			labels: labels,
			label: labels[ 'Markdown editor' ],
			placeholder: labels[ 'Write your markdown...' ],
			minHeight: options.minHeight || 200,
			maxHeight: options.maxHeight || 500
		} );
	}

	function init( options ) {
		if ( typeof window.MarkdownEditor !== 'function' ) {
			console.error( 'LuteceMarkdownEditor: markdown-text-editor is not loaded' );
			return;
		}
		const run = function ( ) {
			document.querySelectorAll( options.selector ).forEach( function ( element ) {
				if ( element.tagName === 'TEXTAREA' ) {
					create( element, options );
				}
			} );
		};
		if ( document.readyState === 'loading' ) {
			document.addEventListener( 'DOMContentLoaded', run );
		} else {
			run( );
		}
	}

	window.LuteceMarkdownEditor = { init: init };
} )( window, document );
