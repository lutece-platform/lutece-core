<#-- 
Macro: tableBody

Description: Generates an HTML <tbody> element with an optional ID, class, and additional parameters.

Parameters:
- id (string, optional): the ID of the <tbody> element.
- class (string, optional): the class of the <tbody> element.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

Snippet:

    Basic table body with rows:

    <@tableBody>
        <@tr>
            <@td>John Doe</@td>
            <@td>john.doe@example.com</@td>
            <@td>Active</@td>
        </@tr>
        <@tr>
            <@td>Jane Smith</@td>
            <@td>jane.smith@example.com</@td>
            <@td>Inactive</@td>
        </@tr>
    </@tableBody>

    Table body with ID for dynamic content:

    <@tableBody id='user_list' class='sortable'>
        <#list users as user>
            <@tr>
                <@td>${user.name}</@td>
                <@td>${user.email}</@td>
            </@tr>
        </#list>
    </@tableBody>

-->
<#macro tableBody id='' class='' params={} deprecated...>
<@deprecatedWarning args=deprecated />
<tbody<@commonAttrs id=id class=class params=params />>
<#nested>
</tbody>
</#macro>