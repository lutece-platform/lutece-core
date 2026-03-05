<#--
Macro: cTd

Description: Generates an HTML td (table data cell) element with optional scope, role, and colspan attributes. Wraps cell content in a span by default for styling purposes.

Parameters:
- id (string, optional): Unique identifier for the table cell. Default: ''.
- class (string, optional): CSS class(es) applied to the table cell. Default: ''.
- scope (string, optional): Scope of the cell ('col' or 'row'). Default: ''.
- headers (string, optional): Space-separated list of header cell IDs that provide context for this data cell. Default: ''.
- role (string, optional): ARIA role for the table cell. Default: ''.
- colspan (string, optional): Number of columns the cell should span. Default: ''.
- nospan (boolean, optional): If true, omits the inner span wrapper around the cell content. Default: false.
- params (string, optional): Additional HTML attributes for the table cell. Default: ''.

Snippet:

    Basic usage:

    <@cTr>
        <@cTd>John Doe</@cTd>
        <@cTd>john@example.com</@cTd>
    </@cTr>

    With colspan and no inner span:

    <@cTr>
        <@cTd colspan='2' nospan=true>
            <p>This cell spans two columns without an inner span.</p>
        </@cTd>
    </@cTr>

-->
<#macro cTd id='' class='' scope='' headers='' role='' colspan='' nospan=false params='' deprecated...>
<@deprecatedWarning args=deprecated />
<td<#if scope!=''> scope="${scope!}"</#if><#if colspan!=''> colspan="${colspan!}"</#if><#if headers!=''> headers="${headers!}"</#if><#if class != ''> class="${class}"</#if><#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if><#if role!=''> role="${role!}"</#if>>
<#if !nospan><span><#nested></span><#else><#nested></#if>
</td>
</#macro>