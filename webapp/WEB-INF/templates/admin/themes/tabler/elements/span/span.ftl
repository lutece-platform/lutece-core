<#--
Macro: span

Description: Generates an HTML span element with a specified ID, class, display settings, alignment, and nested content block.

Parameters:
- id (string, optional): the ID for the span element.
- class (string, optional): additional classes to add to the span element.
- hide (list, optional): a list of display settings to hide the span element, e.g., ["md", "lg"].
- collapsed (boolean, optional): whether to collapse the span element.
- align (string, optional): the alignment setting for the span element, e.g., "center" or "right".
- params (string, optional): additional parameters to add to the HTML code.
- attrs (hash, optional): additional HTML attributes as name/value pairs, e.g. {'aria-hidden': 'true'}. Prefer it to params: the macro writes the attribute and its value is escaped like any value of the page.

Snippet:

    Simple span with a class:

    <@span class='badge bg-primary'>
        Active
    </@span>

    Span hidden on small screens with custom ID:

    <@span class='text-muted' hide=['sm'] id='status-label'>
        Status: Online
    </@span>

-->
<#macro span id='' class='' hide=[] collapsed=false align='' params='' attrs={} deprecated...>
<@deprecatedWarning args=deprecated />
<#local classExtra = collapsed?then( 'collapse', '' ) + ' ' + alignmentSettings( align, '' ) + ' ' + displaySettings( hide, 'inline-flex' ) />
<span<@commonAttrs id=id class=class classExtra=classExtra params=params attrs=attrs />>
	<#nested>
</span>
</#macro>