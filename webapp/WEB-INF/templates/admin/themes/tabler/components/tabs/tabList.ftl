<#-- 
Macro: tabList

Description: Generates an HTML <ul> element containing tabs, with an optional ID, style, and various other features.

Parameters:
- style (string, optional): the style of the tab list.
- vertical (boolean, optional): whether or not the tab list is vertical.
- id (string, optional): the ID of the <ul> element containing the tab list.
- params (string, optional): additional parameters to add to the HTML code.
- color (string, optional): the color of the tab list.

-->
<#macro tabList style='tabs' vertical=false id='' class='' params='' color='' deprecated...>
<@deprecatedWarning args=deprecated />
<#if propagateTabStyle?? ><#local style = propagateTabStyle /></#if>
<ul class="nav nav-${style}<#if vertical> flex-column mb-3</#if><#if class!=''> ${class}</#if>" data-bs-toggle="tabs"<#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if> role="tablist">
<#nested>
</ul>
</#macro>