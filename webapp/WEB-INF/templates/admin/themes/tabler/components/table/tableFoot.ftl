<#-- 
Macro: tableFoot

Description: Generates an HTML <tfoot> element with an optional ID, class, and additional parameters.

Parameters:
- id (string, optional): the ID of the <tfoot> element.
- class (string, optional): the class of the <tfoot> element.
- params (string, optional): additional parameters to add to the HTML code.

-->
<#macro tableFoot id='' class='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<tfoot <#if id!=''> id="${id}"</#if><#if class!=''> class="${class?trim}"</#if><#if params!=''> ${params}</#if>>
<#nested>
</tfoot>
</#macro>