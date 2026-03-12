<#--
Macro: cStepCurrent

Description: Generates the current active step in a multi-step form, including title, navigation buttons (next, previous, save, backup), and mandatory field warnings.

Parameters:
- step (string, required): the step number.
- title (string, required): the title of the step.
- showTitle (boolean, optional): displays the step title header. Default: true.
- titleLevel (number, optional): HTML heading level for the title tag. Default: 2.
- actionNextStep (string, optional): action name for the next step button. Default: ''.
- titleNextStep (string, optional): title attribute for the next step button. Default: ''.
- labelNextStep (string, optional): label of the next step button. Default: '#i18n{portal.theme.labelNextStep}'.
- actionPrevStep (string, optional): action name for the previous step button. Default: ''.
- titlePrevStep (string, optional): title attribute for the previous step button. Default: ''.
- labelPrevStep (string, optional): label of the previous step button. Default: '#i18n{portal.theme.labelPrevStep}'.
- actionSaveStep (string, optional): action name for the save step button. Default: ''.
- titleSaveStep (string, optional): title attribute for the save step button. Default: ''.
- labelSaveStep (string, optional): label of the save step button. Default: '#i18n{portal.theme.labelSaveStep}'.
- actionSaveForBackUpStep (string, optional): action name for the save-for-backup button. Default: ''.
- titleSaveForBackUpStep (string, optional): title attribute for the save-for-backup button. Default: ''.
- labelForBackUpStep (string, optional): label of the save-for-backup button. Default: '#i18n{forms.step.saveResponse}'.
- actionResetBackUpStep (string, optional): action name for the reset backup button. Default: ''.
- titleResetBackUpStep (string, optional): title attribute for the reset backup button. Default: ''.
- labelResetBackUpStep (string, optional): label of the reset backup button. Default: '#i18n{forms.step.resetResponse}'.
- showPrevStep (boolean, optional): displays the previous step button. Default: true.
- hasSteps (boolean, optional): displays step number in the header. Default: true.
- hasMandatory (boolean, optional): adds mandatory field warnings. Default: true.
- class (string, optional): CSS class for the step section. Default: ''.
- params (string, optional): additional HTML attributes. Default: ''.

Showcase:
- desc: "Étape courante - @cStepCurrent"
- newFeature: false

Snippet:

    Current step with next and previous navigation:

    <@cStepCurrent step='2' title='Contact information' actionNextStep='action_doNextStep' actionPrevStep='action_doPrevStep'>
        <@cField label='Email' for='email' required=true>
            <@cInput name='email' id='email' type='email' />
        </@cField>
        <@cField label='Phone' for='phone'>
            <@cInput name='phone' id='phone' type='tel' />
        </@cField>
    </@cStepCurrent>

    First step (no previous button):

    <@cStepCurrent step='1' title='Personal information' actionNextStep='action_doNextStep' showPrevStep=false>
        <@cField label='Name' for='name' required=true>
            <@cInput name='name' id='name' />
        </@cField>
    </@cStepCurrent>

-->
<#macro cStepCurrent step title showTitle=true titleLevel=2 actionNextStep='' titleNextStep='' labelNextStep='#i18n{portal.theme.labelNextStep}' actionPrevStep='' titlePrevStep='' labelPrevStep='#i18n{portal.theme.labelPrevStep}' actionSaveStep='' titleSaveStep='' labelSaveStep='#i18n{portal.theme.labelSaveStep}' actionSaveForBackUpStep='' titleSaveForBackUpStep='' labelForBackUpStep='#i18n{forms.step.saveResponse}' actionResetBackUpStep='' titleResetBackUpStep='' labelResetBackUpStep='#i18n{forms.step.resetResponse}' showPrevStep=true hasSteps=true hasMandatory=true class='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<@cSection id='current_step' class='step step-current ${class!}' params=params >
<#if showTitle>
<@cSection class='step-title'>
	<@cContainer>
		<#if hasSteps>
			<@cTitle class='title' level=titleLevel params='data-step="${step}" title="${title} - #i18n{portal.theme.labelCurrentStep}" aria-current="step"'>
				<@cText type='span' class='step-number'>${step}</@cText>
				<@cText type='span'>${title}</@cText>
			</@cTitle>
		<#else>
			<@cTitle class='title no-step' params='title="${title} - #i18n{portal.theme.labelCurrentStep}"'>${title}</@cTitle>
		</#if>
	</@cContainer>
