<#--
Macro: cContentFrame

Description: Generates a highlighted content frame block with a title and styled wrapper for emphasizing text content on skin pages.

Parameters:
- title (string, required): Title displayed in the content frame header.
- class (string, optional): CSS classes for the frame container. Default: 'content-frame-primary'.
- titleLevel (number, optional): Heading level for the title. Default: 2.
- titleClass (string, optional): Additional CSS classes for the title element. Default: ''.
- contentClass (string, optional): Additional CSS classes for the content wrapper. Default: ''.
- id (string, optional): HTML id attribute. Default: ''.
- params (string, optional): Additional HTML attributes. Default: ''.

Snippet:

    Basic content frame:

    <@cContentFrame title='Important information'>
        <p>Please read the following instructions carefully.</p>
    </@cContentFrame>

    Content frame with custom style:

    <@cContentFrame title='Note' class='content-frame-warning' titleLevel=3 contentClass='p-3'>
        <p>This action cannot be undone.</p>
    </@cContentFrame>

-->
<#macro cContentFrame title class='content-frame-primary' titleLevel=2 titleClass='' contentClass='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<div class="content-frame ${class}" <#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if> >
<#if title?? && title !=''><@cTitle level=titleLevel class='h4 content-frame-title ${titleClass}'>${title}</@cTitle></#if>
<div class="content-frame-content<#if contentClass!=''> ${contentClass}</#if>"><#nested></div>
</div>
</#macro>