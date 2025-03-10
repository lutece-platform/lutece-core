<#-- Macro: cSection

Description: affiche une section.

Parameters:
@param - id - string - optional - identifiant unique de la section
@param - class - string - optional - classe(s) css de la section
@param - type - string - required - élément HTML de niveau block de structure section.
@param - params - string - optional - permet d'ajouter des paramètres HTML à la section
-->
<#macro cSection type='section' class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<${type}<#if class!=''> class="${class}"</#if><#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>>
<#nested>
</${type}>
</#macro>