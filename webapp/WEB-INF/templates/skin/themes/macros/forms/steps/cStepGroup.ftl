<#--
Macro: cStepGroup

Description: Generates a group of fields within a step, optionally iterable with add/remove controls, using either a fieldset or a div container.

Parameters:
- title (string, required): the title of the group. If empty, a div is used instead of a fieldset.
- iterable (boolean, optional): enables add/remove iteration controls. Default: false.
- iteration (number, optional): the current iteration number. Default: 0.
- iterationMax (number, optional): maximum number of iterations allowed. Default: 10.
- labelAddIteration (string, optional): label for the add iteration button. Default: '#i18n{portal.portal.theme.labelAdd}'.
- labelDelIteration (string, optional): label for the delete iteration button. Default: '#i18n{portal.portal.theme.labelDelete}'.
- headerParams (map, optional): additional HTML attributes of the group header, e.g. {'data-x': 'y'}. Default: {}.
- isFieldset (boolean, optional): uses a fieldset element as parent. Default: false.
- noFieldsetTitleLevel (number, optional): heading level when not using a fieldset. Default: 3.
- titleClass (string, optional): CSS class for the title element. Default: 'h3'.
- help (string, optional): help content displayed in an accordion. Default: ''.
- class (string, optional): CSS class for the group container. Default: ''.
- id (string, optional): the ID of the element. Default: ''.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

Showcase:
- desc: "Groupe d'étapes - @cStepGroup"
- newFeature: false

Snippet:

    Basic step group:

    <@cStepGroup title='Address'>
        <@cField label='Street' for='street' required=true>
            <@cInput name='street' id='street' />
        </@cField>
        <@cField label='City' for='city' required=true>
            <@cInput name='city' id='city' />
        </@cField>
    </@cStepGroup>

    Iterable step group (repeatable section):

    <@cStepGroup title='Child' iterable=true iteration=0 iterationMax=5>
        <@cField label='First name' for='child_firstname' required=true>
            <@cInput name='child_firstname' id='child_firstname' />
        </@cField>
        <@cField label='Date of birth' for='child_dob'>
            <@cInputDate name='child_dob' label='' id='child_dob' />
        </@cField>
    </@cStepGroup>

-->
<#macro cStepGroup title iterable=false iteration=0 iterationMax=10 labelAddIteration='#i18n{portal.portal.theme.labelAdd}' labelDelIteration='#i18n{portal.portal.theme.labelDelete}' headerParams={} isFieldset=true noFieldsetTitleLevel=3 titleClass='h3' help='' class='' id='' params={} deprecated...>
<@deprecatedWarning args=deprecated />
<#local cId><#if !id?has_content>id-${random()}<#else>${id}</#if></#local>
<#local groupTitle>${title!}<#if iteration gt 0> (${iteration+1})</#if></#local>
<#local toolbar>
<#if iterable && ( iteration gt 0 || iteration lte iterationMax )>
<div class="step-group-toolbar d-flex justify-content-end mt-0">
	<#if iteration gt 0>
	<@cBtn label='' class='danger me-sm mt-0' params={"name": "action_removeIteration", "value": "rm_${iteration}"} >
		<@cIcon name='trash' /><@cInline class='btn-label'>${labelDelIteration}</@cInline><@cInline class='visually-hidden'> ${groupTitle}</@cInline>
	</@cBtn>
	</#if>
	<#if iteration lte iterationMax>
	<@cBtn label='' class='primary' params={"name": "action_addIteration", "value": "add_${iteration}"}>
		<@cIcon name='plus' /><@cInline class='btn-label'>${labelAddIteration}</@cInline><@cInline class='visually-hidden'> ${title!}</@cInline>
	</@cBtn>
	</#if>
</div>
</#if>
</#local>
<#local helpParams = help?has_content?then( { 'aria-describedby': 'help_' + cId?markup_string }, {} ) />
<@cBlock class='step-group'>
<#if title?has_content>
	<#if isFieldset>
		<@cFieldset class='w-100 flex-fill ${class!}' id=cId params=params + helpParams >
		<@cLegend label='' id='legend-${cId}' params=headerParams + {'tabindex': '-1'}>
			<@cInline class='${titleClass} group-title'>${groupTitle}</@cInline>
		</@cLegend>
		${toolbar}
		<#if help?has_content>
			<@cRow>
				<@cCol id='help_${cId}'>${help}</@cCol>
			</@cRow>
		</#if>
		<#nested>
		</@cFieldset>
	<#else>
		<@cBlock class='w-100 flex-fill ${class!}' id=cId params=params + helpParams >
			<@cTitle level=noFieldsetTitleLevel class='${titleClass} group-title' params=headerParams + {'tabindex': '-1'}>${groupTitle}</@cTitle>
			${toolbar}
			<#if help?has_content>
				<@cRow>
					<@cCol id='help_${cId}'>${help}</@cCol>
				</@cRow>
			</#if>
			<#nested>
		</@cBlock>
	</#if>
<#else>
	<@cBlock class='w-100 flex-fill ${class!}' id=cId params=params >
		<#if help?has_content><@cRow><@cCol>${help}</@cCol></@cRow></#if>
		<#nested>
	</@cBlock>
</#if>
</@cBlock>
</#macro>