<#--
Macro: dropdownMenu

Description: Generates a dropdown menu element with a specified class, ID, and additional parameters.

Parameters:
- class (string, optional): additional classes to add to the dropdown menu.
- id (string, optional): the ID for the dropdown menu.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

Snippet:

    Basic dropdown menu:

    <@dropdownMenu>
        <@dropdownItem href='jsp/admin/ManageUsers.jsp' title='Manage Users' />
        <@dropdownItem href='jsp/admin/ManageRoles.jsp' title='Manage Roles' />
    </@dropdownMenu>

    Dropdown menu with custom class and ID:

    <@dropdownMenu class='dropdown-menu-end' id='actions-menu'>
        <@dropdownItem href='jsp/admin/EditItem.jsp?id=1' title='Edit' />
        <@dropdownItem href='jsp/admin/DeleteItem.jsp?id=1' title='Delete' />
    </@dropdownMenu>

-->
<#macro dropdownMenu class='' id='' params={} deprecated...>
<@deprecatedWarning args=deprecated />
<ul class="dropdown-menu ${class}"<@commonAttrs id=id params=params />>
<#nested>
</ul>
</#macro>