<#--
Macro: messages
Description: Generates HTML alert elements for displaying error, info, and warning messages.
Parameters:
- errors (list, optional): a list of error messages to display.
- infos (list, optional): a list of info messages to display.
- warnings (list, optional): a list of warning messages to display.
- errors_class (string, optional): the CSS class of the alert element for error messages.
- infos_class (string, optional): the CSS class of the alert element for info messages.
- warnings_class (string, optional): the CSS class of the alert element for warning messages.
-->
<#macro box color='' id='' style='' class='' title='' params='' collapsed=false>
<div class="card mb-3<#if color!=''> card-${color}<#else> card-transparent</#if> card-outline<#if style!=''> text-${style}</#if><#if class!=''> ${class}</#if><#if collapsed> collapse </#if>"<#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>>
<#if title =''>
<#nested>
<#else>
<@boxHeader title=title params=params skipHeader=true />
<@boxBody>
<#nested>
</@boxBody>
</#if>
</div>
</#macro>