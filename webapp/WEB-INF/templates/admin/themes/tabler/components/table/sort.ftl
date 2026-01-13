<#-- 

Macro: sort

Description: This macro generates a pair of buttons that allow sorting the data displayed on a web page by a specified attribute. 

Parameters:
- jsp_url: the URL of the web page to sort, including any query string parameters
- attribute: the name of the attribute to sort by
- id (optional): a unique identifier for the sort button group
-->
<#macro sort jsp_url attribute id='' deprecated...>
<@deprecatedWarning args=deprecated />
<#if jsp_url?contains("?")>
<#assign sort_url = jsp_url + "&amp;sorted_attribute_name=" + attribute + "&amp;asc_sort=" />
<#else>
<#assign sort_url = jsp_url + "?sorted_attribute_name=" + attribute + "&amp;asc_sort=" />
</#if>
<@btnGroup ariaLabel='#i18n{portal.util.sort.label}'>
	<@aButton color='default btn-ghost-dark' size='sm' id='sort${id!}_${attribute!}' href='${sort_url}true#sort${id!}_${attribute!}' title='#i18n{portal.util.sort.asc}' buttonIcon='chevron-up' hideTitle=['all'] />
	<@aButton color='default btn-ghost-dark' size='sm' href='${sort_url}false#sort${id!}_${attribute!}' title='#i18n{portal.util.sort.desc}' buttonIcon='chevron-down' hideTitle=['all'] />
</@btnGroup>
</#macro>