<#--
Macro: chItem

Description: Generates an HTML list item (li) element for use inside a chList. Wraps individual entries in ordered or unordered lists.

Parameters:
- id (string, optional): Unique identifier for the list item element. Default: ''.
- class (string, optional): CSS class(es) applied to the list item element. Default: ''.
- params (string, optional): Additional HTML attributes for the list item element. Default: ''.

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
<#macro chItem id='' class='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<li<#if class != ''> class="${class}"</#if><#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>>
<#nested>
</li>
</#macro>