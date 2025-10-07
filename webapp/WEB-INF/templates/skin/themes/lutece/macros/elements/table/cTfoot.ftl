<#-- Macro: cTfoot

Description: définit le footer du tableau.

Parameters:
@param - id - string - optional - l'ID du footer du tableau
@param - class - string - optional - permet d'ajouter une classe CSS au footer du tableau
@param - params - string - optional - permet d'ajouter des parametres HTML au footer du tableau
--> 
<#macro cTfoot class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<tfoot <#if class!=''>thead-${class!}</#if><#if id !=''> id="${id!}"</#if><#if params!=''> ${params!}</#if> >
<#nested>
</tfoot>
</#macro>