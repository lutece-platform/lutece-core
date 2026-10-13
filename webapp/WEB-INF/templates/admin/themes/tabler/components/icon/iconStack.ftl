<#-- Macro: iconStack

Description: Generates a stack of icons.

Parameters:
- class (string, optional): the CSS class for the icon stack.
- id (string, optional): the ID attribute for the icon stack.
- params (string, optional): additional parameters to add to the icon stack.
- attrs (hash, optional): additional HTML attributes as name/value pairs, e.g. {'aria-hidden': 'true'}. Prefer it to params: the macro writes the attribute and its value is escaped like any value of the page.

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
<#macro iconStack class='fa-2x' id='' params='' attrs={} deprecated...>
<@deprecatedWarning args=deprecated />
<span class="fa-stack ${class}"<@commonAttrs id=id params=params attrs=attrs />>
<#nested>
</span>
</#macro>