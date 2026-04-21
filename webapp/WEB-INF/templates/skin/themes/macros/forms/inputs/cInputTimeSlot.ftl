<#--
Macro: cInputTimeSlot

Description: Generates a time slot picker with two time inputs (begin and end) wrapped in a fieldset, including browser polyfill support.

Parameters:
- name (string, required): the name attribute prefix for the time inputs (generates {name}_begin and {name}_end).
- legend (string, optional): the legend/title of the fieldset. Default: ''.
- legendClass (string, optional): CSS class for the fieldset legend. Default: ''.
- beginHour (string, optional): default start time value. Default: '00:00'.
- endHour (string, optional): default end time value. Default: '23:59'.
- step (number, optional): step increment in seconds for the time input (e.g., 900 for 15-minute intervals). Default: 0.
- btnAction (string, optional): CSS selector for the form submit button, used by the polyfill. Default: '[name="action_doSaveStep"]'.
- autocomplete (string, optional): the autocomplete attribute value. Default: ''.
- html5Required (boolean, optional): uses the HTML5 required attribute. Default: false.
- required (boolean, optional): marks the fields as required. Default: false.
- disabled (boolean, optional): disables the inputs. Default: false.
- readonly (boolean, optional): sets the inputs as readonly. Default: false.
- helpMsg (string, optional): help message for the fieldset. Default: ''.
- errorMsg (string, optional): error message on validation failure. Default: ''.
- hideErrorMsg (boolean, optional): hides the error message display. Default: true.
- id (string, optional): the ID of the fieldset. Default: ''.
- class (string, optional): CSS class for the time inputs. Default: 'form-control'.
- params (string, optional): additional HTML attributes for the fieldset. Default: ''.

Showcase:
- desc: "Créneau horaire - @cInputTimeSlot"
- newFeature: false

Snippet:

    Basic time slot:

    <@cInputTimeSlot name='appointment' legend='Appointment time' />

    Time slot with custom range and 15-minute steps:

    <@cInputTimeSlot name='meeting' legend='Meeting time' beginHour='08:00' endHour='18:00' step=900 required=true />

    Time slot without legend:

    <@cInputTimeSlot name='slot' beginHour='09:00' endHour='17:00' />

-->
<#macro cInputTimeSlot name legend='' legendClass='' beginHour='00:00' endHour='23:59' step=0 btnAction='[name="action_doSaveStep"]' autocomplete='' html5Required=false required=false disabled=false readonly=false helpMsg='' errorMsg='' hideErrorMsg=true id='' class='form-control' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#local idLocal><#if id!=''>${id}<#else>${name!}</#if></#local>
<#local inputParams>min="${beginHour!}" max="${endHour!}"</#local>
<@cFieldset legend=legend! legendClass=legendClass id=idLocal! required=required helpMsg=helpMsg params=params >
	<@cRow>
		<@cCol cols='6 col-md-4 col-lg-2 ps-0 ms-xs'>
			<@cField label='#i18n{portal.theme.labelFrom}' id='label_${name}_begin!' for='${name}_begin!' required=required showLabel=false > 
				<@cInput type='time' class=class!'' id='${idLocal}_begin' name='${name}_begin' autocomplete=autocomplete required=required html5Required=html5Required value=beginHour! disabled=disabled! readonly=readonly! errorMsg=errorMsg! hideErrorMsg=hideErrorMsg step=step?number params=inputParams />
			</@cField>	
		</@cCol>
		<@cCol cols='6 col-md-4 col-lg-2'>
			<@cField label='#i18n{portal.theme.labelTo}' id='label_${name}_end!' for='${name}_end!' required=required showLabel=false > 
				<@cInput type='time' class=class!'' id='${idLocal}_end' name='${name}_end' autocomplete=autocomplete required=required html5Required=html5Required value=endHour! disabled=disabled! readonly=readonly! errorMsg=errorMsg! hideErrorMsg=hideErrorMsg step=step?number params=inputParams />
			</@cField>	
		</@cCol>
	</@cRow>
</@cFieldset>
<script src="${commonsSharedThemePath}${commonsSiteJsModulesPath}theme-utils.min.js"></script>
<script src="${commonsSiteSharedPath}${commonsSiteJsPath}input-time-polyfill.y11.js"></script>
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