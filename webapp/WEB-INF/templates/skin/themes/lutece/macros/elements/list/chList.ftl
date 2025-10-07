<#-- Macro: chList

Description: affiche une liste.

Parameters:
@param - id - string - optional - identifiant unique de la liste
@param - class - string - optional - classe(s) css de la liste
@param - type - string - required - type de la liste, par défaut non-ordonnée avec la valeur 'u', peut être ordonnée avec la valeur 'o'.
@param - params - string - optional - permet d'ajouter des paramètres HTML à la liste
-->
<#macro chList type='u' id='' class='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<${type}l<#if class != ''> class="${class}"</#if><#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if> >
    <#nested>
</${type}l>
</#macro>