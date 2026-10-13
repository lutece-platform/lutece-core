<#--
Macro: p

Description: Generates an HTML paragraph element with a specified ID, class, display settings, alignment, and nested content block.

Parameters:
- id (string, optional): the ID for the paragraph element.
- class (string, optional): additional classes to add to the paragraph element.
- hide (list, optional): a list of display settings to hide the paragraph element, e.g., ["md", "lg"].
- collapsed (boolean, optional): whether to collapse the paragraph element.
- align (string, optional): the alignment setting for the paragraph element, e.g., "center" or "right".
- params (string, optional): additional parameters to add to the HTML code.
- attrs (hash, optional): additional HTML attributes as name/value pairs, e.g. {'aria-hidden': 'true'}. Prefer it to params: the macro writes the attribute and its value is escaped like any value of the page.

Snippet:

    Simple paragraph with a class:

    <@p class='text-muted'>
        This is a descriptive paragraph.
    </@p>

    Right-aligned paragraph hidden on mobile:

    <@p align='right' hide=['sm'] class='fw-bold'>
        Important notice visible on larger screens.
    </@p>

-->
<#macro p id='' params='' attrs={} class='' hide=[] collapsed=false align='' deprecated...>
<@deprecatedWarning args=deprecated />
<#local classExtra = collapsed?then( 'collapse', '' ) + ' ' + displaySettings( hide, 'block' ) + ' ' + alignmentSettings( align, '' ) />
<p<@commonAttrs id=id class=class classExtra=classExtra params=params attrs=attrs />>
	<#nested>
</p>
</#macro>