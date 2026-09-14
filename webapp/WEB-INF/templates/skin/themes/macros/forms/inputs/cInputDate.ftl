<#-- Macro: cInputDate
Description: permet de définir un champs de formulaire de type date avec un datepicker JS ou un champ de type date HTML5.

Parameters:
- name - string - required - Nom du champ
- label - string - required - Label asssocié au champs
- id - string - Default '', Id de l'input
- class - string -  Default 'custom-checkbox', classe css à ajouter à l'input
- type - string - Default 'datepicker', type de l'input par default 'datepicker', sinon cela peut être la valeur 'date' pour un champ de type date HTML5 ou vide si on ne veux pas de datepicker ou de champs date html5.
- icon - boolean - Default true, affiche l'icone "agenda" à droite de l'input
- options - object - Default {} Voir les paramètres possibles disponible pour Vanilla JS Datepicker -https://mymth.github.io/vanillajs-datepicker/#/ -
- value : - string - Default '', Valeur par défaut de l'input
- placeholder- string - Default '' , placeholder de l'input
- autocomplete- string - Default '' , autocomplete pour l'input https://developer.mozilla.org/fr/docs/Web/HTML/Attributes/autocomplete
- required - boolean - Default false, champ obligatoire ou non
- disabled - boolean - Default false, champ désactivé ou non
- readonly - boolean - Default false, champ en lecture seule ou non
- helpMsg - string - Default '', Message d'aide pour l'input
- errorMsg - string - Default '', Message d'erreur pour l'input
- params - string - Default '', Tous autres paramètres à ajouter à l'input
- separator - boolean - Default true, Séparateur auto-inséré lors de la saisie en fonction du format de date. Si vrai, le séparateur est inséré automatiquement après le jour et le mois en fonction du format de date. De plus, si la date saisie est invalide ou incomplète, le focus est conservé dans le champ à la sortie, l'attribut aria-invalid est positionné et le message d'erreur (invalid-feedback) est affiché ; la soumission du formulaire est bloquée tant que la date est invalide (y compris avec type='').
- #nested - string - Default '', Contenu textuel a ajouter après l'input


Showcase:
- desc: "Champ date - @cInputDate"
- bs: "forms/form-control"
- newFeature: false

Snippet:

    Basic datepicker:

    <@cInputDate name='birthdate' label='Date of birth' id='birthdate' />

    Datepicker with default value and no icon:

    <@cInputDate name='event_date' label='Event date' id='event_date' value='2026-01-15' icon=false />

    HTML5 date input:

    <@cInputDate name='start_date' label='Start date' id='start_date' type='date' />

    Datepicker in a row:

    <@cRow>
        <@cCol cols='4'>
            <@cInputDate id='date_start' label='From' name='date_start' />
        </@cCol>
        <@cCol cols='4'>
            <@cInputDate id='date_end' label='To' name='date_end' />
        </@cCol>
    </@cRow>

-->
<#macro cInputDate name id='' label='' class='' type='datepicker' icon=true options={} value='' placeholder='' autocomplete='' html5Required=false required=false disabled=false readonly=false helpMsg='' errorMsg='' separator=true params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#local idLocal><#if id!=''>${id}<#else>${name!}</#if></#local>
<#local typeLocal><#if type='date'>date<#else>text</#if></#local>
<#local valLocal><#if value !=''>${value}<#elseif value='now'>.now?date?iso_utc</#if></#local>
<#local errorInput><#if errorMsg !=''>_error</#if></#local>
<#local inputClass><#if errorMsg !=''>is-invalid</#if></#local>
<#local isRequired = ( propagateRequired?? && propagateRequired) || required />
<#local isHtml5Required = ( typeLocal='date' && isRequired ) || html5Required />
<#if label!=''><@cLabel label=label for='${idLocal}' /></#if>
<#if helpMsg !=''><@cFormHelp idLocal helpMsg /></#if>
<#if errorMsg !='' && errorMsg !='_error'><@cFormError idLocal errorMsg /></#if>
<@cInputGroup>
  <@cInput id=idLocal type=typeLocal name=name value=valLocal placeholder=placeholder autocomplete=autocomplete required=isRequired html5Required=isHtml5Required disabled=disabled readonly=readonly errorMsg=errorInput params=params />
  <#if icon && type='datepicker'>
  <@cInputGroupAddonText>
    <@cIcon name='calendar' id=idLocal />
  </@cInputGroupAddonText>
  </#if>
  <#nested>
