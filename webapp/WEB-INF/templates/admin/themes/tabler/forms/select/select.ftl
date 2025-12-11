
<#-- Macro: select

Description: Generates a select input field with options.

Parameters:
- name (string): the name of the select field.
- id (string, optional): the ID of the select field.
- class (string, optional): additional classes to apply to the select field.
- items (list): a list of options for the select field. Each option is an object with "name" and "code" properties.
- default_value (string, optional): the default value for the select field.
- size (string, optional): the size of the select field (e.g. "sm", "md", "lg").
- sort (boolean, optional): whether to sort the options alphabetically by name.
- multiple (integer, optional): the number of visible options when the select field is expanded. If set to a value greater than 1, the select field allows multiple selections.
- params (string, optional): additional parameters to add to the select field.
- title (string, optional): the title attribute of the select field.
- disabled (boolean, optional): whether the select field is disabled.
- tabIndex (integer, optional): the tab index of the select field.
- maxLength (integer, optional): the maximum length of each option name.
- mandatory (boolean, optional): whether the select field is mandatory.

-->
<#macro select name id=name class='' items='' default_value='' size='' sort=false multiple=0 params='' title='' disabled=false tabIndex=0 maxLength=0 mandatory=false deprecated...>
<@deprecatedWarning args=deprecated />
<#if propagateMandatory?? && propagateMandatory ><#local mandatory = true /></#if>
<select id="${id}" name="${name}" class="<#if size!=''>form-control form-control-${size}</#if><#if class!=''> ${class}<#else> form-select</#if>" <#if (multiple &gt; 0)>multiple size="${multiple}"</#if><#if (tabIndex &gt; 0)> tabindex="${tabIndex}"</#if><#if params!=''> ${params}</#if><#if title!=''> title="${title}"</#if><#if mandatory> required</#if><#if disabled> disabled</#if>>
<#if items?has_content>
<#if sort=true>
<#list items?sort_by("name") as item>
<#if default_value="${item.code}">
	<option selected="selected" title="${item.name}" value="${item.code}" <#if !item.name?has_content>label="${i18n("portal.util.labelEmpty")}"</#if>>${item.name}</option>
<#else>
	<option value="${item.code}" title="${item.name}" <#if !item.name?has_content>label="${i18n("portal.util.labelEmpty")}"</#if>>${item.name}</option>
</#if>
</#list>
<#else>
<#if maxLength gt 0>
	<#list items as item>
	<#if maxLength < item.name?length >
		<#assign item_new = "..." + "${item.name?substring(item.name?length-maxLength+3)}" >
	<#else>
		<#assign item_new = "${item.name}">
	</#if>
	<#if default_value="${item.code}">
		<option selected="selected" title="${item.name}" value="${item.code}" >${item_new}</option>
	<#else>
		<option title="${item.name}" value="${item.code}" >${item_new}</option>
	</#if>
	</#list>
<#else>
	<#list items as item>
	<#if default_value="${item.code}">
		<option selected="selected" title="${item.name}" value="${item.code}" <#if !item.name?has_content>label="${i18n("portal.util.labelEmpty")}"</#if>>${item.name}</option>
	<#else>
		<option title="${item.name}" value="${item.code}" <#if !item.name?has_content>label="${i18n("portal.util.labelEmpty")}"</#if>>${item.name}</option>
	</#if>
	</#list>
</#if>
</#if>
<#else>
<#nested>
</#if>
</select>
</#macro>