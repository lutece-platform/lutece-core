<#--
Macro: boxBody
Description: Generates an HTML element for a box body with an optional class, alignment, and collapse state.
Parameters:
- class (string, optional): the CSS class of the box body element.
- collapsed (boolean, optional): whether the box body should be initially collapsed.
- align (string, optional): the horizontal alignment of the box body element.
- id (string, optional): the ID of the box body element. If not provided, a default ID will be generated.
- params (string, optional): additional HTML attributes to include in the box body element.

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
<#macro boxBody class='' collapsed=false align='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#if collapsed><#local class += ' ' + 'collapse' /></#if>
<#if align!=''><#local class += ' ' + alignmentSettings(align,'') /></#if>
<div class="card-body<#if class!=''> ${class}</#if>" <#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>>
	<#nested>
</div>
</#macro>