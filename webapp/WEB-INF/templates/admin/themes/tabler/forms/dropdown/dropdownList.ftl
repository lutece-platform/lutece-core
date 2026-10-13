<#--
Macro: dropdownList

Description: Generates a dropdown menu list element with a specified ID and additional parameters.

Parameters:
- id (string, optional): the ID for the dropdown menu list.
- params (string, optional): additional parameters to add to the HTML code.
- attrs (hash, optional): additional HTML attributes as name/value pairs, e.g. {'aria-hidden': 'true'}. Prefer it to params: the macro writes the attribute and its value is escaped like any value of the page.

Snippet:

    Basic dropdown list with custom content:

    <@dropdownList id='filter-list'>
        <a class="dropdown-item" href="jsp/admin/ManageUsers.jsp">Users</a>
        <a class="dropdown-item" href="jsp/admin/ManageRoles.jsp">Roles</a>
        <div class="dropdown-divider"></div>
        <a class="dropdown-item" href="jsp/admin/ManageSettings.jsp">Settings</a>
    </@dropdownList>

-->
<#macro dropdownList id='' params='' attrs={} deprecated...>
<@deprecatedWarning args=deprecated />
<div class="dropdown-menu"<@commonAttrs id=id params=params attrs=attrs />>
<#nested>
</div>
</#macro>