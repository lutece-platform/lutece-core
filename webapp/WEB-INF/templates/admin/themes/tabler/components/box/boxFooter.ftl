<#--
Macro: boxFooter
Description: Generates an HTML element for a box footer with an optional class and alignment.
Parameters:
- class (string, optional): the CSS class of the box footer element.
- align (string, optional): the horizontal alignment of the box footer element.
- id (string, optional): the ID of the box footer element. If not provided, a default ID will be generated.
- params (string, optional): additional HTML attributes to include in the box footer element.
- attrs (hash, optional): additional HTML attributes as name/value pairs, e.g. {'aria-hidden': 'true'}. Prefer it to params: the macro writes the attribute and its value is escaped like any value of the page.

Snippet:

    Box footer with action buttons:

    <@boxFooter>
        <@aButton href='#' title='Save' color='primary' />
        <@aButton href='#' title='Cancel' color='secondary' />
    </@boxFooter>

    Box footer with right alignment:

    <@boxFooter align='right' class='py-3'>
        <@aButton href='#' title='Submit' color='success' buttonIcon='check' />
    </@boxFooter>

-->
<#macro boxFooter class='' align='' id='' params='' attrs={} deprecated...>
<@deprecatedWarning args=deprecated />
<div<@commonAttrs id=id classBase='card-footer' class=class classExtra=alignmentSettings( align, '' ) params=params attrs=attrs />>
	<#nested>
</div>
</#macro>