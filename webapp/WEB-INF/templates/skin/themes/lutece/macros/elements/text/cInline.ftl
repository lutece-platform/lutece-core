<#--
Macro: cInline

Description: Generates a generic HTML inline element (span by default). Useful for wrapping inline content with custom styling or attributes on skin pages.

Parameters:
- type (string, optional): HTML tag to use for the inline element ('span', 'em', 'strong', etc.). Default: 'span'.
- class (string, optional): CSS class(es) applied to the inline element. Default: ''.
- id (string, optional): Unique identifier for the inline element. Default: ''.
- params (string, optional): Additional HTML attributes for the inline element. Default: ''.

Snippet:

    Basic usage:

    <@cInline class='text-primary'>Highlighted text</@cInline>

    As a strong element:

    <@cInline type='strong' class='fw-bold'>Important notice</@cInline>

    With custom attributes:

    <@cInline type='em' params='data-toggle="tooltip" title="Additional info"'>Hover for details</@cInline>

-->
<#macro cInline type='span' class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<${type}<#if class!=''> class="${class}"</#if><#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>>
<#nested>
</${type}>
</#macro>