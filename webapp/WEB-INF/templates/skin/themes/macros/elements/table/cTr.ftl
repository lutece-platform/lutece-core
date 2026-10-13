<#--
Macro: cTr

Description: Generates an HTML tr (table row) element. Used inside cThead, cTbody, or cTfoot to define a row of cells.

Parameters:
- class (string, optional): CSS class(es) applied to the table row. Default: ''.
- id (string, optional): Unique identifier for the table row. Default: ''.
- params (string, optional): Additional HTML attributes for the table row. Default: ''.
- attrs (hash, optional): additional HTML attributes as name/value pairs, e.g. {'aria-hidden': 'true'}. Prefer it to params: the macro writes the attribute and its value is escaped like any value of the page.

Snippet:

    Basic usage inside a table body:

    <@cTbody>
        <@cTr>
            <@cTd>Alice Martin</@cTd>
            <@cTd>alice@example.com</@cTd>
        </@cTr>
        <@cTr class='table-active'>
            <@cTd>Bob Dupont</@cTd>
            <@cTd>bob@example.com</@cTd>
        </@cTr>
    </@cTbody>

-->
<#macro cTr class='' id='' params='' attrs={} deprecated...>
<@deprecatedWarning args=deprecated />
<tr<@commonAttrs id=id class=class params=params attrs=attrs /> >
<#nested>
</tr>
</#macro>