<#--
Macro: cAlert

Description: Generates an alert banner with icon, optional title, and dismissible close button.

Parameters:
- id (string, optional): The unique identifier for the alert. Default: ''.
- title (string, optional): The title text for the alert. Default: ''.
- type (string, optional): The alert type controlling color and icon ('primary', 'warning', 'danger', 'success'). Default: 'primary'.
- class (string, optional): Additional CSS class(es) for the alert container. Default: ''.
- classText (string, optional): CSS class(es) applied to the alert text. Default: ''.
- dismissible (boolean, optional): If true, a close button is displayed. Default: false.
- params (string, optional): Additional HTML attributes for the alert. Default: ''.

Snippet:

    Basic info alert:

    <@cAlert type='primary' title='Information'>
        <p>Your request has been submitted successfully.</p>
    </@cAlert>

    Dismissible danger alert:

    <@cAlert type='danger' title='Error' dismissible=true>
        <p>An error occurred while processing your form.</p>
    </@cAlert>

-->
<#macro cAlert id='' title='' type='primary' class='' classText='' dismissible=false params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#local type=type! /> 
<#local hasClass=false /> 
<#local allClass=class?split(' ')! /> 
<#local typeClass=allClass[0]! /> 
<#local types=["warning","primary","danger","success"]>
<#if typeClass !='' && types?seq_contains(typeClass)><#local type=typeClass /></#if>
<#local alertIconName='info-circle' />
<#local alertIconTitle='#i18n{portal.theme.labelInfo}' />
<#local ariaRole='status' />
<#if type?starts_with('danger')>
<#local alertIconTitle='#i18n{portal.theme.labelError}' />
<#local alertIconName='alert-ban' /> 
<#local ariaRole='alert' />
<#elseif type?starts_with('warning')>
<#local alertIconTitle='#i18n{portal.theme.labelWarning}' />
<#local alertIconName='alert-triangle' />
<#local ariaRole='alert' />
<#elseif type?starts_with('success')>
<#local alertIconTitle='#i18n{portal.theme.labelSuccess}' />
<#local alertIconName='alert-check' />
<#local ariaRole='status' />
</#if>
<#local alertClass>alert alert-outline alert-${type} d-flex align-items-center<#if dismissible> alert-dismissible</#if><#if allClass?size gt 0><#list allClass as x> ${x}</#list></#if></#local>
<@cBlock class=alertClass! params='role="${ariaRole!}" ${params!}' id=id!>
    <@cIcon name=alertIconName! class='flex-shrink-0 me-2' params='aria-label="${alertIconTitle!}"' />
    <#if title !=''><@cText class="alert-title">${title!}</@cText></#if>
    <#nested />
    <#if dismissible><@cBtn type='button' label='' class='close py-xs px-xs' params='data-bs-dismiss="alert" aria-label="#i18n{portal.theme.labelClose}"' /></#if>
</@cBlock>
</#macro>