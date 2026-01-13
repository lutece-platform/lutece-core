<#-- Macro: iconStack

Description: Generates a stack of icons.

Parameters:
- class (string, optional): the CSS class for the icon stack.
- id (string, optional): the ID attribute for the icon stack.
- params (string, optional): additional parameters to add to the icon stack.
-->
<#macro iconStack class='fa-2x' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<span class="fa-stack ${class}"<#if id!=''> ${id}</#if><#if params!=''> ${params}</#if>>
<#nested>
</span>
</#macro>