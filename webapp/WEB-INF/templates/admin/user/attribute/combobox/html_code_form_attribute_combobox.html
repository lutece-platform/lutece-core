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
<#function isEmptyString default_values_list>
    <#if default_values_list?has_content>
        <#list default_values_list as default_value>
            <#if default_value.value == "">         
                <#return true>
            </#if>
        </#list>
    </#if>
    <#return false>
</#function>
<#function isMultiple listAttributeFields>
    <#list listAttributeFields as attributeField>
        <#if attributeField.multiple = true>
            <#return true>
        </#if>
        <#break>
    </#list>
    <#return false>
</#function>

<#if attribute.helpMessage?exists&&attribute.helpMessage!=''>
	<#assign helpKey = attribute.helpMessage>
</#if>

<@formGroup labelKey='${attribute.title?html}' labelFor='attribute_${attribute.idAttribute}' helpKey=helpKey mandatory=attribute.mandatory rows=2>
	<#if isMultiple( attribute.listAttributeFields )>
		<#assign multiple = 10>
    </#if>
	<@select name='attribute_${attribute.idAttribute}' id='attribute_${attribute.idAttribute}' multiple=multiple>
		<#if !attribute.mandatory && !isMultiple( attribute.listAttributeFields ) >
            <option value="">
				#i18n{portal.users.attribute.labelNone}
            </option>
        </#if>

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
            <#--! if the user has selected the "labelNone" value, do not use the default value-->
            <#assign hasStoredValue = true>
        </#if>

        <#list attribute.listAttributeFields as attributeField>
            <#if attributeField.title?exists && attributeField.value != "">
                <option value="${attributeField.idField}" title="${attributeField.title?html}"
                    <#assign selectedStoredAttribute = hasStoredValue && default_values_list?has_content && isSelected(attributeField.idField default_values_list)>
                    <#assign selectedDefaultAttribute = !hasStoredValue && attributeField.defaultValue>
                    <#if selectedStoredAttribute || selectedDefaultAttribute>
                        selected="selected"  
                    </#if>
                >
                    ${attributeField.title?html}
                </option>
            </#if>
		</#list>
	</@select>
</@formGroup>
