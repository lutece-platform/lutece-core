<#--

Macro: sort

Description: This macro generates a sort menu displayed on the right of a table header cell (<th>). The menu is a dropdown with two entries, one for the ascending sort and one for the descending sort. The labels and icons of the entries depend on the type of the sorted value (text, date, boolean or number). When a sort is applied, the dropdown button icon and title are updated with the icon and label of the chosen entry (handled by admin-table-sort.js, based on the sorted_attribute_name and asc_sort parameters of the current URL).

Parameters:
- jsp_url: the URL of the web page to sort, including any query string parameters
- attribute: the name of the attribute to sort by
- id (optional): a unique identifier for the sort menu
- type (optional): the type of the sorted value, among 'text' (default), 'date', 'boolean' and 'number'. Sets the labels and icons of the two menu entries: text "Sort A to Z" / "Sort Z to A", date "Recent" / "Oldest", boolean "True" / "False", number "Smaller" / "Greater".
- titleAscDesc (optional): an array containing custom labels for the ascending and descending entries, respectively. Overrides the type based labels.

Snippet:

    Sort menu for a text column in a table header:

    <@th>
        #i18n{portal.users.columnName}
        <@sort jsp_url='jsp/admin/ManageUsers.jsp' attribute='name' />
    </@th>

    Sort menu for a date column, with a URL containing query parameters:

    <@th>
        #i18n{portal.users.columnDate}
        <@sort jsp_url='jsp/admin/ManageUsers.jsp?plugin_name=myPlugin' attribute='date_creation' id='users' type='date' />
    </@th>

-->
<#macro sort jsp_url attribute id='' type='text' titleAscDesc=[] deprecated...>
<@deprecatedWarning args=deprecated />
<#if jsp_url?contains("?")>
<#assign sort_url = jsp_url + "&amp;sorted_attribute_name=" + attribute + "&amp;asc_sort=" />
<#else>
<#assign sort_url = jsp_url + "?sorted_attribute_name=" + attribute + "&amp;asc_sort=" />
</#if>
<#switch type>
	<#case 'date'>
		<#local sortLabels = ['#i18n{portal.util.sort.date.asc}','#i18n{portal.util.sort.date.desc}'] />
		<#local sortIcons = ['sort-ascending','sort-descending'] />
		<#break>
	<#case 'boolean'>
		<#local sortLabels = ['#i18n{portal.util.sort.boolean.asc}','#i18n{portal.util.sort.boolean.desc}'] />
		<#local sortIcons = ['sort-ascending','sort-descending'] />
		<#break>
	<#case 'number'>
		<#local sortLabels = ['#i18n{portal.util.sort.number.asc}','#i18n{portal.util.sort.number.desc}'] />
		<#local sortIcons = ['sort-ascending-numbers','sort-descending-numbers'] />
		<#break>
	<#default>
		<#local sortLabels = ['#i18n{portal.util.sort.text.asc}','#i18n{portal.util.sort.text.desc}'] />
		<#local sortIcons = ['sort-ascending-letters','sort-descending-letters'] />
</#switch>
<#if titleAscDesc?size gt 1><#local sortLabels = titleAscDesc /></#if>
<#local sortId = 'sort${id!}_${attribute!}' />
<div class="dropdown float-end lutece-table-sort" id="${sortId}" data-sort-attribute="${attribute}">
	<button type="button" class="btn btn-action btn-sm" data-bs-toggle="dropdown" aria-expanded="false" title="#i18n{portal.util.sort.label}">
		<@icon style='chevron-down' />
		<span class="visually-hidden">#i18n{portal.util.sort.label}</span>
	</button>
	<ul class="dropdown-menu">
		<li>
			<a class="dropdown-item" href="${sort_url}true#${sortId}" title="${sortLabels[0]}" data-asc="true" data-sort-icon="ti ti-${sortIcons[0]}">
				<@icon style=sortIcons[0] class='me-1' /> ${sortLabels[0]}
			</a>
		</li>
		<li>
			<a class="dropdown-item" href="${sort_url}false#${sortId}" title="${sortLabels[1]}" data-asc="false" data-sort-icon="ti ti-${sortIcons[1]}">
				<@icon style=sortIcons[1] class='me-1' /> ${sortLabels[1]}
			</a>
		</li>
	</ul>
</div>
</#macro>