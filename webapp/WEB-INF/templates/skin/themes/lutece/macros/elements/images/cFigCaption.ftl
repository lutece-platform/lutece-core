<#-- Macro: cFigCaption

Description: affiche un figCaption.

Parameters:
@param - id - string - optional - identifiant unique du figCaption
@param - class - string - optional - classe(s) css du figCaption
@param - params - string - optional - permet d'ajouter des paramètres HTML au figCaption
-->
<#macro cFigCaption id='' class='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<figcaption <#if class!=''>class="${class!}"</#if><#if id!=''> id="${id!}"</#if><#if params!=''> ${params!}</#if>>
<#nested/>
</figcaption>
</#macro>