<#--
Macro: dropdownItem

Description: Generates an HTML list item element that serves as a dropdown menu item, with a specified class, href, target, title, ID, and parameters.

Parameters:
- class (string, optional): additional classes to add to the list item.
- href (string, required): the hyperlink for the dropdown menu item.
- target (string, optional): the target for the dropdown menu item.
- title (string, optional): the title for the dropdown menu item.
- id (string, optional): the ID for the list item.
- params (string, optional): additional parameters to add to the HTML code.
- attrs (hash, optional): additional HTML attributes as name/value pairs, e.g. {'aria-hidden': 'true'}. Prefer it to params: the macro writes the attribute and its value is escaped like any value of the page.

Snippet:

    Simple dropdown item:

    <@dropdownItem href='jsp/admin/ManageUsers.jsp' title='Manage Users' />

    Dropdown item opening in a new tab with custom class:

    <@dropdownItem href='https://lutece.com/docs' title='Documentation' target='_blank' class='text-primary' />

-->
<#macro dropdownItem class='' href='' target='' title='' id='' params='' attrs={} deprecated...>
<@deprecatedWarning args=deprecated />
<li>
	<a href="${href}" class="dropdown-item<#if class?has_content> ${class}</#if>" title="${title}"<@commonAttrs id=id params=params attrs=attrs /><#if target?has_content> target="${target}"</#if>>${title}</a>
</li>
</#macro>