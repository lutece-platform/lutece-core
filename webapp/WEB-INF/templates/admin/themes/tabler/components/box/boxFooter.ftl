<#--
Macro: boxFooter
Description: Generates an HTML element for a box footer with an optional class and alignment.
Parameters:
- class (string, optional): the CSS class of the box footer element.
- align (string, optional): the horizontal alignment of the box footer element.
- id (string, optional): the ID of the box footer element. If not provided, a default ID will be generated.
- params (string, optional): additional HTML attributes to include in the box footer element.

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
<#macro boxFooter class='' align='' id='' params='' deprecated...>
<#local class = class?is_markup_output?then(class?markup_string, class) />
<@deprecatedWarning args=deprecated />
<#if align?has_content><#local class += ' ' + alignmentSettings(align,'') /></#if>
<div class="card-footer<#if class?has_content> ${class}</#if>"<#if id?has_content> id="${id}"</#if><#if params?has_content> ${params}</#if>>
	<#nested>
</div>
</#macro>