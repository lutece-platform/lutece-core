<#-- Macro: cLabel

Description: permet de définir le label d'un champs de formulaire.

Parameters:

@param - id - string - optional - l'ID du label
@param - class - string - optional - ajoute une classe CSS au label
@param - label - string - required - définit le libellé du label
@param - for - string - optional - permet de définir le lien entre le label et le champ, à répéter dans la macro @cInput
@param - showLabel - boolean - optional - permet d'afficher le label (par défaut: true)
@param - required - boolean - optional - permet d'indiquer si le champs est obligatoire, ajoute le libellé '(facultatif)' si false (par défaut: false)
@param - params - string - optional - permet d'ajouter des parametres HTML au label
-->
<#macro cLabel label class='' id='' params='' for='' showLabel=true required=false deprecated...>
<@deprecatedWarning args=deprecated />
<#local isrequired=required />
<#if propagateRequired?? && propagateRequired ><#local isrequired=true /></#if>
<label class="<#if class!=''>${class}</#if><#if !showLabel> visually-hidden</#if>"<#if for!=''> for="${for!}"</#if><#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>>
${label!}<#if isrequired>&nbsp;<span class="main-danger-color" tabindex="0" title="#i18n{portal.theme.labelMandatory}">*</span></#if>
<#nested>
</label>
</#macro>