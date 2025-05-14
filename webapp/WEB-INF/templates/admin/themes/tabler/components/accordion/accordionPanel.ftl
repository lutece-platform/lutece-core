<#--
Macro: accordionPanel

Description: Generates an HTML panel element for an accordion-style UI element with expandable and collapsible content.

Parameters:
- color (string, optional): the background color of the panel element.
- collapsed (boolean, optional): whether the panel should be collapsed by default.
- childId (string, required): the ID of the child element.
- id (string, optional): the ID of the panel element. If not provided, a default ID will be generated.
- params (string, optional): additional HTML attributes to include in the panel element.
-->
<#macro accordionPanel color='' collapsed=true childId='' id='' params=''>
<div class="accordion-item px-3<#if color!=''> bg-${color}</#if>"<#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>>
<#if collapsed>
<#assign aClass = 'collapsed'>
<#assign expanded = 'false'>
<#assign childClass = 'collapse'>
<#else>
<#assign aClass = ''>
<#assign expanded = 'true'>
<#assign childClass = 'collapse show'>
</#if>
<#assign childId = childId />
<#nested>
</div>
</#macro>