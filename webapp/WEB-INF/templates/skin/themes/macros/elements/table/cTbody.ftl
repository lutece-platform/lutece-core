<#--
Macro: cTbody

Description: Generates an HTML tbody element to group the body rows of a table. Used inside a cTable to wrap data rows.

Parameters:
- class (string, optional): CSS class(es) applied to the tbody element. Default: ''.
- id (string, optional): Unique identifier for the tbody element. Default: ''.
- params (string, optional): Additional HTML attributes for the tbody element. Default: ''.
- attrs (hash, optional): additional HTML attributes as name/value pairs, e.g. {'aria-hidden': 'true'}. Prefer it to params: the macro writes the attribute and its value is escaped like any value of the page.

Snippet:

    Basic usage inside a cTable:

    <@cTable caption='Users' id='users-table'>
        <@cThead>
            <@cTr><@cTh>Name</@cTh><@cTh>Email</@cTh></@cTr>
        </@cThead>
        <@cTbody>
            <@cTr><@cTd>John Doe</@cTd><@cTd>john@example.com</@cTd></@cTr>
            <@cTr><@cTd>Jane Smith</@cTd><@cTd>jane@example.com</@cTd></@cTr>
        </@cTbody>
    </@cTable>

-->
<#macro cTbody class='' id='' params='' attrs={} deprecated...>
<@deprecatedWarning args=deprecated />
<tbody <#if class?has_content>${class!}</#if><@commonAttrs id=id params=params attrs=attrs /> >
<#nested>
</tbody>
</#macro>