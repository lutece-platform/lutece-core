<#--
Macro: boxFooter
Description: Generates an HTML element for a box footer with an optional class and alignment.
Parameters:
- class (string, optional): the CSS class of the box footer element.
- align (string, optional): the horizontal alignment of the box footer element.
- id (string, optional): the ID of the box footer element. If not provided, a default ID will be generated.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

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
<#macro boxFooter class='' align='' id='' params={} deprecated...>
<@deprecatedWarning args=deprecated />
<div<@commonAttrs id=id classBase='card-footer' class=class classExtra=alignmentSettings( align, '' ) params=params />>
	<#nested>
</div>
</#macro>