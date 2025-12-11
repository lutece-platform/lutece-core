<#--
Macro: dropdownMenu

Description: Generates a dropdown menu element with a specified class, ID, and additional parameters.

Parameters:
- class (string, optional): additional classes to add to the dropdown menu.
- id (string, optional): the ID for the dropdown menu.
- params (string, optional): additional parameters to add to the HTML code.
-->
<#macro dropdownMenu class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<ul class="dropdown-menu ${class}"<#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>>
<#nested>
</ul>
</#macro>