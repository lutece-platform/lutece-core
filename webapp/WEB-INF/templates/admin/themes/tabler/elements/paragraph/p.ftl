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
-->
<#macro p id='' params='' class='' hide=[] collapsed=false align='' deprecated...>
<@deprecatedWarning args=deprecated />
<#if collapsed><#local class += ' ' + 'collapse' /></#if>
<#local class += ' ' + displaySettings(hide,'block') + ' ' + alignmentSettings(align,'') />
<p<#if class?trim!=''> class="${class?trim}"</#if><#if params!=''> ${params}</#if><#if id!=''> id="${id}"</#if>>
	<#nested>
</p>
</#macro>