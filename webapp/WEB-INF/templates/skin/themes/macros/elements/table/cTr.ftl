<#--
Macro: cTr

Description: Generates an HTML tr (table row) element. Used inside cThead, cTbody, or cTfoot to define a row of cells.

Parameters:
- class (string, optional): CSS class(es) applied to the table row. Default: ''.
- id (string, optional): Unique identifier for the table row. Default: ''.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

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
<#macro cTr class='' id='' params={} deprecated...>
<@deprecatedWarning args=deprecated />
<tr<@commonAttrs id=id class=class params=params /> >
<#nested>
</tr>
</#macro>