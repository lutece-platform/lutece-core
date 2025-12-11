<#--
Macro: pageRow
Description: Generates a row element.
Parameters:
- id (string, optional): the ID of the container element.
- width (string, optional): the width of the container element;
- class (string, optional): the CSS class of the container element.
-->
<#macro pageRow id='' width='' class='' deprecated...>
<@deprecatedWarning args=deprecated />
<div class="row<#if class !=''> ${class}</#if>" <#if id !=''> id="${id}"</#if>>
	<#nested>
</div>
</#macro>