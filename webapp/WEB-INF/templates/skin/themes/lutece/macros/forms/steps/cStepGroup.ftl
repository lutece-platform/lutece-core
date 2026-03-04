<#-- Macro: cStepGroup

Description: Defines a macro that show a step group

Parameters:
@param - title - string - optional - required - the title of the group. If empty , the "fieldest" is replaced with a div and not title is shown. 
@param - help - string - optional - 
@param - class - string - optional - the CSS class of the element, default '' 
@param - id - string - optional - the ID of the element, default ''
@param - value - string - optional - the value of the element, default ''
@param - iterable - boolean - optional - Add box to the checkbox, default false 
@param - iteration - number - optional - Number of iteration default 0
@param - iterationMax - number - optional - Number of max iteration possible default 10
@param - labelAddIteration - string - optional - Label to add an iteration, default '#i18n{portal.theme.labelAdd}'
@param - labelDelIteration - string - optional - Label to remove an iteration, default '#i18n{portal.theme.labelDelete}'
@param - headerParams - string - optional - additional HTML attributes to include in the header of step group element default ''
@param - isFieldset - boolean - optional - whether to use a fieldset or a div as parent element, default true
@param - noFieldsetTitleLevel - number - optional - the level of the title when not using a fieldset, default 3	
@param - titleClass - string - optional - the CSS class of the title element, default 'h3'
@param - params - string - optional - additional HTML attributes to include in the parent block element default ''
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