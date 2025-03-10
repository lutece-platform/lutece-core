<#-- Macro: cHeader

Description: affiche un header.

Parameters:
@param - id - string - optional - identifiant unique du header
@param - class - string - optional - classe(s) css du header
@param - title - string - optional - titre compris dans le header
@param - titleLevel - number - optional - niveau de titre (par défaut: 2)
@param - titleUrl - string - optional - url du titre, qui sera sous forme de lien si ce paramètre a une valeur
@param - titleUrlClass - string - optional - classe(s) css sur le titre sous forme de lien
@param - params - string - optional - permet d'ajouter des paramètres HTML au header
-->
<#macro cHeader title='' titleClass='' titleUrl='' titleUrlClass='' titleLevel=2 class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<header <#if class!=''> class="${class!}"</#if><#if id!=''> id="${id!}"</#if><#if params!=''> ${params!}</#if> >
    <#if title !=''><@cTitle level=titleLevel class=titleClass ><#if titleUrl !=''><a href="${titleUrl!}"<#if titleUrlClass !=''> class="${titleUrlClass!}"</#if> title="${title!}"></#if>${title!}<#if titleUrl !=''></a></#if></@cTitle></#if>
    <#nested>
</header>
</#macro>