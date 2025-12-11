<#--
Macro: div

Description: generate a div element with the specified attributes.

Parameters:
- id (string): ID attribute of the div element (default: '')
- class (string): class attribute of the div element (default: '')
- hide (list): list of display settings to hide the element on specific screen sizes (default: [])
- collapsed (boolean): boolean to determine if the element is initially collapsed (default: false)
- align (string): alignment of the content within the div element (default: '')
- params (string): additional parameters to include in the div element (default: '')

-->
<#macro div id='' class='' hide=[] collapsed=false align='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#if collapsed><#local class += ' ' + 'collapse' /></#if>
<#if align!=''><#local class += ' ' + alignmentSettings(align,'') /></#if>
<#if hide??><#local class += ' ' + displaySettings(hide,'block') /></#if>
<div<#if class?trim!=''> class="${class?trim}"</#if><#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>>
	<#nested>
</div>
</#macro>