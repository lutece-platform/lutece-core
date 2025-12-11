<#--
Macro: dropdownList

Description: Generates a dropdown menu list element with a specified ID and additional parameters.

Parameters:
- id (string, optional): the ID for the dropdown menu list.
- params (string, optional): additional parameters to add to the HTML code.
-->
<#macro dropdownList id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<div class="dropdown-menu"<#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>>
<#nested>
</div>
</#macro>