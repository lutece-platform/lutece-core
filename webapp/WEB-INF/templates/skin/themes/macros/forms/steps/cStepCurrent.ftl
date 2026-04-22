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
<#macro cStepCurrent step title showTitle=true titleLevel=2 titleClass='h3' actionNextStep='' titleNextStep='' labelNextStep='#i18n{portal.theme.labelNextStep}' actionPrevStep='' titlePrevStep='' labelPrevStep='#i18n{portal.theme.labelPrevStep}' actionSaveStep='' titleSaveStep='' labelSaveStep='#i18n{portal.theme.labelSaveStep}' actionSaveForBackUpStep='' titleSaveForBackUpStep='' labelForBackUpStep='#i18n{portal.theme.labelSaveResponse}' actionResetBackUpStep='' titleResetBackUpStep='' labelResetBackUpStep='#i18n{portal.theme.labelResetResponse}' showPrevStep=true hasSteps=true hasMandatory=true class='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#local stepClass><#if !showTitle> step-no-title</#if></#local>
<@cSection id='current_step' class='step step-current ${class!}${stepClass!}' params=params >
<#if showTitle>
<@cBlock class='step-title'>
	<@cContainer>
		<#if hasSteps>
			<@cTitle class='title ${titleClass}' level=titleLevel params='data-step="${step}" title="${title} - #i18n{portal.theme.labelCurrentStep}" aria-current="step" tabindex="0"'>
				<@cText type='span' class='step-number'>${step}</@cText>
				<@cText type='span'>${title?replace('- hidden','')}</@cText>
			</@cTitle>
		<#else>
			<@cTitle level=titleLevel class='title no-step' params='title="${title?replace(\'- hidden\',\'\')}- #i18n{portal.theme.labelCurrentStep}" tabindex="0"'>
				${title} <@cText type='span' class='step-number visually-hidden'>${step}</@cText>
			</@cTitle>
		</#if>
	</@cContainer>
</@cBlock>
<#else>
<@cTitle level=titleLevel class='step-title visually-hidden' params='title="${title?replace(\'- hidden\',\'\')}- #i18n{portal.theme.labelCurrentStep}" tabindex="0"'>
	${title} <@cText type='span' class='step-number'>${step}</@cText>
</@cTitle>
</#if>
<@cBlock class='step-content'>
	<@cContainer>
	<#if hasMandatory><@cText class="mandatory-warning visually-hidden">#i18n{portal.theme.msgMandatory}</@cText></#if>
	<#nested>
	</@cContainer>
</@cBlock>
<@cBlock class='step-footer'>
	<@cContainer>
		<#if hasMandatory><@cText class='mandatory-warning ms-xs'>#i18n{portal.theme.msgMandatory}</@cText></#if>
		<@cBlock class='step-current-toolbar d-flex justify-content-center justify-content-sm-end'>
			<@chList class='list-unstyled d-flex justify-content-end flex-column flex-sm-row align-items-center mt-0 me-xs'>
			<#if actionPrevStep !='' && showPrevStep >
				<#assign paramsPrevStep> name="${actionPrevStep}" formnovalidate<#if titlePrevStep !=''> title="${titlePrevStep}"</#if></#assign>
				<@chItem>
					<@cBtn class='tertiary ms-sm' id=actionPrevStep params=paramsPrevStep label=labelPrevStep />
				</@chItem>
			</#if> 
			<#if actionSaveForBackUpStep !=''>
				<#assign paramsSaveForBackUpStep> name="${actionSaveForBackUpStep}" formnovalidate<#if titleSaveForBackUpStep !=''> title="${titleSaveForBackUpStep}"</#if></#assign>
				<@chItem>
					<@cBtn class='tertiary ms-sm' id=actionSaveForBackUpStep params=paramsSaveForBackUpStep label='#i18n{portal.theme.labelSaveStep}' />
				</@chItem>
			</#if>
			<#if actionResetBackUpStep !=''>    
				<#assign paramsResetBackUpStep> name="${actionResetBackUpStep}" formnovalidate<#if titleResetBackUpStep !=''> title="${titleResetBackUpStep}"</#if></#assign>
				<@chItem id='reset-backup' >
					<@cBtn class='tertiary  ms-sm' id=actionResetBackUpStep params=paramsResetBackUpStep label=labelResetBackUpStep />
				</@chItem>
			</#if>
			<#if actionNextStep !=''>
				<#assign paramsNextStep> name="${actionNextStep}"<#if titleNextStep !=''> title="${titleNextStep}"</#if></#assign>
				<@chItem>
					<@cBtn class='primary ms-sm' id=actionNextStep params=paramsNextStep label=labelNextStep />
				</@chItem>
			</#if>
			<#if actionSaveStep !=''>    
				<#assign paramsSaveStep> name="${actionSaveStep}" <#if titleSaveStep !=''> title="${titleSaveStep}"</#if></#assign>
				<@chItem>
					<@cBtn class='secondary' id=actionSaveStep params=paramsSaveStep label=labelSaveStep />
				</@chItem>
			</#if>  
			</@chList>
		</@cBlock>
	</@cContainer>
