<#--
Macro: callOut

Description: Generates an HTML callout element with an optional icon and title.

Parameters:
- color (string, required): the background color of the callout element.
- titleLevel (string, optional): the level of the title element (h1, h2, h3, h4, h5, or h6).
- title (string, optional): the title of the callout element.
- callOutIcon (string, optional): the name of the icon to include in the callout element.
- id (string, optional): the ID of the callout element. If not provided, a default ID will be generated.
- params (string, optional): additional HTML attributes to include in the callout element.
- attrs (hash, optional): additional HTML attributes as name/value pairs, e.g. {'aria-hidden': 'true'}. Prefer it to params: the macro writes the attribute and its value is escaped like any value of the page.

Snippet:

    Info callout with icon and title:

    <@callOut color='info' title='Information' callOutIcon='info-circle'>
        This section provides additional context about the current page.
    </@callOut>

    Warning callout with custom heading level:

    <@callOut color='warning' title='Attention' callOutIcon='exclamation-triangle' titleLevel='h4'>
        Please review the following items before proceeding.
    </@callOut>

-->
<#macro callOut color='' titleLevel='h3' title='' callOutIcon='' id='' params='' attrs={} deprecated...>
<@deprecatedWarning args=deprecated />
<div class="alert alert-important alert-${color}"<@commonAttrs id=id params=params attrs=attrs />>
	<#if title!=''><${titleLevel} class='mb-0'><@icon style=callOutIcon /> ${title}</${titleLevel}></#if>
	<#nested>
</div>
</#macro>