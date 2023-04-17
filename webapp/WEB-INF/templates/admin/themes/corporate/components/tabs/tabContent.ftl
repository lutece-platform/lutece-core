<#-- 
Macro: tabContent

Description: Generates a tab content container with an optional ID and parameters.

Parameters:
- id (string, optional): the ID of the tab content container.
- params (string, optional): additional parameters to add to the HTML code.

-->
<#macro tabContent id='' params='' boxed=true>
<div class="tab-content card p-4 rounded-0"<#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>>
<#nested>
</div>
</#macro>