List Style Plugin for the TinyMCE Editor
----------------------------------------

By Scott Eade (seade at policypoint dot net)
May 7, 2008
Version 3.0

The List Style plugin allows you to set the list-style-type CSS property on
lists within the TinyMCE editor.

Installation Instructions
-------------------------

* Install the files under the TinyMCE plugins directory.
* Add plugin to TinyMCE plugin option list.  Example: plugins : "liststyle"
* Add the liststyle button to the button list.
  Example: theme_advanced_button3_add : "liststyle"

Initialization example
----------------------

    tinyMCE.init({
        theme : "advanced",
        mode : "textareas",
        plugins : "liststyle",
        theme_advanced_buttons3_add : "liststyle"
    });

Usage Instructions
------------------

* Position the cursor on an item in the list whose style you wish to alter and
  click the liststyle button.
* Select the list style type and click "Update".
* The plugin supports nested lists - the style of the list at the level of the
  item at the cursor position is altered, not the others.

History
-------

* 2008-05-07: Version 3.0 released.
  - Updated for TinyMCE 3.x.
* 2007-11-28: Version 1.1.3 released.
  - Tweaked the popup window height to allow for IE7 security address bar.
* 2006-08-25: Version 1.1.2 released.
  - Added Usage instructions.
  - Added support for undo and redo.
* 2006-03-25: Version 1.1.1 released.
  - Fixed Javascript error under IE.  Thanks to Bryan Costin for highlighting
    this issue.
* 2006-03-10: Version 1.1.0 released.
  - Updated for TinyMCE plugin architecture change introduced in TinyMCE 2.0.3.
    Use ListStyle 1.0.1 if you are using a TinyMCE 2.0.0 - 2.0.2.
* 2006-01-30: Version 1.0.2 released.
  - Fixed error that occurred when invoked on a non-LI element.
  - Consistently use single quotes in plugin.
  - Added compressed plugin file.
* 2005-10-11: Version 1.0.1 released.  Changes made thanks to spocke:
  - Fixed so it uses inst.getFocusElement instead of the deprecated
    tinyMCE.selectedElement.
  - Moved the style information to a separate .css file.
  - Made it possible for translation of all labels.
  - Translated the plugin into Swedish.
* 2005-10-07: Version 1.0 released.

Copyright and license
---------------------

* Copyright 2005-2008 PolicyPoint Technologies Pty. Ltd.
* License: LGPL 2.0
