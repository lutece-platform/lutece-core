<#-- Macro: cFigure

Description: affiche une figure.

Parameters:
@param - id - string - optional - identifiant unique de la figure
@param - class - string - optional - classe(s) css de la figure
@param - caption - string - optional - description de la figure
@param - params - string - optional - permet d'ajouter des paramètres HTML à la figure
-->
<#macro cFigure caption='' id='' class='figure' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<figure <#if class!=''>class="${class!}"</#if><#if id!=''> id="${id!}"</#if><#if params!=''> ${params!}</#if>>
<#nested/>
<#if caption!=''><@cFigCaption class='figure-caption'>${caption!}</@cFigCaption></#if>
</figure>
</#macro>