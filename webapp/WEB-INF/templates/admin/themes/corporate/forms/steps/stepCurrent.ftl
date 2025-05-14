<#-- Macro: stepCurrent

Description: Defines a macro that show the current step

Parameters:
@param - step - string - optional - required - Step number
@param - title - string - optional - required - the title of the step
@param - showTitle - boolean - optional - Show step title, default true 
@param - titleLevel - number - optional - HTML level of the title tag, default 2
@param - class - string - optional - the CSS class of the element, default '' 
@param - actionNextStep - string - optional - If set add a next step button, default ''
@param - titleNextStep - string - optional - If set add a title for next step button, default ''
@param - labelNextStep - string - optional - Label of the next button '#i18n{themeparisfr.labelNextStep}'
@param - actionPrevStep - string - optional - If set add a previous step button, default ''
@param - titlePrevStep - string - optional - If set add a title for prev step button, default ''
@param - labelPrevStep - string - optional - Label of the previous step button, default '#i18n{themeparisfr.labelPrevStep}'
@param - actionSaveStep - string - optional - If set add a save step button, default ''
@param - titleSaveStep - string - optional - If set add a title for save step button, default ''
@param - labelSaveStep - string - optional - Label of the save step button , default '#i18n{themeparisfr.labelSaveStep}'
@param - actionSaveForBackUpStep - string - optional - If set add a save step for backup button, default ''
@param - titleSaveForBackUpStep - string - optional - If set add a title for "SaveForBackUpStep" step button, default ''
@param - labelSaveForBackUpStep - string - optional - Label of the save backup button, default '#i18n{forms.step.saveResponse}'
@param - actionResetBackUpStep - string - optional - If set add a reset backup button, default ''
@param - titleResetBackUpStep - string - optional - If set add a title for reset backup step button, default ''
@param - labelResetBackUpStep - string - optional - Label of the reset backup button, default '#i18n{forms.step.resetResponse}'
@param - showPrevStep - boolean - optional - Show previous step button, default true 
@param - hasSteps - boolean - optional - Show step infos in header of step, default true 
@param - hasMandatory - boolean - optional - Add mandatory warning, default true 
@param - params - string - optional - additional HTML attributes to include in the parent block element default ''
-->
<#macro stepCurrent step title showTitle=true titleLevel=3 actionNextStep='' titleNextStep='' labelNextStep='#i18n{forms.step.next}' actionPrevStep='' titlePrevStep='' labelPrevStep='#i18n{forms.step.previous}' actionSaveStep='' titleSaveStep='' labelSaveStep='#i18n{forms.step.save}' actionSaveForBackUpStep='' titleSaveForBackUpStep='' labelForBackUpStep='#i18n{forms.step.saveResponse}' actionResetBackUpStep='' titleResetBackUpStep='' labelResetBackUpStep='#i18n{forms.step.resetResponse}' showPrevStep=true hasSteps=true hasMandatory=true class='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<@div id='current_step' class='step step-current ${class!}' params=params >
	<#if showTitle>
	<@div class='step-title'>
		<#if hasSteps>
			<@h class='title' level=titleLevel params='data-step="${step}" title="${title} - #i18n{themeparisfr.labelCurrentStep}" aria-current="step"'>
				<@span class='step-number'>${step}</@span>
				<@span>${title}</@span>
			</@h>
		<#else>
			<@h level=titleLevel class='title no-step' params='title="${title} - #i18n{themeparisfr.labelCurrentStep}"'>${title}</@h>
		</#if>
	</@div>
	</#if>
	<@div class='step-content'>
		<#if hasMandatory><@p class='mandatory-warning visually-hidden'>#i18n{themeparisfr.msgMandatory}</@p></#if>
		<#nested>
		<#if hasMandatory>
			<@row>
				<@columns xs=12 md=10>
					<@p class="mandatory-warning">#i18n{themeparisfr.msgMandatory}</@p>
				</@columns>
			</@row>
		</#if>
		<@row>	
			<@columns class='d-flex justify-content-center justify-content-sm-end'>
				<@ul class='list-unstyled d-flex justify-content-end flex-column flex-sm-row align-items-center'>
				<#if actionNextStep !=''>
					<#assign paramsNextStep> name="${actionNextStep}"<#if titleNextStep !=''> title="${titleNextStep}"</#if></#assign>
					<@li class='order-5'>
						<@button type='submit' id=actionNextStep params=paramsNextStep title=labelNextStep />
					</@li>
				</#if>    
				<#if actionSaveStep !=''>    
					<#assign paramsSaveStep> name="${actionSaveStep}" formnovalidate<#if titleSaveStep !=''> title="${titleSaveStep}"</#if></#assign>
					<@li class='order-5'>
						<@button type='submit' id=actionSaveStep params=paramsSaveStep title=labelSaveStep />
					</@li>
				</#if>
				<#if actionPrevStep !='' && showPrevStep >
					<#assign paramsPrevStep> name="${actionPrevStep}" formnovalidate<#if titlePrevStep !=''> title="${titlePrevStep}"</#if></#assign>
					<@li class='order-2'>
						<@button type='submit' id=actionPrevStep params=paramsPrevStep title=labelPrevStep />
					</@li>
				</#if> 
				</@ul>
			</@columns>
		</@row>
	</@div>
</@div>
<script>
window.addEventListener('DOMContentLoaded', (event) => {
<#if step?number gt 1 >
	<#assign actionStep><#if actionNextStep !=''>${actionNextStep!}<#else>${actionSaveStep!}</#if></#assign>
	const formValidateButton = document.querySelector('#${actionStep!}');
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
				<#assign invalidLabel>#i18n{themeparisfr.msgMandatory}</#assign>
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
		btnReset.classList.add('btn', 'btn-secondary', 'ms-sm');
		btnReset.innerHTML = 'Supprimer la sauvergarde';
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
});
</script> 
</#macro>