<#--
Macro: cText

Description: Generates an HTML block-level text element (paragraph by default). Wraps textual content on skin pages with a configurable tag.

Parameters:
- type (string, required): HTML tag for the text element ('p', 'blockquote', 'pre', etc.). Default: 'p'.
- id (string, optional): Unique identifier for the text element. Default: ''.
- class (string, optional): CSS class(es) applied to the text element. Default: ''.
- params (string, optional): Additional HTML attributes for the text element. Default: ''.

Showcase:
- desc: Paragraphe - @cText
- newFeature: false

Snippet:

    Basic usage:

    <@cText>
        This is a paragraph of text on the page.
    </@cText>

    As a blockquote:

    <@cText type='blockquote' class='blockquote'>
        Public services should be accessible to all citizens.
    </@cText>

-->
<#macro cText type='p' id='' class='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<${type}<#if class!=''> class="${class!}"</#if><#if id!=''> id="${id!}"</#if><#if params!=''> ${params!}</#if>>
<#nested/>
</${type}>
</#macro>