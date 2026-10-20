<#--
Macro: cStepCurrent

Description: Generates the current active step in a multi-step form, including title, navigation buttons (next, previous, save, backup), and mandatory field warnings.

Parameters:
- step (string, required): the step number.
- title (string, required): the title of the step.
- showTitle (boolean, optional): displays the step title header. Default: true.
- titleLevel (number, optional): HTML heading level for the title tag. Default: 2.
- formId -  string - optional - the id of the form to check default 'form-validate' 
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
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

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
<#macro cStepCurrent step title showTitle=true titleLevel=2 titleClass='h3' formId='form-validate' actionNextStep='' titleNextStep='' labelNextStep='#i18n{portal.theme.labelNextStep}' actionPrevStep='' titlePrevStep='' labelPrevStep='#i18n{portal.theme.labelPrevStep}' actionSaveStep='' titleSaveStep='' labelSaveStep='#i18n{portal.theme.labelSaveStep}' actionSaveForBackUpStep='' titleSaveForBackUpStep='' labelForBackUpStep='#i18n{portal.theme.labelSaveResponse}' actionResetBackUpStep='' titleResetBackUpStep='' labelResetBackUpStep='#i18n{portal.theme.labelResetResponse}' showPrevStep=true hasSteps=true hasMandatory=true id='current_step' class='' params={} deprecated...>
<@deprecatedWarning args=deprecated />
<#local stepClass = ( !showTitle )?then( ' step-no-title', '' ) />
<@cSection id='${id}' class='step step-current ${class!}${stepClass!}' params=params >
<#if showTitle>
<@cBlock class='step-title'>
	<@cContainer>
		<#if hasSteps>
			<@cTitle class='title ${titleClass}' level=titleLevel params={"data-step": ( step ), "title": "${title} - #i18n{portal.theme.labelCurrentStep}", "aria-current": "step", "tabindex": "-1"}>
				<#if step?number!=0><@cText type='span' class='step-number'>${step}</@cText></#if>
				<@cText type='span'>${title?replace('- hidden','')}</@cText>
			</@cTitle>
		<#else>
			<@cTitle level=titleLevel class='title no-step' params={"title": "${title?replace('- hidden','')}- #i18n{portal.theme.labelCurrentStep}", "tabindex": "-1"}>
				${title} <@cText type='span' class='step-number visually-hidden'>${step}</@cText>
			</@cTitle>
		</#if>
	</@cContainer>
</@cBlock>
<#else>
<@cTitle level=titleLevel class='step-title visually-hidden' params={"title": "${title?replace('- hidden','')}- #i18n{portal.theme.labelCurrentStep}", "tabindex": "-1"}>
	${title} <@cText type='span' class='step-number'>${step}</@cText>
</@cTitle>
</#if>
<@cBlock class='step-content'>
	<@cContainer>
	<#if hasMandatory><@cText class="mandatory-warning">#i18n{portal.theme.msgMandatory}</@cText></#if>
	<#nested>
	</@cContainer>
