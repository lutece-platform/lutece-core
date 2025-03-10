<#-- Macro: cAlert

Description: affiche une bannière d'alerte.

Parameters:
@param - id - string - optional - l'ID de l'alert
@param - title - string - optional - le titre de l'alert (par défaut : '#i18n{theme.labelWarning}')
@param - class - string - optional - permet d'ajouter une classe CSS prefixée 'alert-' à l'alert (par défaut : 'primary')
@param - classText - string - optional - permet d'ajouter une classe CSS au texte de l'alert (par défaut : 'primary')
@param - dismissible - boolean - optional - permet d'activer la fermeture de l'alert (par défaut: true)
@param - params - string - optional - permet d'ajouter des parametres HTML à l'alert
-->
<#macro cAlert id='' title='' class='info' classText='' dismissible=false params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#local type='#i18n{theme.labelInfo}' />
<#local ariaRole='status' />
<#if class?starts_with('danger')>
<#local type='#i18n{theme.labelError}' />
<#local ariaRole='alert' />
<#elseif class?starts_with('warning')>
<#local type='#i18n{theme.labelWarning}' />
<#local ariaRole='alert' />
<#elseif class?starts_with('success')>
<#local type='#i18n{theme.labelSuccess}' />
<#local ariaRole='status' />
</#if>
<#local alertClass>alert ${class}<#if dismissible> dismissible fade show</#if></#local>
<@cBlock class=alertClass! params='role="${ariaRole!}" ${params!}' id=id!>
   <@cText class='${classText!}'><#if type !=''>${type!}</#if> ${title} <#nested /></@cText>
   <#if dismissible>
        <@cBlock class="ms-auto">
            <@cBtn type='button' label='' class='btn-close' params='data-bs-dismiss="alert" aria-label="#i18n{theme.labelClose}"' />
        </@cBlock>
        </#if>
</@cBlock>
</#macro>