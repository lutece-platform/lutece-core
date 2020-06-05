<#function isSelected nIdField default_values_list>
    <#--
      default_values_list is null when a user is beeing created
    -->
    <#if default_values_list?has_content>
        <#list default_values_list as default_value>
        <#--
            - attributeField?exists is false when an attribute was just created
            and the user is beeing modified.
            - default_value.attributeField.idField is 0 when the value that was
            set is the labelNone missing value of non mandatory comboxes
        -->
            <#if default_value.attributeField?exists && default_value.attributeField.idField = nIdField>       
                <#return true>
            </#if>
        </#list>
    </#if>
    <#return false>
</#function>
	
<#if attribute.helpMessage?exists&&attribute.helpMessage!=''>
	<#assign helpKey = attribute.helpMessage>
</#if>
	
<@formGroup labelKey='${attribute.title?html}' mandatory=attribute.mandatory rows=2>
        <#--
          first pass to see if a value has been stored.
          Used to choose between the stored value and
          the 'default' attribute of the field to prefill the form
        -->
        <#assign hasStoredValue = false>
        <#list attribute.listAttributeFields as attributeField>
            <#if attributeField.title?exists && attributeField.value != "">
                    <#if default_values_list?has_content && isSelected(attributeField.idField default_values_list)>
                    <#assign hasStoredValue = true>
                    </#if>
            </#if>
		</#list>
        <#if default_values_list?has_content && isSelected(0 default_values_list)>
            <#-- if the user has unchecked all checkboxes -->
            <#assign hasStoredValue = true>
        </#if>

	<#assign checkboxMandatory = (attribute.listAttributeFields?size == 1) && mandatory>
	<#list attribute.listAttributeFields as attributeField>
		<#if attributeField.title?exists && attributeField.value != "">
			<#if attribute.fieldInLine>
				<#assign orientation = 'horizontal'>
			</#if>
            <#assign selectedStoredAttribute = hasStoredValue && default_values_list?has_content && isSelected(attributeField.idField default_values_list)>
            <#assign selectedDefaultAttribute = !hasStoredValue && attributeField.defaultValue>
            <#assign checked=selectedStoredAttribute || selectedDefaultAttribute>
			<@checkBox name='attribute_${attribute.idAttribute}' id='attribute_${attribute.idAttribute}' value='${attributeField.idField}' checked=checked labelKey='${attributeField.title}' orientation=orientation mandatory=checkboxMandatory />
		</#if>
	</#list>
</@formGroup>
