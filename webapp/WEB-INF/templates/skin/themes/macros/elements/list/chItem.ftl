<#--
Macro: chItem

Description: Generates an HTML list item (li) element for use inside a chList. Wraps individual entries in ordered or unordered lists.

Parameters:
- id (string, optional): Unique identifier for the list item element. Default: ''.
- class (string, optional): CSS class(es) applied to the list item element. Default: ''.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

Showcase:
- desc: Élément de liste - @chItem
- newFeature: false

Snippet:

    Basic usage inside a list:

    <@chList type='u'>
        <@chItem>First item</@chItem>
        <@chItem>Second item</@chItem>
        <@chItem class='active'>Third item (active)</@chItem>
    </@chList>

-->
<#macro chItem id='' class='' params={} deprecated...>
<@deprecatedWarning args=deprecated />
<li<@commonAttrs id=id class=class params=params />>
<#nested>
</li>
</#macro>