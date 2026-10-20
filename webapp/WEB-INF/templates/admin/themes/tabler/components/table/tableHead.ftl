<#-- 
Macro: tableHead

Description: Generates an HTML <thead> element with an optional ID, class, and additional parameters.

Parameters:
- id (string, optional): the ID of the <thead> element.
- class (string, optional): the class of the <thead> element.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

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
<#macro tableHead id='' class='' params={} deprecated...>
<@deprecatedWarning args=deprecated />
<thead<@commonAttrs id=id class=class params=params />>
<#nested>
</thead>
</#macro>