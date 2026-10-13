<#-- Macro: formGroup

Description: Generates a Bootstrap form group with a label, input field, and help text.

Parameters:
- id (string, optional): the ID of the form group.
- formStyle (string, optional): the form style (horizontal, inline, col, fullwidth or blank).
- groupStyle (string, optional): the style of the form group (success or error).
- class (string, optional): additional CSS classes to add to the form group.
- rows (number, optional): the number of rows in the input field.
- labelKey (string, optional): the internationalization key for the label text.
- labelFor (string, optional): the ID of the input field that the label is associated with.
- labelId (string, optional): the ID of the label element.
- labelClass (string, optional): additional CSS classes to add to the label element.
- helpKey (string, optional): the internationalization key for the help text.
- mandatory (boolean, optional): whether the input field is mandatory.
- hideLabel (list, optional): a list of label properties to hide (e.g. "label", "i18nLabel").
- collapsed (boolean, optional): whether to collapse the form group.
- params (string, optional): additional parameters to add to the form group element.
- attrs (hash, optional): additional HTML attributes as name/value pairs, e.g. {'aria-hidden': 'true'}. Prefer it to params: the macro writes the attribute and its value is escaped like any value of the page.

Snippet:

    Horizontal form group with label and text input:

    <@formGroup labelKey='#i18n{portal.users.label.email}' labelFor='email' formStyle='horizontal'>
        <@input type='email' name='email' id='email' placeHolder='user@example.com' />
    </@formGroup>

    Mandatory form group with help text:

    <@formGroup labelKey='#i18n{portal.users.label.login}' labelFor='login' formStyle='horizontal' mandatory=true helpKey='Enter a unique login identifier.'>
        <@input type='text' name='login' id='login' mandatory=true />
    </@formGroup>

    Inline form group:

    <@formGroup labelKey='#i18n{portal.search.label.query}' labelFor='query' formStyle='inline'>
        <@input type='text' name='query' id='query' />
    </@formGroup>

-->
<#macro formGroup id='' formStyle='horizontal' groupStyle='' class='' rows=1 labelKey='' labelKeyDesc='' labelFor='' labelId='' labelClass='' helpKey='' mandatory=false hideLabel=[] collapsed=false params='' attrs={} deprecated...>
<@deprecatedWarning args=deprecated />	
<#if groupStyle = 'success'><#local validation = 'is-valid'><#elseif groupStyle='error'><#local validation = 'is-invalid'></#if>
<#local classBase = ( formStyle == 'col' )?then( 'row', '' ) />
<#if formStyle='horizontal' || formStyle='col' || formStyle='fullwidth'><#local classBase += ' mb-3' /><#elseif formStyle='inline'><#local classBase += ' g-3' /></#if>
<#local classExtra = collapsed?then( 'collapse', '' ) + ' ' + validation! />
<div<@commonAttrs id=id classBase=classBase class=class classExtra=classExtra params=params attrs=attrs />>
<#local displayLabelClass = displaySettings(hideLabel,'inline-flex') />
<#local formLabelClass = labelClass />
<#if rows=1>
	<#if labelKey!='' && formStyle='horizontal'>
		<#local formLabelClass += ' form-label'>
		<#if displayLabelClass?contains('d-none')>
			<#local divClass='col'>
		<#else>
			<#local divClass='col'>
		</#if>
	<#elseif formStyle='inline'>
		<#local formLabelClass = 'col-auto' />
		<#local divClass = 'col-auto' />
	<#elseif formStyle = 'fullwidth'>
		<#local formLabelClass = 'form-label' />
		<#local divClass = '' />
	<#else>
		<#local divClass = ''>
	</#if>
<#else>
	<#local formLabelClass += ' form-label'>
	<#local divClass = 'col-12'>
</#if>
<#if labelKey!=''>
	<@formLabel class=formLabelClass?trim labelFor=labelFor labelKeyDesc=labelKeyDesc labelId=labelId labelKey=labelKey hideLabel=hideLabel mandatory=mandatory />
</#if>
<#assign propagateMandatory = mandatory>
<div class="${divClass}">
<#nested>
<#assign propagateMandatory = false>
<#if helpKey!=''><#if formStyle!='inline'><p class="mb-0"></#if><small class="text-muted form-text"<#if labelFor!=''> id="help_${labelFor}"</#if>>${helpKey}</small><#if formStyle!='inline'></p></#if></#if>
</div>
</div>
</#macro>
