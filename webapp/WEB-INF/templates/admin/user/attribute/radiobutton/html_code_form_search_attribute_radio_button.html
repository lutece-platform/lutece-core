<#function isSelected nIdField default_values_list>
	<#list default_values_list as default_value>
		<#if default_value.attributeField?exists && default_value.attributeField.idField = nIdField && default_value.value != "">  		
			<#return true>
		</#if>
	</#list>
	<#return false>
</#function>

<@formGroup labelKey='${attribute.title?html}' rows=2>
	<#if attribute.fieldInLine>
		<#assign orientation = horizontal>
	<#else>
		<#assign orientation = vertical>
	</#if>
	
	<#if !default_values_list?has_content>
		<#assign checked = true>
	</#if>
	<@radioButton name='attribute_${attribute.idAttribute}' labelKey='#i18n{portal.users.attribute.labelAll}' checked=checked orientation=orientation />
	
	<#list attribute.listAttributeFields as attributeField>
    <#if attributeField.title?exists && attributeField.value != ''>
		<#if default_values_list?has_content && isSelected( attributeField.idField,default_values_list ) >
	        <#assign checked = true>
		</#if>
		<@radioButton name='attribute_${attribute.idAttribute}' labelKey='${attributeField.title}' checked=checked orientation=orientation />
    </#if>
    </#list>
</@formGroup>