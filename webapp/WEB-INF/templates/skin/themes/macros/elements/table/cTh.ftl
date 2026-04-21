<#--
Macro: cTh

Description: Generates an HTML th (table header cell) element with scope and role attributes for accessibility. Used inside cTr within a cThead to define column or row headers.

Parameters:
- id (string, optional): Unique identifier for the table header cell. Default: ''.
- class (string, optional): CSS class(es) applied to the table header cell. Default: ''.
- scope (string, optional): Scope of the header cell ('col' or 'row'). Default: 'col'.
- role (string, optional): ARIA role for the header cell ('columnheader' or 'rowheader'). Default: 'columnheader'.
- params (string, optional): Additional HTML attributes for the table header cell. Default: ''.

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
<#macro cTh id='' class='' scope='col' role='columnheader' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<th scope="${scope!}" class="<#if class != ''>${class}</#if>"<#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if> >
    <#nested>
</th>
</#macro>