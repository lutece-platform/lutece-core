<#-- Macro: cArticle

Description: affiche un article.

Parameters:
@param - id - string - optional - identifiant unique de la section
@param - class - string - optional - classe(s) css de la section
@param - title - string - optional - titre de l'article
@param - titleLevel - number - <b>required</b> - taille du titre de l'article
@param - titleClass - string - optional - classe(s) css sur le titre de l'article
@param - params - string - optional - permet d'ajouter des paramètres HTML à l'article
-->
<#macro cArticle title='#i18n{portal.theme.defaultArticleTitle}' titleLevel=2 titleClass='' class='portlet' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<article class="<#if class!=''> ${class}</#if>"<#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if> >
<#if title!=''><@cTitle level=titleLevel class=titleClass>${title}</@cTitle></#if> 
<@cSection type='div' class='article-content'>
<#nested>
</@cSection>
</article>
</#macro>