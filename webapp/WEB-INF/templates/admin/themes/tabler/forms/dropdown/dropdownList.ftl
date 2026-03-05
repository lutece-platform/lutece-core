<#--
Macro: dropdownList

Description: Generates a dropdown menu list element with a specified ID and additional parameters.

Parameters:
- id (string, optional): the ID for the dropdown menu list.
- params (string, optional): additional parameters to add to the HTML code.

Snippet:

    Basic dropdown list with custom content:

    <@dropdownList id='filter-list'>
        <a class="dropdown-item" href="jsp/admin/ManageUsers.jsp">Users</a>
        <a class="dropdown-item" href="jsp/admin/ManageRoles.jsp">Roles</a>
        <div class="dropdown-divider"></div>
        <a class="dropdown-item" href="jsp/admin/ManageSettings.jsp">Settings</a>
    </@dropdownList>

-->
<#macro dropdownList id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<div class="dropdown-menu"<#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>>
<#nested>
</div>
</#macro>