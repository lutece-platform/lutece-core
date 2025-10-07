<#-- Macro: chItem

Description: affiche un élément liste li.

Parameters:
@param - id - string - optional - identifiant unique de l'élément de liste
@param - class - string - optional - classe(s) css de l'élément de liste
@param - params - string - optional - permet d'ajouter des paramètres HTML à l'élément de liste
-->
<#macro chItem id='' class='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<li<#if class != ''> class="${class}"</#if><#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>>
<#nested>
</li>
</#macro>