</@cInputGroup>
<#if type='datepicker'><@getThemeDatePicker idField=idLocal options=options /></#if>
<#if separator && type != 'date'>
<@cInputDateTypingSeparator idField=idLocal hasDatepicker=(type='datepicker') />
</#if>
</#macro>
<#-- Macro: cInputDateTypingSeparator (interne)
Description: attache la saisie assistée à un input date : insertion automatique du
séparateur pendant la frappe ("01" -> "01/") d'après le format de date de la locale.
Utilisé par cInputDate et cInputDateRange ; doit être appelé après getThemeDatePicker.

Parameters:
- idField - string - required - Id de l'input cible
- hasDatepicker - boolean - Default true, vrai si l'input est géré par le datepicker JS
         (la validation est alors gérée par le patch global de initThemeDatePicker.ftl),
         faux pour un champ texte seul (validation et maintien du focus gérés ici)
-->
<#macro cInputDateTypingSeparator idField hasDatepicker=true>
<script>
(function() {
	<#-- The datepicker (themeDatepickerA11y) replaces the original input by a visible
	     clone on DOMContentLoaded, so the listeners must be attached after its init -->
	function initDateTyping() {
		const el = document.getElementById('${idField}');
		if (!el || el.type === 'hidden') return;
		// Detect separator and format from locale (e.g. "31/12/2026" → sep="/", parts=[2,2,4])
		const yr = new Date().getFullYear();
		const lang = document.documentElement.lang || navigator.language;
		const localeSample = new Date(yr, 11, 31).toLocaleDateString(lang);
		const sep = localeSample.replace(/[0-9]/g, '').charAt(0) || '/';
		const partLengths = localeSample.split(sep).map(function(p) { return p.length; });
		// Localized format hint, e.g. "JJ/MM/AAAA" (fr) / "DD/MM/YYYY" (en)
		const fmtLetters = '#i18n{portal.theme.error.invalidDateFormat.letters}';
		const fmtHint = localeSample.replace('31', (fmtLetters.charAt(0) || 'J').repeat(2))
			.replace('12', (fmtLetters.charAt(1) || 'M').repeat(2))
			.replace(String(yr), (fmtLetters.charAt(2) || 'A').repeat(4));
		if (el.placeholder === '') {
			el.placeholder = fmtHint.toLowerCase();
		}
		// Format digits, inserting the separator as soon as a part is complete ("01" → "01/")
		function formatWithSep(digits) {
			let result = '', di = 0;
			for (let i = 0; i < partLengths.length; i++) {
				const chunk = digits.substring(di, di + partLengths[i]);
				if (chunk.length === 0) break;
				if (i > 0) result += sep;
				result += chunk;
				di += partLengths[i];
				if (chunk.length === partLengths[i] && i < partLengths.length - 1 && di >= digits.length) {
					result += sep;
					break;
				}
			}
			return result;
		}
		// Track previous value so the user can delete freely
		let prevLen = el.value.length;
		el.addEventListener('input', function() {
			const curLen = el.value.length;
			const isDeleting = curLen < prevLen;
			prevLen = curLen;
			if (isDeleting) return;
			const digits = el.value.replace(/[^0-9]/g, '');
			const result = formatWithSep(digits);
			if (result !== el.value) {
				el.value = result;
				prevLen = result.length;
				el.setSelectionRange(result.length, result.length);
				// Notify listeners (datepicker hidden-field sync, live validation)
				el.dispatchEvent(new Event('input', { bubbles: true }));
			}
		});
<#if hasDatepicker>
		<#-- Validation and focus-keeping are handled globally by the patch in
		     initThemeDatePicker.ftl; only the deletion tracking needs a resync
		     when the datepicker rewrites the value -->
		el.addEventListener('changeDate', function() { prevLen = el.value.length; });
<#else>
		// No datepicker on this field: check the typed value is a real calendar
		// date (rejects 31/02/2026), raise the error message in the HTML and
		// keep focus in the field while it is not valid (same behaviour as the
		// validation patch of initThemeDatePicker.ftl for datepicker fields)
		const dayFirst = localeSample.indexOf('31') < localeSample.indexOf('12');
		const yearFirst = localeSample.indexOf(String(yr)) === 0;
		function isValidDate(str) {
			const parts = str.split(sep);
			if (parts.length !== partLengths.length) return false;
			let d, m, y;
			if (yearFirst) { y = parts[0]; m = parts[1]; d = parts[2]; }
			else if (dayFirst) { d = parts[0]; m = parts[1]; y = parts[2]; }
			else { m = parts[0]; d = parts[1]; y = parts[2]; }
			if (!/^[0-9]+$/.test(d + m + y) || y.length !== 4) return false;
			const dt = new Date(parseInt(y, 10), parseInt(m, 10) - 1, parseInt(d, 10));
			return dt.getFullYear() === parseInt(y, 10)
				&& dt.getMonth() === parseInt(m, 10) - 1
				&& dt.getDate() === parseInt(d, 10);
		}
		const msg = (el.getAttribute('data-date-error') || '#i18n{portal.theme.error.invalidDateFormat}')
			.replace('{0}', fmtHint).replace('{1}', new Date(yr, 5, 30).toLocaleDateString(lang));
		let feedback = null;
		function showError() {
			el.classList.add('is-invalid');
			el.setAttribute('aria-invalid', 'true');
			if (!feedback || !feedback.parentNode) {
				feedback = document.createElement('p');
				feedback.className = 'invalid-feedback';
				feedback.id = 'error_${idField}';
				feedback.innerHTML = '<svg class="paris-icon paris-icon-alert-error main-danger-color" aria-hidden="true" focusable="false" role="img"><use href="#paris-icon-alert-error"></use></svg> ';
				feedback.appendChild(document.createTextNode(msg));
				const group = el.closest('.input-group') || el;
				group.parentNode.insertBefore(feedback, group);
			}
			el.setAttribute('aria-describedby', feedback.id);
		}
		function clearError() {
			el.classList.remove('is-invalid');
			el.removeAttribute('aria-invalid');
			el.removeAttribute('aria-describedby');
			if (feedback && feedback.parentNode) feedback.parentNode.removeChild(feedback);
			feedback = null;
		}
		let touched = false;
		function validate() {
			if (el.value === '' || isValidDate(el.value)) { clearError(); return true; }
			showError();
			return false;
		}
		el.addEventListener('blur', function() {
			touched = true;
			if (validate()) return;
			setTimeout(function() {
				el.focus();
				el.setSelectionRange(0, el.value.length);
			}, 0);
		});
		// Re-validate while typing once the field has been touched, so the
		// message disappears as soon as the value becomes valid again
		el.addEventListener('input', function() { if (touched) validate(); });
		// Validate on submit and block the submission if the date is invalid
		const form = el.closest('form');
		if (form) {
			form.addEventListener('submit', function(e) {
				touched = true;
				if (el.offsetParent !== null && !validate()) {
					e.preventDefault();
					el.focus();
				}
			});
		}
</#if>
	}
	if (document.readyState === 'loading') {
		document.addEventListener('DOMContentLoaded', initDateTyping);
	} else {
		initDateTyping();
	}
})();
</script>
</#macro>
<#-- Macro: cInputDateRange                                
Parameters:
name  : required                                
label : required                                
id : default 'dtRange'                           
class : default ''                
type  : default 'datepicker' : datepicker / date 
icon : boolean default true                      
options : object default {}                      
value : default ''                               
placeholder : Array default ['','']              
required : Array default [false,false]           
disabled : Array default [false,false]           
readonly : Array default [false,false]           
helpMsg : default ''
errorMsg : default ''
params : default ''
separator : boolean default true, séparateur auto-inséré lors de la saisie (voir cInputDate)
#nested
-->
<#macro cInputDateRange name label=['#i8n{theme.labelDateStart}','#i8n{theme.labelDateEnd}'] showLabel=[false,false] id='dtRange' class='' type='datepicker' icon=true options={} value='' placeholder=['',''] required=[false,false]  html5Required=[false,false] disabled=[false,false] readonly=[false,false] helpMsg='' errorMsg='' separator=true params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#local idLocal><#if id!=''>${id}<#else>${name!}</#if></#local>
<#local typeLocal><#if type='date'>date<#else>text</#if></#local>
<#local valLocal><#if value !=''>${value}<#elseif value='now'>.now?date?iso_utc</#if></#local>
<#local isRequired0 = ( propagateRequired?? && propagateRequired) || required[0] />
<#local isRequired1 = ( propagateRequired?? && propagateRequired) || required[1] />
<#local isHtml5Required0 = ( typeLocal='date' && isRequired0 ) || html5Required[0] />
<#local isHtml5Required1 = ( typeLocal='date' && isRequired1 ) || html5Required[1] />
<#assign errorClass = (errorMsg !='')?then('is-invalid','') >
<#if helpMsg !=''><@cFormHelp idLocal helpMsg /></#if>
<#if errorMsg !=''><@cFormError idMsg errorMsg /></#if>
<@cBlock class='daterange ${class!} ${errorClass!}' id='${idLocal}' params=params >
  <@cRow>
    <@cCol>
        <@cLabel label=label[0] for='${idLocal}_range_start' required=isRequired0 class=(showLabel[0]?then('', 'visually-hidden')) />
    </@cCol>
    <@cCol>
        <@cLabel label=label[1] for='${idLocal}_range_end' required=isRequired1 class=(showLabel[1]?then('', 'visually-hidden')) />
    </@cCol>
