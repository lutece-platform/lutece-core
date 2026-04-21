<#--
Macro: cSetPageTitle

Description: Generates a JavaScript snippet that sets or appends to the browser page title, optionally reading content from a DOM element.

Parameters:
- title (string, required): Title text to set or append.
- srcElement (string, optional): CSS selector for a DOM element whose text/value will be appended to the title. Default: ''.
- init (boolean, optional): Whether to replace the entire title (true) or append to it (false). Default: false.
- type (string, optional): Type of source element content to read ('text' or 'input'). Default: 'text'.

Snippet:

    Initialize page title:

    <@cSetPageTitle title='My Service - Paris.fr' init=true />

    Append to existing title:

    <@cSetPageTitle title='Search results' />

    Append title from a DOM element:

    <@cSetPageTitle title='' srcElement='.page-heading' type='text' />

-->  
<#macro cSetPageTitle title srcElement='' init=false type='text' deprecated...>
<@deprecatedWarning args=deprecated />
<#if title?? && title!=''>
const pageTitle = document.querySelector('title');
<#if init>
pageTitle.textContent = '${title}';
<#else>
const mainTitleText = pageTitle.textContent;
const complementaryTitleText = <#if srcElement=''>'${title!}'<#else><#if type='text'>document.querySelector('${srcElement}').textContent<#else>document.querySelector('${srcElement}').value</#if></#if>;
pageTitle.textContent = `<#noparse>${mainTitleText} ${complementaryTitleText}</#noparse>`;
</#if>
<#nested>
</#if>
</#macro>