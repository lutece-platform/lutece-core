<#--
Macro: pre

Description: Generates an HTML preformatted text element with a specified ID, class, display settings, alignment, and nested content block.

Parameters:
- id (string, optional): the ID for the preformatted text element.
- class (string, optional): additional classes to add to the preformatted text element.
- hide (list, optional): a list of display settings to hide the preformatted text element, e.g., ["md", "lg"].
- collapsed (boolean, optional): whether to collapse the preformatted text element.
- align (string, optional): the alignment setting for the preformatted text element, e.g., "center" or "right".
- params (string, optional): additional parameters to add to the HTML code.
-->
<#macro pre id='' class='' hide=[] collapsed=false align='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#if collapsed><#local class += ' ' + 'collapse' /></#if>
<#local class += ' ' + alignmentSettings(align,'') + ' ' + displaySettings(hide,'block') />
<pre<#if class!=''> class="${class?trim}"</#if><#if params!=''> ${params}</#if><#if id!=''> ${id}</#if>>
	<#nested>
</pre>
</#macro>