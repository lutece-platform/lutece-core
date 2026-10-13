<#-- 
Macro: tableHead

Description: Generates an HTML <thead> element with an optional ID, class, and additional parameters.

Parameters:
- id (string, optional): the ID of the <thead> element.
- class (string, optional): the class of the <thead> element.
- params (string, optional): additional parameters to add to the HTML code.
- attrs (hash, optional): additional HTML attributes as name/value pairs, e.g. {'aria-hidden': 'true'}. Prefer it to params: the macro writes the attribute and its value is escaped like any value of the page.

Snippet:

    Basic table header:

    <@tableHead>
        <@tr>
            <@th>#i18n{portal.users.columnName}</@th>
            <@th>#i18n{portal.users.columnEmail}</@th>
            <@th>#i18n{portal.users.columnStatus}</@th>
        </@tr>
    </@tableHead>

    Table header with custom class:

    <@tableHead id='header_users' class='bg-light'>
        <@tr>
            <@th>ID</@th>
            <@th>Label</@th>
        </@tr>
    </@tableHead>

-->
<#macro tableHead id='' class='' params='' attrs={} deprecated...>
<@deprecatedWarning args=deprecated />
<thead<@commonAttrs id=id /><#if class!=''> class="${class?trim}"</#if><@commonAttrs params=params attrs=attrs />>
<#nested>
</thead>
</#macro>