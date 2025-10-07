<#-- Macro: cFieldset

Description: permet de définir le container d'un formulaire.

Parameters:

@param - legend - string - optional - permet de définir la légende du fieldset, défaut '' 
@param - legendClass - string - optional - ajoute une classe CSS à la légende du fieldset
@param - role - string - optional - permet de définir la valeur de l'attribut 'aria-label' du formulaire
@param - class - string - optional - ajoute une classe CSS au formulaire
@param - id - string - optional - l'ID du formulaire
@param - params - string - optional - permet d'ajouter des parametres HTML au formulaire
@param - for - string - optional - permet de lier un id avec le fieldset
@param - helpMsg - string - optional - permet de définir le message d'aide sur le fieldset
@param - helpPos - string - optional - par défaut "top" sinon "after" permet de définir la position le message d'aide sur le fieldset, par défaut au dessus du contenu après la légende
@param - showLabel - string - optional - permet de définir si la legend est affichée ou pas
@param - required - string - optional - permet d'afficher le signe * indiquant que le champs est obligatoire
-->
<#macro cFieldset legend='' legendClass='' role='group' class='' id='' params='' for='' helpMsg='' helpPos='top' showLabel=true required=false deprecated...>
<@deprecatedWarning args=deprecated />
<#local cId><#if id=''>id-${random()}<#else>${id}</#if></#local>
<fieldset<#if class!=''> class="${class}"</#if><#if for!=''> for="${for!}"</#if> id="fieldset-${cId}"<#if params!=''> ${params}</#if><#if role!=''> role="${role}"</#if> aria-labelledby="legend-${cId}"<#if helpMsg !=''> aria-describedby="help_${cId!}"</#if>'>
<#if legend!=''><legend <#if legendClass!='' || !showLabel>class="${legendClass!}<#if !showLabel> visually-hidden</#if>"</#if> id="legend-${cId}" <#if required> aria-required="true"</#if>>${legend!}<#if required> <span class="main-danger-color" tabindex="-1" title="#i18n{theme.labelMandatory}">*</span></#if></legend></#if>
<#if helpPos == 'top' && helpMsg !=''><@cFormHelp cId helpMsg /></#if>
<#nested>
<#if helpPos == 'after' && helpMsg !=''><@cFormHelp cId helpMsg /></#if>
</fieldset>
</#macro>