</@cBlock>
<@cBlock class='step-footer'>
	<@cContainer>
		<@cBlock class='step-current-toolbar d-flex justify-content-center justify-content-sm-end'>
			<@chList class='list-unstyled d-flex justify-content-end flex-column flex-sm-row align-items-center mt-0 me-xs'>
			<#if actionPrevStep?has_content && showPrevStep >
				<#assign paramsPrevStep = { 'name': actionPrevStep, 'formnovalidate': true } + titlePrevStep?has_content?then( { 'title': titlePrevStep }, {} ) />
				<@chItem>
					<@cBtn class='tertiary ms-sm' id=actionPrevStep params=paramsPrevStep label=labelPrevStep />
				</@chItem>
			</#if> 
			<#if actionSaveForBackUpStep?has_content>
				<#assign paramsSaveForBackUpStep = { 'name': actionSaveForBackUpStep, 'formnovalidate': true } + titleSaveForBackUpStep?has_content?then( { 'title': titleSaveForBackUpStep }, {} ) />
				<@chItem>
					<@cBtn class='tertiary ms-sm' id=actionSaveForBackUpStep params=paramsSaveForBackUpStep label='#i18n{portal.theme.labelSaveStep}' />
				</@chItem>
			</#if>
			<#if actionResetBackUpStep?has_content>    
				<#assign paramsResetBackUpStep = { 'name': actionResetBackUpStep, 'formnovalidate': true } + titleResetBackUpStep?has_content?then( { 'title': titleResetBackUpStep }, {} ) />
				<@chItem id='reset-backup' >
					<@cBtn class='tertiary  ms-sm' id=actionResetBackUpStep params=paramsResetBackUpStep label=labelResetBackUpStep />
				</@chItem>
			</#if>
			<#if actionNextStep?has_content>
				<#assign paramsNextStep = { 'name': actionNextStep } + titleNextStep?has_content?then( { 'title': titleNextStep }, {} ) />
				<@chItem>
					<@cBtn class='primary ms-sm' id=actionNextStep params=paramsNextStep label=labelNextStep />
				</@chItem>
			</#if>
			<#if actionSaveStep?has_content>    
				<#assign paramsSaveStep = { 'name': actionSaveStep } + titleSaveStep?has_content?then( { 'title': titleSaveStep }, {} ) />
				<@chItem>
					<@cBtn class='secondary' id=actionSaveStep params=paramsSaveStep label=labelSaveStep />
				</@chItem>
			</#if>  
			</@chList>
		</@cBlock>
	</@cContainer>
