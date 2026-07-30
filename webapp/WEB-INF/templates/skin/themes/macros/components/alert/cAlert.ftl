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

Showcase:
- desc: Alerte - @cAlert
- bs: components/alerts

Samples:
    <@cAlert title='Titre de l\'alerte' />
    <@cAlert class='danger' title='Message erreur' />
    <@cAlert class='danger' title='Message d\'erreur'>Contenu de l'erreur</@cAlert>
    <@cAlert class='danger' dismissible=true title='Message d\'erreur'>Message d'erreur et bouton de fermeture</@cAlert>
    <@cAlert class='warning' title='Message attention...' />
    <@cAlert class='warning' dismissible=true title='Message attention...'>Contenu du message "warning"</@cAlert>
    <@cAlert class='success' title='Message validation...'>Confirmation...</@cAlert>
    <@cAlert class='success' dismissible=true title='Message validation...'>Confirmation...</@cAlert>
    <@cAlert type='warning' class='mt-xxl' dismissible=true title='Message validation...'>Confirmation...</@cAlert>
    <@cAlert type='warning' isHtmlTitle=true htmlTitleLevel=3 dismissible=true title='Message avec titre HTML de niveau 3'>
    <@chList>
        <@chItem>Contenu du message avec un titre HTML personnalisé</@chItem>
        <@chItem>Le titre est de niveau 3 grâce au paramètre htmlTitleLevel</@chItem>
        <@chItem>Le paramètre isHtmlTitle doit être à true pour que le titre soit interprété comme du HTML</@chItem>
    </@chList>
    </@cAlert>

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
<#macro cAlert id='' title='' isHtmlTitle=false htmlTitleLevel=3 type='primary' iconType='informative' class='' classText='' dismissible=false params='' deprecated...>
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
<#local alertIconName='ban' /> 
<#local ariaRole='alert' />
<#elseif type?starts_with('warning')>
<#local alertIconTitle='#i18n{portal.theme.labelWarning}' />
<#local alertIconName='alert-triangle ' />
<#local ariaRole='alert' />
<#elseif type?starts_with('success')>
<#local alertIconTitle='#i18n{portal.theme.labelSuccess}' />
<#local alertIconName='alert-check ' />
<#local ariaRole='status' />
</#if>
<#local alertClass>alert alert-outline alert-${type}<#if dismissible> dismissible fade show</#if><#if  allClass?size gt 0><#list allClass as x> ${x}</#list></#if></#local>
<@cBlock class=alertClass! params='role="${ariaRole!}" ${params!}' id=id!>
    <@cBlock class='alert-header'>
        <@cBlock class='alert-icon'><@cIcon name=alertIconName! type=iconType title=alertIconTitle! /></@cBlock>
        <@cBlock class='alert-text ${classText!}'><#if title !=''><#if isHtmlTitle><@cTitle class="alert-title mt-0" level=htmlTitleLevel>${title!}</@cTitle><#else><@cText class="alert-title">${title!}</@cText></#if></#if></@cBlock>
        <#if dismissible>
        <@cBlock class="alert-dismiss">
            <@cBtn type='button' label='' class='close py-xs px-xs' params='data-bs-dismiss="alert" aria-label="#i18n{portal.theme.labelClose}"' />
        </@cBlock>
        </#if>
    </@cBlock>
    <#local _nested><#nested /></#local>
    <#if _nested?? && _nested !=''><@cBlock class='alert-content'>${_nested}</@cBlock></#if>
</@cBlock>
</#macro>