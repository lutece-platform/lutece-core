<#-- Macro: li

Description: Generates an HTML list item (<li>) with optional attributes and content.

Parameters:
- id (string, optional): the ID attribute of the list item.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.
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
<#macro li id='' params={} class='' hide=[] align='' deprecated...>
<@deprecatedWarning args=deprecated />
<#local classExtra = alignmentSettings( align, '' ) + ' ' + displaySettings( hide, 'block' ) />
<li<@commonAttrs id=id class=class classExtra=classExtra params=params />>
<#nested>
</li>
</#macro>