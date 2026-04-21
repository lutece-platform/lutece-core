<#--
Macro: cFigCaption

Description: Generates an HTML figcaption element for use inside a figure. Provides a text caption describing an image or illustration.

Parameters:
- id (string, optional): Unique identifier for the figcaption element. Default: ''.
- class (string, optional): CSS class(es) applied to the figcaption element. Default: ''.
- params (string, optional): Additional HTML attributes for the figcaption element. Default: ''.

Snippet:

    Basic usage:

    <@cFigCaption>
        Photo of the city hall during the annual celebration.
    </@cFigCaption>

    With a custom class:

    <@cFigCaption class='figure-caption text-muted'>
        Source: City archives, 2025.
    </@cFigCaption>

-->
<#macro cFigCaption id='' class='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<figcaption <#if class!=''>class="${class!}"</#if><#if id!=''> id="${id!}"</#if><#if params!=''> ${params!}</#if>>
<#nested/>
</figcaption>
</#macro>