<#-- Macro: cStepNext

Description: Defines a macro that show a next step banner

Parameters:
@param - step - string - optional - required - Step number
@param - title - string - optional - required - the title of the step
@param - titleLevel - number - optional - HTML level of the title tag, default 2
@param - class - string - optional - the CSS class of the element, default '' 
@param - id - string - optional - the ID of the element, default ''
@param - params - string - optional - additional HTML attributes to include in the parent block element default ''
-->
<#macro cStepNext step title titleLevel=2 class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#local stepClass>step step-next<#if class!=''>${class}</#if></#local>
<@cSection class=stepClass id=id params=params>
	<@cSection class='step-title'>
		<@cContainer>
			<@cTitle class='title' level=titleLevel params='title="${title}" data-step="${step}"'>
				<@cText type='span' class='step-number'>${step}</@cText>
				<@cText type='span'>${title}</@cText>
			</@cTitle>
		</@cContainer>
	</@cSection>
</@cSection>
</#macro>