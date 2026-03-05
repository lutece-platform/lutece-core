<#--
Macro: cStepGroup

Description: Generates a group of fields within a step, optionally iterable with add/remove controls, using either a fieldset or a div container.

Parameters:
- title (string, required): the title of the group. If empty, a div is used instead of a fieldset.
- iterable (boolean, optional): enables add/remove iteration controls. Default: false.
- iteration (number, optional): the current iteration number. Default: 0.
- iterationMax (number, optional): maximum number of iterations allowed. Default: 10.
- labelAddIteration (string, optional): label for the add iteration button. Default: '#i18n{portal.theme.labelAdd}'.
- labelDelIteration (string, optional): label for the delete iteration button. Default: '#i18n{portal.theme.labelDelete}'.
- headerParams (string, optional): additional HTML attributes for the group header. Default: ''.
- isFieldset (boolean, optional): uses a fieldset element as parent. Default: false.
- noFieldsetTitleLevel (number, optional): heading level when not using a fieldset. Default: 3.
- titleClass (string, optional): CSS class for the title element. Default: 'h3'.
- help (string, optional): help content displayed in an accordion. Default: ''.
- class (string, optional): CSS class for the group container. Default: ''.
- id (string, optional): the ID of the element. Default: ''.
- params (string, optional): additional HTML attributes. Default: ''.

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
<#macro cStepGroup title iterable=false iteration=0 iterationMax=10 labelAddIteration='#i18n{portal.theme.labelAdd}' labelDelIteration='#i18n{portal.theme.labelDelete}' headerParams='' isFieldset=false noFieldsetTitleLevel=3 titleClass='h3' help='' class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#if isFielset>
	<@cFormRow>
		<@cSection class='w-100 flex-fill step-group ${class!}' id=id params=params >
			<@cContainer>
				<@cRow>
					<#local headerClass>col-12 px-3 <#if iterable && iteration gt 0>d-flex justify-content-between</#if></#local>
					<#local legendTitle>${title!} <#if iteration gt 0>(${iteration+1})</#if></#local>
					<@cFieldset legend=legendTitle class=headerClass params=headerParams >
					<#if iterable && iteration gt 0>
						<@cSection type='div' class='text-right'>
							<@cBtn label='' class='danger btn-mini ms-m' params='name="action_removeIteration" value="rm_${iteration}"' >
								<@parisIcon name='trash' /><@cInline class='btn-label'>${labelDelIteration}</@cInline>
							</@cBtn>
						</@cSection>
					</#if>
					<#if help!=''>
						<@cRow>
							<@cCol>
								<@cAccordion id=iteration title='${i18n( "theme.msgHelpAbout", title )}' class='outline my-l' >
								${help}
								</@cAccordion>
							</@cCol>
						</@cRow>
					</#if>
					<#nested>
					<#if iterable && iteration lte iterationMax>
						<@cRow>
							<@cCol class='px-4 px-sm-5 d-flex justify-content-end'>
								<@cBtn label='' class='mini ms-m' params='name="action_addIteration" value="add_${iteration}"'>
									<@parisIcon name='plus' /><@cInline class='btn-label'>${labelAddIteration}</@cInline>
								</@cBtn>
							</@cCol>
						</@cRow>
					</#if>
				</@cFieldset>
				</@cRow>
			</@cContainer>
		</@cSection>
	</@cFormRow>
<#else>
	<@cFormRow>
		<@cSection class='w-100 flex-fill step-group ${class!}' id=id params=params >
			<@cContainer>
				<@cRow>
					<#local headerClass>col-12 px-3 <#if iterable && iteration gt 0>d-flex justify-content-between</#if></#local>
					<@cSection type='div' class=headerClass params=headerParams >
						
						<@cTitle level=3 class='title main-color'>${title!} <#if iteration gt 0>(${iteration+1})</#if></@cTitle>
						<#if iterable && iteration gt 0>
							<@cSection type='div' class='text-right'>
								<@cBtn label='' class='danger btn-mini ms-m' params='name="action_removeIteration" value="rm_${iteration}"' >
									<@parisIcon name='trash' /><@cInline class='btn-label'>${labelDelIteration}</@cInline>
								</@cBtn>
							</@cSection>
						</#if>
					</@cSection>
				</@cRow>
				<#if help!=''>
					<@cFormRow>
						<@cCol>
							<@cAccordion id=iteration title='${i18n( "theme.msgHelpAbout", title )}' class='outline my-l' >
							${help}
							</@cAccordion>
						</@cCol>
					</@cFormRow>
				</#if>
				<#nested>
				<#if iterable && iteration lte iterationMax>
					<@cFormRow>
						<@cCol class='px-4 px-sm-5 d-flex justify-content-end'>
							<@cBtn label='' class='mini ms-m' params='name="action_addIteration" value="add_${iteration}"'>
								<@parisIcon name='plus' /><@cInline class='btn-label'>${labelAddIteration}</@cInline>
							</@cBtn>
						</@cCol>
					</@cFormRow>
				</#if>
			</@cContainer>
		</@cSection>
	</@cFormRow>
</#if>
</#macro>