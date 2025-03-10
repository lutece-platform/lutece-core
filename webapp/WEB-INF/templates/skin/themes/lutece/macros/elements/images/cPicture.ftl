<#-- Macro: cPicture

Description: affiche une picture.

Parameters:
@param - id - string - optional - identifiant unique de la picture
@param - class - string - optional - classe(s) css de la picture
@param - params - string - optional - permet d'ajouter des paramètres HTML à la picture
-->
<#macro cPicture id='' class='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<picture<#if class!=''> class="${class!}"</#if><#if id!=''> id="${id!}"</#if><#if params!=''> ${params!}</#if>>
<#nested/>
</picture>
</#macro>