<#function isMultiple listAttributeFields>
	<#list listAttributeFields as attributeField>
		<#if attributeField.multiple = true>
			<#return true>
		</#if>
		<#break>
	</#list>
	<#return false>
</#function>
<#function isSelected nIdField default_values_list>
	<#list default_values_list as default_value>
		<#if default_value.attributeField?exists && default_value.attributeField.idField = nIdField && default_value.value != "">  		
			<#return true>
		</#if>
	</#list>
	<#return false>
</#function>

<@formGroup labelKey='${attribute.title?html}' labelFor='attribute_${attribute.idAttribute}' helpKey=helpKey mandatory=mandatory rows=2>
	<#if isMultiple( attribute.listAttributeFields )>
		<#assign multiple = 10>
    </#if>
	<@select name='attribute_${attribute.idAttribute}' id='attribute_${attribute.idAttribute}' multiple=multiple>
		<#if !isMultiple( attribute.listAttributeFields )>
			<option value="">
				#i18n{portal.users.attribute.labelAll}
			</option>
		</#if>
		<#list attribute.listAttributeFields as attributeField>
			<#if attributeField.title?exists && attributeField.value != "">
				<option value="${attributeField.idField}"
					<#if default_values_list?has_content && isSelected( attributeField.idField,default_values_list ) >
						selected="selected"
					</#if>
				>
					${attributeField.title?html}
				</option>
			</#if>
		</#list>
	</@select>
</@formGroup>