<#-- Macro: li

Description: Generates an HTML list item (<li>) with optional attributes and content.

Parameters:
- id (string, optional): the ID attribute of the list item.
- params (string, optional): additional attributes to add to the list item, in the form of a string of HTML attributes.
- attrs (hash, optional): additional HTML attributes as name/value pairs, e.g. {'aria-hidden': 'true'}. Prefer it to params: the macro writes the attribute and its value is escaped like any value of the page.
- class (string, optional): the class attribute of the list item.
- hide (list, optional): a list of strings representing the CSS display values for hiding the list item at different screen sizes (e.g. ["md:hidden"] to hide the list item on medium screens and up).
- align (string, optional): the alignment of the list item within its parent element ("left", "center", or "right").

Snippet:

    Simple list item:

    <@li>This is a list item</@li>

    List item with class and ID:

    <@li class='fw-bold text-primary' id='item-1'>
        Important item
    </@li>

-->
<#macro li id='' params='' class='' hide=[] align='' attrs={} deprecated...>
<@deprecatedWarning args=deprecated />
<#local classExtra = alignmentSettings( align, '' ) + ' ' + displaySettings( hide, 'block' ) />
<li<@commonAttrs id=id class=class classExtra=classExtra params=params attrs=attrs />>
<#nested>
</li>
</#macro>