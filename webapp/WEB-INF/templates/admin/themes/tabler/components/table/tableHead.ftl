<#-- 
Macro: tableHead

Description: Generates an HTML <thead> element with an optional ID, class, and additional parameters.

Parameters:
- id (string, optional): the ID of the <thead> element.
- class (string, optional): the class of the <thead> element.
- params (string, optional): additional parameters to add to the HTML code.

-->
<#macro tableHead id='' class='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<thead<#if id!=''> id="${id}"</#if><#if class!=''> class="${class?trim}"</#if><#if params!=''> ${params}</#if>>
<#nested>
</thead>
</#macro>