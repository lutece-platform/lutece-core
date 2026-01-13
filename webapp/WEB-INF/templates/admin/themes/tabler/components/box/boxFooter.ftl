<#--
Macro: boxFooter
Description: Generates an HTML element for a box footer with an optional class and alignment.
Parameters:
- class (string, optional): the CSS class of the box footer element.
- align (string, optional): the horizontal alignment of the box footer element.
- id (string, optional): the ID of the box footer element. If not provided, a default ID will be generated.
- params (string, optional): additional HTML attributes to include in the box footer element.
-->
<#macro boxFooter class='' align='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#if align!=''><#local class += ' ' + alignmentSettings(align,'') /></#if>
<div class="card-footer<#if class!=''> ${class}</#if>"<#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>>
	<#nested>
</div>
</#macro>