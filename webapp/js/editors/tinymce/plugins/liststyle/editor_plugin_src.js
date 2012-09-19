/**
 * $Id$
 *
 * @author PolicyPoint Technologies Pty. Ltd.
 * @copyright Copyright 2005-2008, PolicyPoint Technologies Pty. Ltd.
 */

(function() {
	
	// Load language pack
	tinymce.PluginManager.requireLangPack("liststyle");
	
	tinymce.create("tinymce.plugins.ListStylePlugin", {
		init: function(ed, url) {

			// Register mceListStyle command
			ed.addCommand("mceListStyle", function(ui, v) {
				var listStyleType = '', list = '', listStart;
				var se = ed.selection.getNode(); // selectedElement
				var p = ed.dom.getParent(se, 'ol,ul'); // parent
				if (p) {
					list = p.nodeName.toLowerCase();
					listStyleType = p.style.listStyleType ? p.style.listStyleType : list == 'ol' ? 'decimal' : 'disc';
					//alert('listStyleType = ' + listStyleType);
					// Select the node so it can be used after the command exits.
					// Was seeing if this could be used to select the list rather
					// than the items in IE, but no joy.
					//ed.selection.select(p);
					listStart = parseInt(p.start);
					if (listStart < 1)
						listStart = 1;
					ed.windowManager.open({
						url : url + '/liststyle.htm',
						width : 340 + parseInt(ed.getLang('liststyle.delta_width', 0)),
						height : 300 + (tinymce.isIE ? 40 : 0) + parseInt(ed.getLang('liststyle.delta_height', 0)),
						inline : 1
					}, {
						plugin_url : url,
						listStyleType : listStyleType,
						list : list,
						start : listStart,
						classAttr : p.className,
						isIE : tinymce.isIE
					});
				}
			});

			ed.onInit.add(function() {
				if (ed && ed.plugins.contextmenu) {
					ed.plugins.contextmenu.onContextMenu.add(function(th, m, e) {
						var p = ed.dom.getParent(ed.selection.getNode(), 'ol,ul');

						if (p) {
							//m.removeAll();
							m.add({title : 'liststyle.desc', cmd : 'mceListStyle', ui : true});
						}
					});
				}
			});

			ed.onNodeChange.add(function(ed, cm, n) {
				var p = ed.dom.getParent(n, 'ol,ul');
				cm.setDisabled('liststyle', !p);
			});

			// Register liststyle button
			ed.addButton("liststyle", {
				title: "liststyle.desc",
				cmd: "mceListStyle",
				image: url + "/images/liststyle.gif"
			});
		},

		getInfo : function() {
			return {
				longname: "ListStyle",
				author : 'PolicyPoint Technologies Pty. Ltd.',
				authorurl : 'http://policypoint.net/',
				infourl : 'http://policypoint.net/tinymce/docs/plugin_liststyle.html',
				version: "3.0"
			};
		}

	});

	// Register plugin
	tinymce.PluginManager.add("liststyle", tinymce.plugins.ListStylePlugin);
})();
