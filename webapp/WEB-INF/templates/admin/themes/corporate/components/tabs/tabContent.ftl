<#-- 
Macro: tabContent

Description: Generates a tab content container with an optional ID and parameters.

Parameters:
- id (string, optional): the ID of the tab content container.
- params (string, optional): additional parameters to add to the HTML code.

-->
<#macro tabContent class='' id='' params='' boxed=true deprecated...>
<@deprecatedWarning args=deprecated />
<div class="tab-content card<#if boxed> p-4 rounded-0</#if><#if class!=''> ${class}</#if>"<#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>>
<#nested>
</div>
</#macro>