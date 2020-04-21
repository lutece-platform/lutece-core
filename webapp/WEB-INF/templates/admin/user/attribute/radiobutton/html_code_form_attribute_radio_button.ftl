<#function isSelected nIdField default_values_list>
    <#--
      default_values_list is null when a user is beeing created
    -->
	<#if default_values_list?has_content>
        <#--
            - attributeField?exists is false when an attribute was just created
            and the user is beeing modified.
            - default_value.attributeField.idField is 0 when the value that was
            set is the labelNone missing value of non mandatory comboxes
        -->
		<#list default_values_list as default_value>
			<#if default_value.attributeField?exists && default_value.attributeField.idField = nIdField>  		
  				<#return true>
  			</#if>
  		</#list>
	</#if>
	<#return false>
</#function>
<#function hasElement default_values_list>
	<#if default_values_list?has_content>
		<#list default_values_list as default_value>
			<#if default_value.idUserField != 0>
				<#return true>
			</#if>
		</#list>
	</#if>
	<#return false>
</#function>
	
<#if attribute.helpMessage?exists&&attribute.helpMessage!=''>
	<#assign helpKey = attribute.helpMessage>
</#if>

<@formGroup labelKey='${attribute.title?html}' rows=2 helpKey=helpKey mandatory=attribute.mandatory>
	<#if attribute.fieldInLine>
		<#assign orientation = 'horizontal'>
	<#else>
		<#assign orientation = 'vertical'>
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
	
	<#if !attribute.mandatory>
        <#--
          second pass to see if any attribute is the default value.
          If not, we want to check the "novalue" radio button.
        -->
        <#assign hasDefaultField = false>
        <#list attribute.listAttributeFields as attributeField>
            <#if attributeField.defaultValue>
                <#assign hasDefaultField = true>
            </#if>
        </#list>

        <#if (default_values_list?has_content && isSelected( 0 default_values_list ) ) || (!hasStoredValue && !hasDefaultField) >
			<#assign checked = true>
		<#else>
			<#assign checked = false>
		</#if>
		<@radioButton name='attribute_${attribute.idAttribute}' value='' checked=checked labelKey='#i18n{portal.users.attribute.labelNone}' orientation=orientation />
	</#if>
	
	<#list attribute.listAttributeFields as attributeField>
		<#if attributeField.title?exists && attributeField.value != "">
            <#assign checkedStoredAttribute = hasStoredValue && default_values_list?has_content && isSelected(attributeField.idField default_values_list)>
            <#assign checkedDefaultAttribute = !hasStoredValue && attributeField.defaultValue>
            <#assign checked = checkedStoredAttribute || checkedDefaultAttribute>
			<@radioButton name='attribute_${attribute.idAttribute}' value='${attributeField.idField}' checked=checked labelKey='${attributeField.title}' orientation=orientation />
		</#if>
	</#list>
</@formGroup>