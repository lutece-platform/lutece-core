<#-- 
Macro: tableBody

Description: Generates an HTML <tbody> element with an optional ID, class, and additional parameters.

Parameters:
- id (string, optional): the ID of the <tbody> element.
- class (string, optional): the class of the <tbody> element.
- params (string, optional): additional parameters to add to the HTML code.

-->
<#macro tableBody id='' class='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<tbody<#if id!=''> id="${id}"</#if><#if class!=''> class="${class?trim}"</#if><#if params!=''> ${params}</#if>>
<#nested>
</tbody>
</#macro>