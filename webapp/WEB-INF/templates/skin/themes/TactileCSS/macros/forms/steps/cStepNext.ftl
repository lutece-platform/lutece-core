<#--
Macro: cStepNext

Description: Generates a banner for a future (upcoming) step in a multi-step form, displaying the step number and title in a dimmed state.

Parameters:
- step (string, required): the step number.
- title (string, required): the title of the step.
- titleLevel (number, optional): HTML heading level for the title tag. Default: 2.
- class (string, optional): CSS class for the step section. Default: ''.
- id (string, optional): the ID of the element. Default: ''.
- params (string, optional): additional HTML attributes. Default: ''.

Showcase:
- desc: "Étape suivante - @cStepNext"
- newFeature: false

Snippet:

    Next step banner:

    <@cStepNext step='3' title='Payment details' />

    Next step with custom heading level:

    <@cStepNext step='4' title='Confirmation' titleLevel=3 />

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