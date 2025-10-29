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
<div class="accordion shadow" id="${id}"<#if params!=''> ${params}</#if>>
<#assign parentId = id>
<#nested>
</div>
</#macro>
<#--
Macro: accordion

Description: Generates an HTML accordion styled UI element with expandable and collapsible content.

Parameters:
- id (string, optional): the ID of the container element. If not provided, a default ID will be generated.
- icon (string, optional): the icon to display in the header.
- collapsed (boolean, optional): whether the accordion should be collapsed by default.
- params (string, optional): additional HTML attributes to include in the container element.
-->
<#macro accordion id title  icon='' collapsed=false params='' deprecated...>
<@deprecatedWarning args=deprecated />
<@accordionContainer id=id>
   <@accordionPanel collapsed=collapsed childId='${id}Child'>
      <@accordionHeader title=title headerIcon=icon />
      <@accordionBody>
      <#nested>
      </@accordionBody>
   </@accordionPanel>
</@accordionContainer>
</#macro>   