<#-- Macro: cPictureSrc

Description: affiche la source d'une picture.

Parameters:
@param - id - string - optional - identifiant unique de la source
@param - class - string - optional - classe(s) css de la source
@param - srcset - string - required - source de l'image
@param - media - string - optional - Media Query
@param - type - string - optional - type de ressource
@param - params - string - optional - permet d'ajouter des paramètres HTML à la source
-->
<#macro cPictureSrc srcset media='' type='' class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<source srcset="${srcset}" <#if media!=''>media="${media!}"</#if><#if type!=''> type="${type!}"</#if><#if class!=''> class="${class!}"</#if><#if id!=''> id="${id!}"</#if><#if params!=''> ${params!}</#if>>
</#macro>