</@cBlock>
<@cModal title='#i18n{portal.theme.labelRemoveSaveStep}' dismissLabel='#i18n{portal.theme.labelDismissSaveStep}' id='reset-step' footer='<button class="btn btn-primary" id="modal-reset-backup" type="button"><span class="btn-label">#i18n{portal.theme.labelRemoveSaveStepInfo}</span></button>' />
<script>
window.addEventListener('DOMContentLoaded', (event) => {
	/* Error Scroll                           */
	// Add smooth scrolling to all links
	const invalidElements = document.querySelectorAll('.step-current .step-content .is-invalid');
	const currentStepId = document.querySelector('#current_step .step-title .step-number').textContent.trim();
	if ( invalidElements.length > 0 ) {
		const firstInvalidElements = invalidElements[0];
		const invalidElementStatusMsg = `<p class="visually-hidden" id="step-alert" tabindex="-1">#i18n{portal.theme.statusMsgStepValidationErrors}</p>`
		document.querySelector('.step-current .step-content .container').insertAdjacentHTML( 'afterbegin', invalidElementStatusMsg );
		document.querySelector('#step-alert').focus();	
		firstInvalidElements.scrollIntoView({ behavior: 'smooth', block: 'center' });
		switch(firstInvalidElements.tagName.toLowerCase()) {
			case 'legend':
				firstInvalidElements.parentElement.querySelector('input').focus();
				break;
			case 'div':
				firstInvalidElements.querySelector('input').focus();
				break;
			default:
				firstInvalidElements.focus();
				break;
		}
	} else if ( !isNaN(parseInt(currentStepId) ) && parseInt( currentStepId ) > 1 ) {
		const current = document.querySelector('#current_step');
		const y = current.offsetTop - 220;
		window.scrollBy(0, y);
		// Déplacer le focus sur le titre de l'étape courante
		// pour assurer sa restitution par les technologies d'assistance.
		const stepTitle = document.querySelector('.step-current .step-title .title');
		if ( stepTitle ) {
			stepTitle.focus();
		}
	}
	<#assign actionStep><#if actionNextStep !=''>${actionNextStep!}<#else>${actionSaveStep!}</#if></#assign>
	const formValidate = document.getElementById('form-validate'), formValidateButton = document.getElementById('${actionStep!}');
	<#if step?number gt 1 >
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
</#if>
	const saveStep = document.getElementById('save-step-status');
	if(saveStep){
		const at = saveStep.querySelector('.alert-title');
		const btnReset = document.createElement('button');
		btnReset.classList.add('btn', 'btn-tertiary', 'ms-sm');
		btnReset.innerHTML = "${'#i18n{portal.theme.labelRemoveSaveStep}'?js_string}";
		btnReset.setAttribute('type', 'button');
		btnReset.setAttribute('data-bs-target', '#reset-stepModal');
		btnReset.setAttribute('data-bs-toggle', 'modal');
		at.appendChild(btnReset);

		const btnModalReset = document.querySelector('#modal-reset-backup');
		btnModalReset.addEventListener('click', (e) => {
			const resetBackup = document.getElementById('${actionResetBackUpStep}');
			resetBackup.click();
		});
	}

	const btnValidateHidden = '<button class="visually-hidden" name="${actionStep}" aria-hidden="true" tabindex="-1" ></button>'
	formValidate.insertAdjacentHTML('afterbegin', btnValidateHidden );
});
</script> 
<script type="module" src="${commonsSharedThemePath}${commonsSiteJsModulesPath}theme-form-validation.js"></script>
</@cSection>
</#macro>