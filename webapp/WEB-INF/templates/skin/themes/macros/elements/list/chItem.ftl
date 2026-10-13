<#--
Macro: chItem

Description: Generates an HTML list item (li) element for use inside a chList. Wraps individual entries in ordered or unordered lists.

Parameters:
- id (string, optional): Unique identifier for the list item element. Default: ''.
- class (string, optional): CSS class(es) applied to the list item element. Default: ''.
- params (string, optional): Additional HTML attributes for the list item element. Default: ''.
- attrs (hash, optional): additional HTML attributes as name/value pairs, e.g. {'aria-hidden': 'true'}. Prefer it to params: the macro writes the attribute and its value is escaped like any value of the page.

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
<#macro chItem id='' class='' params='' attrs={} deprecated...>
<@deprecatedWarning args=deprecated />
<li<@commonAttrs id=id class=class params=params attrs=attrs />>
<#nested>
</li>
</#macro>