<#--
Macro: dropdownList

Description: Generates a dropdown menu list element with a specified ID and additional parameters.

Parameters:
- id (string, optional): the ID for the dropdown menu list.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

Snippet:

    Basic dropdown list with custom content:

    <@dropdownList id='filter-list'>
        <a class="dropdown-item" href="jsp/admin/ManageUsers.jsp">Users</a>
        <a class="dropdown-item" href="jsp/admin/ManageRoles.jsp">Roles</a>
        <div class="dropdown-divider"></div>
        <a class="dropdown-item" href="jsp/admin/ManageSettings.jsp">Settings</a>
    </@dropdownList>

-->
<#macro dropdownList id='' params={} deprecated...>
<@deprecatedWarning args=deprecated />
<div class="dropdown-menu"<@commonAttrs id=id params=params />>
<#nested>
</div>
</#macro>