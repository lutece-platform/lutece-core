<#--
Macro: cTh

Description: Generates an HTML th (table header cell) element with scope and role attributes for accessibility. Used inside cTr within a cThead to define column or row headers.

Parameters:
- id (string, optional): Unique identifier for the table header cell. Default: ''.
- class (string, optional): CSS class(es) applied to the table header cell. Default: ''.
- scope (string, optional): Scope of the header cell ('col' or 'row'). Default: 'col'.
- role (string, optional): ARIA role for the header cell ('columnheader' or 'rowheader'). Default: 'columnheader'.
- params (string, optional): Additional HTML attributes for the table header cell. Default: ''.
- attrs (hash, optional): additional HTML attributes as name/value pairs, e.g. {'aria-hidden': 'true'}. Prefer it to params: the macro writes the attribute and its value is escaped like any value of the page.

Snippet:

    Basic usage inside a table header:

    <@cThead>
        <@cTr>
            <@cTh>Name</@cTh>
            <@cTh>Email</@cTh>
            <@cTh>Role</@cTh>
        </@cTr>
    </@cThead>

    Row header:

    <@cTh scope='row' role='rowheader'>Total</@cTh>

-->
<#macro cTh id='' class='' scope='col' role='columnheader' params='' attrs={} deprecated...>
<@deprecatedWarning args=deprecated />
<th scope="${scope!}" class="<#if class != ''>${class}</#if>"<@commonAttrs id=id params=params attrs=attrs /> >
    <#nested>
</th>
</#macro>