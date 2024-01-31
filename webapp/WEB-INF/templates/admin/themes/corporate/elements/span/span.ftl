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
-->
<#macro span id='' class='' hide=[] collapsed=false align='' params=''>
<#if collapsed><#local class += ' ' + 'collapse' /></#if>
<#local class += ' ' + alignmentSettings(align,'') + ' ' + displaySettings(hide,'inline-flex') />
<span<#if class?trim!=''> class="${class?trim}"</#if><#if params!=''> ${params}</#if><#if id!=''> id="${id}"</#if>>
	<#nested>
</span>
</#macro>