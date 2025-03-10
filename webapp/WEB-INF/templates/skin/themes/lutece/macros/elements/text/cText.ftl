<#-- Macro: cText

Description: affiche un texte.

Parameters:
@param - id - string - optional - identifiant unique du texte
@param - class - string - optional - classe(s) css du texte
@param - type - string - required - type de texte, par défaut un paragraphe
@param - params - string - optional - permet d'ajouter des paramètres HTML au texte
-->
<#macro cText type='p' id='' class='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<${type}<#if class!=''> class="${class!}"</#if><#if id!=''> id="${id!}"</#if><#if params!=''> ${params!}</#if>>
<#nested/>
</${type}>
</#macro>