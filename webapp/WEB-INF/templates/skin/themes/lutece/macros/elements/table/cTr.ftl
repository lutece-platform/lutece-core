<#-- Macro: cTr

Description: définit une ligne du tableau.

Parameters:
@param - id - string - optional - l'ID de la ligne du tableau
@param - class - string - optional - permet d'ajouter une classe CSS à la ligne du tableau
@param - params - string - optional - permet d'ajouter des parametres HTML à la ligne du tableau
-->
<#macro cTr class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<tr<#if class!=''> class="${class!}"</#if><#if id !=''> id="${id!}"</#if><#if params!=''> ${params!}</#if> >
<#nested>
</tr>
</#macro>