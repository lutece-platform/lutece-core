<#-- Macro: iconStack

Description: Generates a stack of icons.

Parameters:
- class (string, optional): the CSS class for the icon stack.
- id (string, optional): the ID attribute for the icon stack.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

Snippet:

    Stacked icons with a circle background:

    <@iconStack>
        <@icon style='circle fa-stack-2x' />
        <@icon style='home fa-stack-1x fa-inverse' />
    </@iconStack>

    Stacked icons with a custom CSS class:

    <@iconStack class='fa-3x' id='status-icon'>
        <@icon style='square fa-stack-2x text-primary' />
        <@icon style='check fa-stack-1x fa-inverse' />
    </@iconStack>

-->
<#macro iconStack class='fa-2x' id='' params={} deprecated...>
<@deprecatedWarning args=deprecated />
<span class="fa-stack ${class}"<@commonAttrs id=id params=params />>
<#nested>
</span>
</#macro>