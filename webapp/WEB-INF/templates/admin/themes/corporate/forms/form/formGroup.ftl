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
-->
<#macro formGroup id='' formStyle='horizontal' groupStyle='' class='' rows=1 labelKey='' labelKeyDesc='' labelFor='' labelId='' labelClass='' helpKey='' mandatory=false hideLabel=[] collapsed=false params='' deprecated...>
<@deprecatedWarning args=deprecated />    
<#if groupStyle = 'success'>
    <#local validation = 'is-valid'>
<#elseif groupStyle='error'>
    <#local validation = 'is-invalid'>
</#if>
<#if collapsed><#local class += ' collapse' /></#if>
<div class="<#if formStyle != 'inline' && formStyle != 'col'>form-group<#elseif formStyle == 'col'>col-sm-12 <#else>col-lg</#if><#if formStyle='horizontal' || formStyle='fullwidth' > mb-3</#if><#if formStyle='floating'> form-floating</#if><#if class!=''> ${class?trim}</#if><#if validation?? && validation!=''> ${validation}</#if>"<#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>>
<#local displayLabelClass = displaySettings( hideLabel, 'inline-flex') />
<#local labelClass = labelClass >
<#if rows=1>
    <#if labelKey!='' && formStyle='horizontal'>
        <#local labelClass += 'form-label'>
        <#if displayLabelClass?contains('d-none')>
            <#local divClass='col'>
        <#else>
            <#local divClass = 'col-lg-6'>
        </#if>
    <#elseif formStyle = 'inline'>
        <#local divClass = 'mb-2 mr-sm-2'>
        <#local labelClass += ' mr-2' />
    <#elseif formStyle = 'floating'>
        <#local labelClass += ' form-label floating'>
        <#local divClass = 'form-floating'>
    <#elseif formStyle = 'fullwidth'>
		<#local labelClass += ' col-12 col-md-2 form-label text-end' />
		<#local divClass = 'col-md-10' />    
    <#else>
        <#local divClass='col-sm-12 offset-lg-3 col-lg-6'>
    </#if>
<#else>
    <#local labelClass += ' form-label'>
    <#local divClass = 'col-sm-12'>
</#if>
<#if labelKey!='' && formStyle != 'floating'>
    <@formLabel class=labelClass?trim labelFor=labelFor labelKeyDesc=labelKeyDesc labelId=labelId labelKey=labelKey hideLabel=hideLabel mandatory=mandatory />
</#if>
<#assign propagateMandatory = mandatory>
<#nested>
<#assign propagateMandatory = false>
<#if labelKey!='' && formStyle='floating'>
    <@formLabel class=labelClass?trim labelFor=labelFor labelKeyDesc=labelKeyDesc labelId=labelId labelKey=labelKey hideLabel=hideLabel mandatory=mandatory />
</#if>
<#if helpKey!=''><small class="text-muted<#if formStyle!='inline'> form-text</#if>" <#if labelFor!=''>aria-describedby="${labelFor}"</#if>>${helpKey}</small></#if>
</div>
</#macro>
