<#if attribute.helpMessage?exists&&attribute.helpMessage!=''>
	<#assign helpKey = attribute.helpMessage>
</#if>

<@formGroup labelKey='${attribute.title?html}' labelFor='attribute_${attribute.idAttribute}' mandatory=attribute.mandatory helpKey=helpKey rows=2>
	<#list attribute.listAttributeFields as attributeField>
		<#if attributeField.width?? && ( attributeField.width > 0 )>
			<#assign size = 'attributeField.width'>
		</#if> 
		<#if attributeField.value?exists && !default_values_list?has_content>
			<#assign value = attributeField.value>
		<#elseif default_values_list?has_content>
			<#list default_values_list as default_value>
				<#assign value = default_value.value>
			</#list>
		<#else>
			<#assign value = ''>
		</#if>
		<#if ( attributeField.maxSizeEnter >= 0 )>
			<#assign maxlength = attributeField.maxSizeEnter>
		</#if>
	</#list>
	
	<@input type='text' name='attribute_${attribute.idAttribute}' id='attribute_${attribute.idAttribute}' size=size value=value />
</@formGroup>