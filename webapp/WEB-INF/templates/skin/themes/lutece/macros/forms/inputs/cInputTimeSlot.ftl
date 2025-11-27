<#-- Macro: cInputTimeSlot                               
Parameters:
@param - name - string - required - Nom du champ
@param - legend - string - Default '', Titre/légende du fieldset                   
@param - id - string - Default '', Id de l'input                           
@param - class - string - Default '', classe css à ajouter à l'input
@param - legendClass - string - Default '', classe css à ajouter à la légende du fieldset
@param - beginHour - string - Default '00:00', Heure minimum de début par défaut                        
@param - endHour - string - Default '23:59', Heure de maximum fin par défaut       
@param - inputParams - string - Default '', Paramètres supplémentaires pour les inputs                 
@param - step - number - Default 0, Le pas est la valeur d'incrémentation ou de décrémentation de la valeur du champ. Seules les valeurs qui sont des incréments en multiple de step depuis la valeur de base (min si cet attribut est défini, value sinon, et si aucun n'est fourni, une valeur par défaut appropriée) sont valides. Pour les champs de type time, la valeur de l'attribut step est exprimée en secondes (avec un facteur de multiplication de 1000 puisque la valeur numérique sous-jacente est exprimée en millisecondes). Par défaut, la valeur de l'incrément est 60, ce qui correspond à 1 minute.  https://developer.mozilla.org/fr/docs/Web/HTML/Reference/Elements/input/time#step
@param - btnAction - string - Default '[name="action_doSaveStep"]', si non vide ajoute un trigger sur le bouton de validation du formulaire pour traitement par le polyfill.       
@param - autocomplete - string - Default '', autocomplete pour l'input https://developer.mozilla.org/fr/docs/Web/HTML/Attributes/autocomplete       
@param - html5Required - boolean - Default true, attribut required HTML5                 
@param - required - boolean - Default false, champ obligatoire ou non                 
@param - disabled - boolean - Default false, champ désactivé ou non
@param - readonly - boolean - Default false, champ en lecture seule ou non                
@param - helpMsg - string  - Default '', Message d'aide pour l'input                                   
@param - errorMsg - string - Default '', Message d'erreur pour l'input                      
@param - params - string - Default '', Tous autres paramètres à ajouter au fieldset

@sample : 
 -->
<#macro cInputTimeSlot name legend='' legendClass='' beginHour='00:00' endHour='23:59' step=0 btnAction='[name="action_doSaveStep"]' autocomplete='' html5Required=false required=false disabled=false readonly=false helpMsg='' errorMsg='' hideErrorMsg=true id='' class='form-control' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#local idLocal><#if id!=''>${id}<#else>${name!}</#if></#local>
<#local inputParams>min="${beginHour!}" max="${endHour!}"</#local>
<@cFieldset legend=legend! legendClass=legendClass id=idLocal! required=required helpMsg=helpMsg params=params >
	<@cRow>
		<@cCol cols='6 col-md-4 col-lg-2 ps-0 ms-xs'>
			<@cField label='#i18n{themeparisfr.labelFrom}' id='label_${name}_begin!' for='${name}_begin!' required=required showLabel=false > 
				<@cInput type='time' class=class!'' id='${idLocal}_begin' name='${name}_begin' autocomplete=autocomplete required=required html5Required=html5Required value=beginHour! disabled=disabled! readonly=readonly! errorMsg=errorMsg! hideErrorMsg=hideErrorMsg step=step?number params=inputParams />
			</@cField>	
		</@cCol>
		<@cCol cols='6 col-md-4 col-lg-2'>
			<@cField label='#i18n{themeparisfr.labelTo}' id='label_${name}_end!' for='${name}_end!' required=required showLabel=false > 
				<@cInput type='time' class=class!'' id='${idLocal}_end' name='${name}_end' autocomplete=autocomplete required=required html5Required=html5Required value=endHour! disabled=disabled! readonly=readonly! errorMsg=errorMsg! hideErrorMsg=hideErrorMsg step=step?number params=inputParams />
			</@cField>	
		</@cCol>
	</@cRow>
</@cFieldset>
<script src="themes/skin/parisfr/js/themeparisfr.util.min.js"></script>
<script src="themes/skin/parisfr/js/plugins/forms/input-time-polyfill.y11.js"></script>
<script>
document.addEventListener('DOMContentLoaded', function() {
	const browserInfo = getBrowserInfo();
	// Polyfill for IE / FF / Safari
	if ( browserInfo.name.includes( 'IE') || browserInfo.name.includes('Firefox') || browserInfo.name.includes('Safari') ) {
		<#if btnAction !=''>const btnSendStep = document.querySelector('${btnAction}');</#if>
		const timeInputs = document.querySelectorAll('input[type="time"]');
		// Replace time inputs with text inputs with class "time-polyfill"
		timeInputs.forEach( input => {
			input.classList.add('time-polyfill');
			input.type = 'text';
		});

		// Add event listener for keydown event to handle tabbing between inputs
		document.addEventListener('keydown', function( e ) {
			if( e.target.classList.contains('time-polyfill') ) {
				switch (e.keyCode) {
					case 9:
						const inputName = e.target.name;
						const nextInputName = inputName.endsWith('_begin') ? inputName.replace('_begin', '_end') : inputName.replace('_end', '_begin');
						const nextInput = document.querySelector(<#noparse>`input[name="${nextInputName}"]`</#noparse>);
						if (nextInput) {
							nextInput.focus();
						}
						e.stopImmediatePropagation();
						break;
					default:	
						break;
				}
			}
        <#if btnAction !=''>
		// Validate before submitting the form
		if( btnSendStep != null ){
			btnSendStep.on('click', function(e) {
				// Replace time inputs with text inputs with class "time-polyfill"
				timeInputs.forEach( input => {
					input.value = input.value === ('--:--') ? '' : input.value;
				});
			});
		}
        </#if>
		});
	}
});
</script>
</#macro>