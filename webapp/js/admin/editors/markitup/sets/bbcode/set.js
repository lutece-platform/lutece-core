// ----------------------------------------------------------------------------
// markItUp!
// ----------------------------------------------------------------------------
// Copyright (C) 2008 Jay Salvat
// http://markitup.jaysalvat.com/
// ----------------------------------------------------------------------------
// BBCode tags example
// http://en.wikipedia.org/wiki/Bbcode
// ----------------------------------------------------------------------------
// Feel free to add more tags
// ----------------------------------------------------------------------------


mySettings = {
	previewParserPath:	'~/../../../parser/parserBbcode', // path to your BBCode parser
	previewParser:false, 
	markupSet: [
		{name:'Gras', key:'B', openWith:'[b]', closeWith:'[/b]'},
		{name:'Italique', key:'I', openWith:'[i]', closeWith:'[/i]'},
		{name:'Souligner', key:'U', openWith:'[u]', closeWith:'[/u]'},
		{separator:'---------------' },
		{name:'Liens', key:'L', openWith:'[url=[![Url]!]]', closeWith:'[/url]', placeHolder:'Your text to link here...'},
		{separator:'---------------' },
		{	name:'Couleurs', 
			className:'colors', 
			openWith:'[color=[![Color]!]]', 
			closeWith:'[/color]', 
				dropMenu: [
					{name:'Jaune',	openWith:'[color=yellow]', 	closeWith:'[/color]', className:"col1-1" },
					{name:'Orange',	openWith:'[color=orange]', 	closeWith:'[/color]', className:"col1-2" },
					{name:'Rouge', 	openWith:'[color=red]', 	closeWith:'[/color]', className:"col1-3" },
					
					{name:'Bleu', 	openWith:'[color=blue]', 	closeWith:'[/color]', className:"col2-1" },
					{name:'Violet', openWith:'[color=purple]', 	closeWith:'[/color]', className:"col2-2" },
					{name:'Vert', 	openWith:'[color=green]', 	closeWith:'[/color]', className:"col2-3" },
					
					{name:'Blanc', 	openWith:'[color=white]', 	closeWith:'[/color]', className:"col3-1" },
					{name:'Gris', 	openWith:'[color=gray]', 	closeWith:'[/color]', className:"col3-2" },
					{name:'Noir',	openWith:'[color=black]', 	closeWith:'[/color]', className:"col3-3" }
				]
		},
		{separator:'---------------' },
		{name:'Taille', key:'S', openWith:'[size=[![Text size]!]]', closeWith:'[/size]',
		dropMenu :[
			{name:'Grand', openWith:'[size=50]', closeWith:'[/size]' },
			{name:'Normal', openWith:'[size=30]', closeWith:'[/size]' },
			{name:'Petit', openWith:'[size=10]', closeWith:'[/size]' }
		]},
		{separator:'---------------' },
		{name:'Liste &agrave; puce', openWith:'[list]\n', closeWith:'\n[/list]'},
		{name:'Liste ordonn&eacute;e', openWith:'[list=[![Starting number]!]]\n', closeWith:'\n[/list]'}, 
		{name:'Element de liste', openWith:'[*] '},
		{separator:'---------------' },
		{name:'Code', openWith:'[code]', closeWith:'[/code]'}, 
		{separator:'---------------' },
		{name:'Nettoyer', className:"clean", replaceWith:function(markitup) { return markitup.selection.replace(/\[(.*?)\]/g, "") } },
		{name:'Pr&eacute;visualiser - Alt + Click pour supprimer la pr&eacute;visualisation', className:"preview", call:'preview' },
		{separator:'---------------' }
	]
}