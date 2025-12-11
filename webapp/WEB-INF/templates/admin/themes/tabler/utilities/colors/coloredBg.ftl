<#-- Macro: coloredBg

Description: Wraps content in an element with a colored background.

Parameters:
- color (string): the color of the background, using a Bootstrap color class (e.g. "primary", "secondary", "success", "info", "warning", "danger", "light", or "dark").
- type (string, optional): the type of the element to use (e.g. "div", "p", "span", etc.).
- id (string, optional): the ID of the element.
- params (string, optional): additional parameters to add to the element.
-->
<#macro coloredBg color='' type='p' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<${type} class="bg-${color}"<#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>>
	<#nested>
</${type}>
</#macro>