<#--
Macro: boxBody
Description: Generates an HTML element for a box body with an optional class, alignment, and collapse state.
Parameters:
- class (string, optional): the CSS class of the box body element.
- collapsed (boolean, optional): whether the box body should be initially collapsed.
- align (string, optional): the horizontal alignment of the box body element.
- id (string, optional): the ID of the box body element. If not provided, a default ID will be generated.
- params (string, optional): additional HTML attributes to include in the box body element.
-->
<#macro boxBody class='' collapsed=false align='' id='' params=''>
<#if collapsed><#local class += ' ' + 'collapse' /></#if>
<#if align!=''><#local class += ' ' + alignmentSettings(align,'') /></#if>
<div class="card-body bg-white<#if class!=''> ${class}</#if>" <#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>>
	<#nested>
</div>
</#macro>