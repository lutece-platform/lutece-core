/**
 * LUTECE Plugin
 *
 * @author Lutece - Mairie de Paris
 */
(function () {
    var lutece = (function () {
        'use strict';
        tinymce.PluginManager.requireLangPack( 'lutece', 'fr_FR');
        tinymce.PluginManager.add( 'lutece', function (editor, url) {
            /*
            Add a LUTECE icon
             */
            editor.ui.registry.addIcon( 'luteceicon', '<svg width="24" height="24"><use xlink:href="themes/admin/shared/js/editors/tinymce5/plugins/lutece/lutece.svg#logo"></use></svg>');
            
            let luteceConf = false;

            /*
            Define configuration for the iframe
             */
            const luteceDialogConfig = {
                title: 'Lutece contents',
                url: 'jsp/admin/insert/GetAvailableInsertServices.jsp?input=tinymce5&selected_text=',
                buttons: [
                    {
                        type: 'cancel',
                        name: 'cancel',
                        text: 'Close Dialog',
                        primary: false,
                        align: 'end'
                    }
                ],
            };

            // Toolbar button
            editor.ui.registry.addButton( 'lutece', {
                text: "",
                tooltip: "Add Lutece contents",
                icon: "luteceicon",
                onAction: () => {
                    luteceConf = editor.windowManager.openUrl( luteceDialogConfig )
                }
            });

        });
        // Load the required translation files
        // 
      }());
})();