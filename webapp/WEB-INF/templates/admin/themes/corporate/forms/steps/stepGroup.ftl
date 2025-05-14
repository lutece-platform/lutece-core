<#-- Macro: cStepGroup

Description: Defines a macro that show a step group

Parameters:
@param - title - string - optional - required - the title of the group
@param - help - string - optional - 
@param - class - string - optional - the CSS class of the element, default '' 
@param - id - string - optional - the ID of the element, default ''
@param - value - string - optional - the value of the element, default ''
@param - iterable - boolean - optional - Add box to the checkbox, default false 
@param - iteration - number - optional - Number of iteration default 0
@param - iterationMax - number - optional - Number of max iteration possible default 10
@param - labelAddIteration - string - optional - Label to add an iteration, default '#i18n{themeparisfr.labelAdd}'
@param - labelDelIteration - string - optional - Label to remove an iteration, default '#i18n{themeparisfr.labelDelete}'
@param - headerParams - string - optional - additional HTML attributes to include in the header of step group element default ''
@param - params - string - optional - additional HTML attributes to include in the parent block element default ''
-->
<#macro cStepGroup title iterable=false iteration=0 iterationMax=10 labelAddIteration='#i18n{themeparisfr.labelAdd}' labelDelIteration='#i18n{themeparisfr.labelDelete}' headerParams='' help='' class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<@row>
	<@columns>
		<@div class='container'>
			<@fieldSet class='w-100 flex-fill row step-group ${class!}' fieldsetId=id fieldsetParams=params  >
				<#local legendClass>col-12 <#if iterable && iteration gt 0>d-flex justify-content-between align-items-center</#if></#local>
				<#local legend>
					<@span class='h3 title main-info-color'>${title!}<#if iteration gt 0> (${iteration+1})</#if></@span>
					<#if iterable && iteration gt 0>
					<@button label='' class='danger btn-mini me-sm mt-0' params='name="action_removeIteration" value="rm_${iteration}"' >
						<@icon style='trash' /><@span class='btn-label'>${labelDelIteration}</@span>
					</@button>
					</#if>
				</#local>
				<@cLegend label=legend class=legendClass params=headerParams />
				<#if help!=''>
					<@row>
						<@columns>${help}</@columns>
					</@row>
				</#if>
				<#nested>
				<#if iterable && iteration lte iterationMax>
					<@row>
						<@columns class='d-flex justify-content-end'>
							<@button label='' class='primary ms-m' params='name="action_addIteration" value="add_${iteration}"'>
								<@icon style='plus' /><@span class='btn-label'>${labelAddIteration}</@span>
							</@button>
						</@columns>
					</@row>
				</#if>
			</@fieldSet>
		</@div>
	</@columns>
</@row>
</#macro>