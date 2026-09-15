<#--
Macro: cHeader

Description: Generates an HTML header element with an optional title that can be rendered as a link. Useful for page or section headers on skin pages.

Parameters:
- title (string, optional): Title displayed inside the header. Default: ''.
- titleClass (string, optional): CSS class(es) applied to the title element. Default: ''.
- titleUrl (string, optional): URL for the title link; when set, the title becomes a clickable link. Default: ''.
- titleUrlClass (string, optional): CSS class(es) applied to the title link. Default: ''.
- titleLevel (number, optional): Heading level for the header title (1-6). Default: 2.
- class (string, optional): CSS class(es) applied to the header element. Default: ''.
- id (string, optional): Unique identifier for the header element. Default: ''.
- params (string, optional): Additional HTML attributes for the header element. Default: ''.

Showcase:
- desc: En-tête - @cHeader
- newFeature: false

Snippet:

    Basic usage:

    <@cHeader title='Welcome to Our Site'>
        <nav>Navigation content here</nav>
    </@cHeader>

    With a linked title:

    <@cHeader title='Home' titleUrl='/jsp/site/Portal.jsp' titleUrlClass='text-decoration-none' titleLevel=1 class='page-header'>
        <p>Site tagline or description</p>
    </@cHeader>

-->
<#macro cHeader title='' titleClass='' titleUrl='' titleUrlClass='' titleLevel=2 class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<header <#if class?has_content> class="${class!}"</#if><#if id?has_content> id="${id!}"</#if><#if params?has_content> ${params!}</#if> >
    <#if title?has_content><@cTitle level=titleLevel class=titleClass ><#if titleUrl?has_content><a href="${titleUrl!}"<#if titleUrlClass?has_content> class="${titleUrlClass!}"</#if> title="${title!}"></#if>${title!}<#if titleUrl?has_content></a></#if></@cTitle></#if>
    <#nested>
</header>
</#macro>