</@cBlock>
<#assign resetFooter><button class="btn btn-primary" id="modal-reset-backup" type="button"><span class="btn-label">#i18n{portal.theme.labelRemoveSaveStepInfo}</span></button></#assign>
<@cModal title='#i18n{portal.theme.labelRemoveSaveStep}' dismissLabel='#i18n{portal.theme.labelDismissSaveStep}' id='reset-step' footer=resetFooter />
<script>
window.addEventListener('DOMContentLoaded', (event) => {
	/* Error Scroll                           */
	// Add smooth scrolling to all links
	const invalidElements = document.querySelectorAll('.step-current .step-content .is-invalid');
	let currentStepId = 0;
	const stepNumberElement = document.querySelector('#current_step .step-title .step-number')
	if ( stepNumberElement != null ){
		currentStepId = document.querySelector('#current_step .step-title .step-number').textContent.trim();
	}
	if ( invalidElements.length > 0 ) {
		const summary = document.createElement( 'div' );
		summary.id = 'step-error-summary';
		summary.className = 'alert alert-outline alert-danger';
		summary.setAttribute( 'tabindex', '-1' );
		const summaryTitle = document.createElement( 'p' );
		summaryTitle.className = 'alert-title';
		summaryTitle.textContent = '${i18n('portal.theme.statusMsgStepValidationErrors')?js_string?no_esc}';
		summary.appendChild( summaryTitle );
		const summaryList = document.createElement( 'ul' );
		document.querySelectorAll( '.step-current .step-content .invalid-feedback[id^="error_"]' ).forEach( function( feedback ) {
			const fieldId = feedback.id.substring( 'error_'.length );
			const field = document.getElementById( fieldId );
			const fieldset = document.getElementById( 'fieldset-' + fieldId ) || ( field ? field.closest( 'fieldset' ) : null );
			const label = document.querySelector( 'label[for="' + CSS.escape( fieldId ) + '"]' ) || ( fieldset ? fieldset.querySelector( 'legend' ) : null );
			const item = document.createElement( 'li' );
			const link = document.createElement( 'a' );
			link.href = '#' + fieldId;
			link.textContent = ( label ? label.textContent.replace( '*', '' ).trim( ) + ' : ' : '' ) + feedback.textContent.trim( );
			link.addEventListener( 'click', function( e ) {
				const target = field || ( fieldset ? fieldset.querySelector( 'input, select, textarea' ) : null );
				if ( target ) {
					e.preventDefault( );
					target.focus( );
				}
			} );
			item.appendChild( link );
			summaryList.appendChild( item );
		} );
		if ( summaryList.children.length > 0 ) {
			summary.appendChild( summaryList );
		}
		document.querySelector( '.step-current .step-content .container' ).insertAdjacentElement( 'afterbegin', summary );
		document.title = '${i18n('portal.theme.labelError')?js_string?no_esc} : ' + document.title;
		summary.scrollIntoView( { behavior: 'smooth', block: 'center' } );
		summary.focus( );
	} else if ( !isNaN(parseInt(currentStepId) ) && parseInt( currentStepId ) > 1 ) {
		const current = document.querySelector('#current_step');
		// Scroll instantané vers l'étape courante après validation de l'étape précédente.
		// getBoundingClientRect().top + scrollY = position absolue fiable dans le document
		// (offsetTop est relatif à l'offsetParent) ; behavior:'instant' force un positionnement
		// immédiat en ignorant le scroll-behavior:smooth défini par le thème.
		const y = current.getBoundingClientRect().top + window.scrollY - 220;
		window.scrollTo({ top: y, behavior: 'instant' });
		// Déplacer le focus sur le titre de l'étape courante
		// pour assurer sa restitution par les technologies d'assistance.
		const stepTitle = document.querySelector('.step-current .step-title .title');
		if ( stepTitle ) {
			stepTitle.focus();
		}
	}
	<#assign actionStep = ( actionNextStep?has_content )?then( ( actionNextStep!'' )?string, ( actionSaveStep!'' )?string ) />
	const formValidate = document.getElementById('${(formId!)?js_string?no_esc}')<#if actionStep?? && actionStep?has_content>,formValidateButton = document.getElementById('${(actionStep!)?js_string?no_esc}')</#if>;
	<#if actionStep?? && actionStep?has_content>
	formValidateButton && formValidateButton.addEventListener('click', (e) => {
		const fields = document.querySelectorAll( '.step-current .step-content input, .step-current .step-content select, .step-current .step-content textarea' );
		fields.forEach( function( field ) {
			if ( !field.id || field.type === 'hidden' || field.type === 'radio' || field.type === 'checkbox' || field.checkValidity( ) ) {
				return;
			}
			field.classList.add( 'is-invalid' );
			field.setAttribute( 'aria-invalid', 'true' );
			const errorId = 'error_' + field.id;
			let feedback = document.getElementById( errorId );
			if ( !feedback ) {
				feedback = document.createElement( 'p' );
				feedback.className = 'invalid-feedback';
				feedback.id = errorId;
				const anchor = field.closest( '.input-group' ) || field;
				anchor.after( feedback );
			}
			feedback.textContent = field.validity.valueMissing ? '${i18n('portal.theme.labelMandatoryField')?js_string?no_esc}' : field.validationMessage;
			const describedBy = ( field.getAttribute( 'aria-describedby' ) || '' ).split( ' ' ).filter( function( token ) { return token && token !== errorId; } );
			field.setAttribute( 'aria-describedby', [ errorId ].concat( describedBy ).join( ' ' ) );
		});
	})
	</#if>
	const saveStep = document.getElementById('save-step-status');
	if(saveStep){
		const at = saveStep.querySelector('.alert-title');
		const btnReset = document.createElement('button');
		btnReset.classList.add('btn', 'btn-tertiary', 'ms-sm');
		btnReset.textContent = "${i18n('portal.theme.labelRemoveSaveStep')?js_string?no_esc}";
		btnReset.setAttribute('type', 'button');
		btnReset.setAttribute('data-bs-target', '#reset-stepModal');
		btnReset.setAttribute('data-bs-toggle', 'modal');
		at.appendChild(btnReset);

		const btnModalReset = document.querySelector('#modal-reset-backup');
		btnModalReset.addEventListener('click', (e) => {
			const resetBackup = document.getElementById('${actionResetBackUpStep?js_string?no_esc}');
			resetBackup.click();
		});
	}

	const btnValidateHidden = '<button class="visually-hidden" name="${actionStep?js_string?no_esc}" aria-hidden="true" tabindex="-1" ></button>'
	formValidate && formValidate.insertAdjacentHTML('afterbegin', btnValidateHidden );
});
</script> 
<script type="module" src="${commonsSharedThemePath}${commonsSiteJsModulesPath}theme-form-validation.js"></script>
</@cSection>
</#macro>