<#--
Macro: div

Description: generate a div element with the specified attributes.

Parameters:
- id (string): ID attribute of the div element (default: '')
- class (string): class attribute of the div element (default: '')
- hide (list): list of display settings to hide the element on specific screen sizes (default: [])
- collapsed (boolean): boolean to determine if the element is initially collapsed (default: false)
- align (string): alignment of the content within the div element (default: '')
- params (string): additional parameters to include in the div element (default: '')
- attrs (hash, optional): additional HTML attributes as name/value pairs, e.g. {'aria-hidden': 'true'}. Prefer it to params: the macro writes the attribute and its value is escaped like any value of the page.

Snippet:

    Simple div with a class:

    <@div class='card-body'>
        Card content goes here.
    </@div>

    Div hidden on small screens with centered alignment:

    <@div class='mt-3' hide=['sm'] align='center'>
        This content is hidden on small screens and centered.
    </@div>

    Collapsible div with an ID:

    <@div id='details-section' class='p-3' collapsed=true>
        Additional details that can be toggled.
    </@div>

-->
<#macro div id='' class='' hide=[] collapsed=false align='' params='' attrs={} deprecated...>
<@deprecatedWarning args=deprecated />
<#local classExtra = collapsed?then( 'collapse', '' ) + ' ' + alignmentSettings( align, '' ) + ' ' + displaySettings( hide, 'block' ) />
<div<@commonAttrs id=id class=class classExtra=classExtra params=params attrs=attrs />>
	<#nested>
</div>
</#macro>