</@cRow>
<@cRow>
    <@cCol>
        <@cInputGroup>
          <@cInput id='${idLocal}_range_start' type=typeLocal name=name value=valLocal placeholder=placeholder[0] required=isRequired0 html5Required=isHtml5Required0 disabled=disabled[0] readonly=readonly[0]  />
        </@cInputGroup>
    </@cCol>
    <@cCol>
        <@cInputGroup>
            <@cInput id='${idLocal}_range_end' type=typeLocal name='${name}_range_end' placeholder=placeholder[1] required=isRequired1 html5Required=isHtml5Required1 disabled=disabled[1] readonly=readonly[1] />
            <#if icon>
            <@cInputGroupAddon>
                <@cInputGroupAddonText tag='div'>
                    <@cIcon 'agenda' '${idLocal}' />
                </@cInputGroupAddonText>   
            </@cInputGroupAddon> 
            </#if>
        </@cInputGroup>
    </@cCol>
</@cRow>
<#nested>
</@cBlock>
<#local optionsLocal><#if options?size = 0>{inputs:["${idLocal}_range_start","${idLocal}_range_start"]}</#if></#local>
<#if type='datepicker'><@getThemeDatePicker idField='' range=true rangeIdWrapper='${idLocal}' options=options /></#if>
<#if separator && type != 'date'>
<@cInputDateTypingSeparator idField='${idLocal}_range_start' hasDatepicker=(type='datepicker') />
<@cInputDateTypingSeparator idField='${idLocal}_range_end' hasDatepicker=(type='datepicker') />
</#if>
</#macro>