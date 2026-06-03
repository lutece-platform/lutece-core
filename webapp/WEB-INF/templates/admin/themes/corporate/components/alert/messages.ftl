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
<#macro messages errors=[] infos=[] warnings=[] errors_class="alert alert-danger" infos_class="alert alert-info" warnings_class="alert alert-warning">
<#assign errorCount = errors?size>
<#assign warningCount = warnings?size>
<#assign infoCount = infos?size>
<#assign totalCount = errorCount + warningCount + infoCount>

<#assign alertColor = "primary">
<#assign titleKey = "portal.util.message.titleWarning">
<#if totalCount gt 0>
    <#if errorCount gt 0>
        <#assign alertColor = "danger">
		<#assign titleKey = "portal.util.message.titleError">
    <#elseif warningCount gt 0>
        <#assign alertColor = "warning">
    <#else>
        <#assign alertColor = "info">
    </#if>
</#if>
<#if totalCount gt 0>
    <@alert color=alertColor iconTitle='exclamation-circle' dismissible=true id='messages_all_div'>
        <#if errorCount gt 0>
            <#list errors as error>
                <#if error.message??>
                    <h3 class="ms-2 text-danger mt-0 mb-0">${error.message!' #i18n{portal.util.message.titleError} '}</h3>
                </#if>
            </#list>
        </#if>

        <#if warningCount gt 0>
            <#list warnings as warning>
                <#if warning.message??>
                   <h3 class="ms-2 text-warning mt-0 mb-0">${warning.message!' #i18n{portal.util.message.titleWarning} '}</h3>
                </#if>
            </#list>
        </#if>

        <#if infoCount gt 0>
            <#list infos as info>
                <#if info.message??>
                    <h3 class="ms-2 text-info mt-0 mb-0">${info.message!' #i18n{portal.util.labelWarning} '}</h3>
                </#if>
            </#list>
        </#if>
    </@alert>
</#if>
</#macro>