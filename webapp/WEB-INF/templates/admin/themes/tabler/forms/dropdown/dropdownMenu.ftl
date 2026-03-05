<#--
Macro: dropdownMenu

Description: Generates a dropdown menu element with a specified class, ID, and additional parameters.

Parameters:
- class (string, optional): additional classes to add to the dropdown menu.
- id (string, optional): the ID for the dropdown menu.
- params (string, optional): additional parameters to add to the HTML code.

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
<#macro dropdownMenu class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<ul class="dropdown-menu ${class}"<#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>>
<#nested>
</ul>
</#macro>