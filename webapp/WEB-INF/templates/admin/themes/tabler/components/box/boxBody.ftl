<#--
Macro: boxBody
Description: Generates an HTML element for a box body with an optional class, alignment, and collapse state.
Parameters:
- class (string, optional): the CSS class of the box body element.
- collapsed (boolean, optional): whether the box body should be initially collapsed.
- align (string, optional): the horizontal alignment of the box body element.
- id (string, optional): the ID of the box body element. If not provided, a default ID will be generated.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

Snippet:

    Basic box body with content:

    <@boxBody>
        <p>This is the main content area of the card.</p>
    </@boxBody>

    Box body with center alignment and custom class:

    <@boxBody align='center' class='p-4'>
        <p>Centered content with extra padding.</p>
    </@boxBody>

-->
<#macro boxBody class='' collapsed=false align='' id='' params={} deprecated...>
<@deprecatedWarning args=deprecated />
<#local classExtra = collapsed?then( 'collapse', '' ) + ' ' + alignmentSettings( align, '' ) />
<div<@commonAttrs id=id classBase='card-body' class=class classExtra=classExtra params=params />>
	<#nested>
</div>
</#macro>