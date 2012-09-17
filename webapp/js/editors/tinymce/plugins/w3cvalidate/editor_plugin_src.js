/**
 * $Id: editor_plugin_src.js 59 2006-11-17 13:30:19Z tan $
 *
 * @author Thomas Andersen
 * @copyright Copyright © 2008 Thomas Andersen. All rights reserved.
 */
(function() {
	// Load plugin specific language pack
	tinymce.PluginManager.requireLangPack('w3cvalidate');

	tinymce.create('tinymce.plugins.W3CValidate', {
		/**
		 * Initializes the plugin, this will be executed after the plugin has been created.
		 * This call is done before the editor instance has finished it's initialization so use the onInit event
		 * of the editor instance to intercept that event.
		 *
		 * @param {tinymce.Editor} ed Editor instance that the plugin is initialized in.
		 * @param {string} url Absolute URL to where the plugin is located.
		 */
		init : function(ed, url) {
      foo = url;
			// Register the command so that it can be invoked by using tinyMCE.activeEditor.execCommand('mceExample');
			ed.addCommand('w3cvalidate', function() {
				ed.windowManager.open({
					file : url + '/w3cvalidate.htm',
					width : 320 + parseInt(ed.getLang('w3cvalidate.delta_width', 0)),
					height : 100 + parseInt(ed.getLang('w3cvalidate.delta_height', 0)),
					inline : 1
				}, {
					plugin_url : url // Plugin absolute URL
					//some_custom_arg : 'custom arg' // Custom argument
				});
			});

			// Register example button
			ed.addButton('w3cvalidate', {
				title : 'w3cvalidate.desc',
				cmd : 'w3cvalidate',
				image : url + '/img/xhtml.gif'
			});
    
			// Add a node change handler, selects the button in the UI when a image is selected
			ed.onNodeChange.add(function(ed, cm, n) {
				//cm.setActive('w3cvalidate', n.nodeName == 'IMG');
			});
		},

		/**
		 * Creates control instances based in the incomming name. This method is normally not
		 * needed since the addButton method of the tinymce.Editor class is a more easy way of adding buttons
		 * but you sometimes need to create more complex controls like listboxes, split buttons etc then this
		 * method can be used to create those.
		 *
		 * @param {String} n Name of the control to create.
		 * @param {tinymce.ControlManager} cm Control manager to use inorder to create new control.
		 * @return {tinymce.ui.Control} New control instance or null if no control was created.
		 */
		createControl : function(n, cm) {
			return null;
		},

		/**
		 * Returns information about the plugin as a name/value array.
		 * The current keys are longname, author, authorurl, infourl and version.
		 *
		 * @return {Object} Name/value array containing information about the plugin.
		 */
		getInfo : function() {
			return {
				longname : 'W3C Validator Plugin',
				author : 'Enonic',
				authorurl : 'http://www.enonic.com',
				infourl : 'http://www.enonic.com',
				version : "1.0"
			};
		}
	});

	// Register plugin
	tinymce.PluginManager.add('w3cvalidate', tinymce.plugins.W3CValidate);
})();