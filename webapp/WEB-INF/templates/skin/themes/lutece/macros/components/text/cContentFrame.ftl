<#-- Macro: cContentFrame

Description: affiche une mise en avant de texte.

Parameters:
@param - id - string - optional - identifiant unique de la mise en avant
@param - class - string - optional - classe(s) css de la mise en avant
@param - title - string - required - titre de la mise en avant
@param - titleLevel - number - optional - niveau de titre (par défaut: 2)
@param - params - string - optional - permet d'ajouter des paramètres HTML à la mise en avant
-->
<#macro cContentFrame title class='content-frame-primary' titleLevel=2 titleClass='' contentClass='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<div class="content-frame ${class}" <#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if> >
<#if title?? && title !=''><@cTitle level=titleLevel class='h4 content-frame-title ${titleClass}'>${title}</@cTitle></#if>
<div class="content-frame-content<#if contentClass!=''> ${contentClass}</#if>"><#nested></div>
</div>
</#macro>