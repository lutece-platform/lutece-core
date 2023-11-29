<#-- 
Macro: tabs

Description: Generates an HTML <div> element containing tab panels, with an optional ID, style, color, and various other features.

Parameters:
- color (string, optional): the color of the tabs.
- style (string, optional): the style of the tabs.
- class (string, optional): additional classes to add to the HTML code.
- id (string, optional): the ID of the <div> element containing the tabs.
- hide (list, optional): a list of breakpoint-specific display settings to hide the element at certain screen sizes.
- collapsed (boolean, optional): whether or not the tabs are collapsed by default.
- params (string, optional): additional parameters to add to the HTML code.

-->
<#macro tabs color='' style='tabs' class='' id='' hide=[] collapsed=false params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#if collapsed><#local class += ' ' + 'collapse' /></#if>
<#if hide??><#local class += ' ' + displaySettings(hide,'block') /></#if>
<#assign propagateTabStyle = style />
<div class="<#if color!=''> ${color}</#if><#if class?trim!=''> ${class?trim}</#if>" <#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>>
<#nested>
</div>
</#macro>