</@cSection>
</#if>
<@cContainer>
<@cSection class="step-content">
	<#if hasMandatory>
		<@cFormRow>
			<@cCol class="visually-hidden">
				<@cText class="mandatory-warning">#i18n{portal.theme.msgMandatory}</@cText>
			</@cCol>
		</@cFormRow>
	</#if>
	<#nested>
	<#if hasMandatory>
		<@cFormRow>
			<@cCol cols='12 col-md-10'>
				<@cText class="mandatory-warning">#i18n{portal.theme.msgMandatory}</@cText>
			</@cCol>
		</@cFormRow>
	</#if>
	<@cFormRow>	
		<@cCol cols='12' class='d-flex justify-content-end mb-l'>
			<@chList class='list-unstyled d-flex justify-content-end'>
			<#if actionNextStep !=''>
				<#assign paramsNextStep> name="${actionNextStep}"<#if titleNextStep !=''> title="${titleNextStep}"</#if></#assign>
				<@chItem>
					<@cBtn class='primary btn-action ms-sm order-5' id=actionNextStep params=paramsNextStep label=labelNextStep />
				</@chItem>
			</#if>    
			<#if actionSaveStep !=''>    
				<#assign paramsSaveStep> name="${actionSaveStep}" formnovalidate<#if titleSaveStep !=''> title="${titleSaveStep}"</#if></#assign>
				<@chItem>
					<@cBtn class='primary btn-action order-2' id=actionSaveStep params=paramsSaveStep label=labelSaveStep />
				</@chItem>
			</#if>
			<#if actionPrevStep !='' && showPrevStep >
				<#assign paramsPrevStep> name="${actionPrevStep}" formnovalidate<#if titlePrevStep !=''> title="${titlePrevStep}"</#if></#assign>
				<@chItem>
					<@cBtn class='tertiary order-1' id=actionPrevStep params=paramsPrevStep label=labelPrevStep />
				</@chItem>
			</#if> 
			<#if actionSaveForBackUpStep !=''>
				<#assign paramsSaveForBackUpStep> name="${actionSaveForBackUpStep}" formnovalidate<#if titleSaveForBackUpStep !=''> title="${titleSaveForBackUpStep}"</#if></#assign>
				<@chItem>
					<@cBtn class='primary btn-action ms-sm order-3' id=actionSaveForBackUpStep params=paramsSaveForBackUpStep label='#i18n{portal.theme.labelSaveStep}' />
				</@chItem>
			</#if>
			<#if actionResetBackUpStep !=''>    
				<#assign paramsResetBackUpStep> name="${actionResetBackUpStep}" formnovalidate<#if titleResetBackUpStep !=''> title="${titleResetBackUpStep}"</#if></#assign>
				<@chItem>
					<@cBtn class='secondary ms-sm order-4' id=actionResetBackUpStep params=paramsResetBackUpStep label=labelResetBackUpStep />
				</@chItem>
			</#if>
			</@chList>
		</@cCol>
	</@cFormRow>
</@cSection>
</@cContainer>
</@cSection>
<#if step?number gt 1 >
<script>
window.addEventListener('DOMContentLoaded', (event) => {
	const current = document.querySelector('#current_step');
	const y = current.offsetTop - 180;
	window.scrollBy(0, y);
	
	const formValidateButton = document.querySelector('#${actionNextStep!}');
		formValidateButton.addEventListener('click', (e) => {
            const invalids = document.querySelectorAll('.form-control:invalid','.form-control:user-invalid');
			const arrInvalids = Array.prototype.slice.call(invalids);
			arrInvalids.forEach( function( invalid ){
				invalid.classList.add('is-invalid')
				invalid.setAttribute('aria-invalid', 'true');
				const isRequired = invalid.getAttributeNode('required'); 
				if( isRequired !='' ){
					let pInvalid = document.createElement("p");
					pInvalid.classList.add( 'invalid-feedback' );
					pInvalid.setAttribute( 'role', 'alert' );
					<#assign invalidLabel>#i18n{portal.theme.msgMandatory}</#assign>
					pInvalid.innerHTML = `${invalidLabel}`;
					if (invalid.closest('.input-group') != null) {
						invalid.closest('.input-group').after(pInvalid);
					} else {
						invalid.after(pInvalid);
					}
				}
			});
		})
});
</script> 
</#if>
</#macro>