<#-- Macro: cTbody

Description: définit le body du tableau.

Parameters:
@param - id - string - optional - l'ID du body du tableau
@param - class - string - optional - permet d'ajouter une classe CSS au body du tableau
@param - params - string - optional - permet d'ajouter des parametres HTML au body du tableau
-->
<#macro cTbody class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<tbody <#if class!=''>${class!}</#if><#if id !=''> id="${id!}"</#if><#if params!=''> ${params!}</#if> >
<#nested>
</tbody>
</#macro>