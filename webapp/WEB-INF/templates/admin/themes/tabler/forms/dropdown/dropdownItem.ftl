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
-->
<#macro dropdownItem class='' href='' target='' title='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<li>
	<a href="${href}" class="dropdown-item<#if class!=''> ${class}</#if>" title="${title}"<#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if><#if target!=''> target="${target}"</#if>>${title}</a>
</li>
</#macro>