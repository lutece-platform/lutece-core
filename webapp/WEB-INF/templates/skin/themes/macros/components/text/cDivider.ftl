<#--
Macro: cDivider

Description: Generates a horizontal text divider element, typically used to separate form sections with an "or" label.

Parameters:
- label (string, optional): Text displayed in the divider. Default: '#i18n{portal.theme.labelOr}'.
- class (string, optional): Additional CSS classes. Default: ''.
- id (string, optional): HTML id attribute. Default: ''.
- params (string, optional): Additional HTML attributes. Default: ''.
- attrs (hash, optional): additional HTML attributes as name/value pairs, e.g. {'aria-hidden': 'true'}. Prefer it to params: the macro writes the attribute and its value is escaped like any value of the page.

Showcase:
- desc: Séparateur - @cDivider
- newFeature: false

Snippet:

    Basic divider:

    <@cDivider />

    Divider with custom label:

    <@cDivider label='or continue with' class='my-4' />

-->
<#macro cDivider label='#i18n{portal.theme.labelOr}' class='' id='' params='' attrs={} deprecated...>
<@deprecatedWarning args=deprecated />
<p class="divider<#if class != ''> ${class}</#if>"<@commonAttrs id=id params=params attrs=attrs />>${label!}</p>
</#macro>