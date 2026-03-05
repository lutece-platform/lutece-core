<#--
Macro: chList

Description: Generates an HTML unordered (ul) or ordered (ol) list element. Use with chItem to build structured lists on skin pages.

Parameters:
- type (string, required): Type of list: 'u' for unordered (ul) or 'o' for ordered (ol). Default: 'u'.
- id (string, optional): Unique identifier for the list element. Default: ''.
- class (string, optional): CSS class(es) applied to the list element. Default: ''.
- params (string, optional): Additional HTML attributes for the list element. Default: ''.

Snippet:

    Unordered list:

    <@chList type='u' class='list-unstyled'>
        <@chItem>Home</@chItem>
        <@chItem>About</@chItem>
        <@chItem>Contact</@chItem>
    </@chList>

    Ordered list:

    <@chList type='o'>
        <@chItem>Step one</@chItem>
        <@chItem>Step two</@chItem>
        <@chItem>Step three</@chItem>
    </@chList>

-->
<#macro chList type='u' id='' class='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<${type}l<#if class != ''> class="${class}"</#if><#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if> >
    <#nested>
</${type}l>
</#macro>