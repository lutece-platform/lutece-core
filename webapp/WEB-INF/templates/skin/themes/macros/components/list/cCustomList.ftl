<#--
Macro: cCustomList

Description: Generates a custom list component. Deprecated: use cList instead.

Parameters:
- items (list, required): List of items to display.
- type (string, optional): List style type. Default: 'arrow'.
- class (string, optional): Additional CSS classes. Default: ''.
- id (string, optional): HTML id attribute. Default: ''.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

Showcase:
- desc: Liste personnalisée - @cCustomList
- newFeature: false

Snippet:

    Basic usage (deprecated, prefer cList):

    <@cCustomList items=myItems type='arrow'>
        <li>Additional item</li>
    </@cCustomList>

-->
<#macro cCustomList items type='arrow' class='' id='' params={}>
<@cList items type class id params >
    <#nested>    
</@cList>
</#macro>