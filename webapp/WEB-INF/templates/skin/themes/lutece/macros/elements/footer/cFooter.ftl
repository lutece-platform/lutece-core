<#-- Macro: cFooter

Description: affiche un footer.

Parameters:
@param - id - string - optional - identifiant unique du footer
@param - class - string - optional - classe(s) css du footer
@param - title - string - optional - titre compris dans le footer
@param - titleLevel - number - optional - niveau de titre (par défaut: 2)
@param - params - string - optional - permet d'ajouter des paramètres HTML au footer
-->
<#macro cFooter title='' titleLevel=2 class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<footer<#if class!=''> class="${class}"</#if><#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if> >
    <#if title !=''><@cTitle level=titleLevel>${title}</@cTitle></#if>
    <#nested>
</footer>
</#macro>