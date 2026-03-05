<#--
Macro: cArticle

Description: Generates an HTML article element with an optional title and content section. Useful for structuring portlet content on skin pages.

Parameters:
- title (string, optional): Title displayed in the article header. Default: '#i18n{portal.theme.defaultArticleTitle}'.
- titleLevel (number, required): Heading level for the article title (1-6). Default: 2.
- titleClass (string, optional): CSS class(es) applied to the article title. Default: ''.
- class (string, optional): CSS class(es) applied to the article element. Default: 'portlet'.
- id (string, optional): Unique identifier for the article element. Default: ''.
- params (string, optional): Additional HTML attributes for the article element. Default: ''.

Snippet:

    Basic usage:

    <@cArticle title='Latest News' titleLevel=2>
        <p>Here is the content of the article displayed on the page.</p>
    </@cArticle>

    With custom classes:

    <@cArticle title='Featured Event' titleLevel=3 titleClass='text-primary' class='portlet featured'>
        <p>Details about the featured event.</p>
    </@cArticle>

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