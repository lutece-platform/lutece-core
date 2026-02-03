<#-- Macro: btnToolbar

Description: Generates a button toolbar container with customizable attributes.

Parameters:
- id (string, optional): the ID attribute of the container.
- class (string, optional): additional CSS classes to add to the container.
- align (string, optional): the alignment of the container's content.
- ariaLabel (string, optional): the ARIA label of the container.
- params (string, optional): additional parameters to add to the container.
-->
<#macro btnToolbar id='' class='' vertical=false align='' ariaLabel='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#if align!=''><#local class += ' ' + alignmentSettings(align,'') /></#if>
<div class="btn-group<#if vertical>-vertical</#if><#if class!=''> ${class?trim}</#if>" role="toolbar"<#if ariaLabel!=''> aria-label="${ariaLabel}"</#if><#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>>
<#nested>
</div>
</#macro>