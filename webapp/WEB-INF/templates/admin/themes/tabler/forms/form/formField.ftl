<#-- 
Macro: formField

Description: Generates a form row with the specified CSS class.

Parameters:
- class (string, optional): additional classes to add to the form row.
-->
<#macro formField class='' deprecated...>
<@deprecatedWarning args=deprecated />
<div class="form-row mb-3<#if class!=''> ${class}</#if>">
<#nested>
</div>
</#macro>