/**
 * $Id: editor_plugin_src.js 201 2008-05-31 15:56:56Z spocke $
 *
 * @author Laurent HOHL
 * 
 */

(function() {
	// Load plugin specific language pack
	tinymce.PluginManager.requireLangPack('lutece');

	tinymce.create('tinymce.plugins.lutecePlugin', {
		/**
		 * Initializes the plugin, this will be executed after the plugin has been created.
		 * This call is done before the editor instance has finished it's initialization so use the onInit event
		 * of the editor instance to intercept that event.
		 *
		 * @param {tinymce.Editor} ed Editor instance that the plugin is initialized in.
		 * @param {string} url Absolute URL to where the plugin is located.
		 */
		init : function(ed, url) {
			// Register the command so that it can be invoked by using tinyMCE.activeEditor.execCommand('mcelutece');
		  ed.addCommand('mcelutece', function() {
		  
      var path="";
		  var loc = "\"" + location + "\"";  
      
      if( loc.indexOf("/plugins/") > 0){
         if(tinymce.isIE){
               path="../../";
           }else{
              path="jsp/admin/";
           }
         }
        
	      window.open( path + "insert/GetAvailableInsertServices.jsp?input=" + ed.name + "&selected_text="+ ed.selection.getContent({format : 'text'}),
                 "",
                  "toolbar=no, scrollbars=yes, status=yes, location=no, directories=no, menubar=no, width=550, height=450, left=300, top=100"
                );  
      });
      
			// Register lutece button
			ed.addButton('lutece', {
				title : 'lutece.desc',
				cmd : 'mcelutece',
				image : url + '/img/lutece.png'
			});

			// Add a node change handler, selects the button in the UI when a image is selected
			ed.onNodeChange.add( function(ed, cm, n) {
				cm.setActive('lutece', n.nodeName == 'IMG');
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
				longname : 'lutece plugin',
				author : 'Laurent HOHL',
				authorurl : 'http://dev.lutece.paris.fr',
				infourl : 'http://dev.lutece.paris.fr',
				version : "1.0"
			};
		}
	});

	// Register plugin
	tinymce.PluginManager.add('lutece', tinymce.plugins.lutecePlugin);
})();
