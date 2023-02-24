<#--
Macro: accordionContainer

Description: Generates an HTML container element for a set of accordion-style UI elements with expandable and collapsible content.

Parameters:
- id (string, optional): the ID of the container element. If not provided, a default ID will be generated.
- params (string, optional): additional HTML attributes to include in the container element.
-->
<#macro accordionContainer id='' params=''>
<#if id = '' >
   <#if accordionContainerId?? == false><#assign accordionContainerId = 1 ><#else><#assign accordionContainerId = accordionContainerId + 1 ></#if>
   <#local id = 'accCont_'+ accordionContainerId >
</#if>
<div class="accordion" id="${id}"<#if params!=''> ${params}</#if>>
<#assign parentId = id>
<#nested>
</div>
</#macro>