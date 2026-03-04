<#-- Macro: cAlert

Description: affiche une bannière d'alerte.

Parameters:
@param - id - string - optional - l'ID de l'alert
@param - title - string - optional - le titre de l'alert (par défaut : '#i18n{portal.theme.labelWarning}')
@param - type - string - optional - le type de l'alert (par défaut : 'primary')
@param - class - string - optional - permet d'ajouter une classe CSS prefixée 'alert-' à l'alert (par défaut : 'primary')
@param - classText - string - optional - permet d'ajouter une classe CSS au texte de l'alert (par défaut : 'primary')
@param - dismissible - boolean - optional - permet d'activer la fermeture de l'alert (par défaut: false)
@param - params - string - optional - permet d'ajouter des parametres HTML à l'alert
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