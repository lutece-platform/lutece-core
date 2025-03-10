<#-- Macro: cThead

Description: définit le header du tableau.

Parameters:
@param - id - string - optional - l'ID du header du tableau
@param - class - string - optional - permet d'ajouter une classe CSS au header du tableau
@param - params - string - optional - permet d'ajouter des parametres HTML au header du tableau
-->  
<#macro cThead class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<thead <#if class!=''>thead-${class!}</#if><#if id !=''> id="${id!}"</#if><#if params!=''> ${params!}</#if> >
<#nested>
</thead>
</#macro>