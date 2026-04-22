<#--
Macro: cInput

Description: Generates a form input field with support for various types, validation, help/error messages, datalist, and phone number formatting.

Parameters:
 - id - string - optional - l'ID du champs de formulaire
 - class - string - optional - ajoute une classe CSS au champs de formulaire (par défaut: 'form-control')
 - name - string - required - permet de définir la valeur de l'attribut 'name' du champs du formulaire
 - type - string - optional - permet de définir la valeur de l'attribut 'type' du champs du formulaire
 - size - string - optional - permet d'ajouter un suffixe à la classe CSS 'form-control', valeur possible 'lg' ou 'sm'
 - value - string - optional - permet de définir la valeur par défaut du champs du formulaire
 - placeholder - string - optional - permet de définir la valeur de l'attribut 'placeholder' du champs du formulaire
 - phoneCountry - string - optional - En complément de l'attribut type à la valeur 'tel', permet de formatter le numéro de téléphone entré au format du pays. Actuellement, seul le format français est pris en charge (+33 X XX XX XX XX). (par défaut: 'FR')
 - required - boolean - optional - permet d'indiquer si le champs est obligatoire (par défaut: false)
 - html5Required - boolean - optional - permet d'indiquer si le champs doit utliser l'attribut html5 required (par défaut: true)
 - disabled - boolean - optional - permet d'indiquer si le champs est desactivé (par défaut: false)
 - readonly - boolean - optional - permet d'indiquer si le champs est lecture seule (par défaut: false)
 - pattern - string - optional - permet de saisir une expression réguliére pour contrôler le champs
 - accept - string - optional - permet de définir l'attribut 'accept' pour les types de fichiers du champs
 - ariaLabel - string - optional - permet de définir l'attribut 'aria-label' pour le champs
 - autocomplete- string - Default '' , autocomplete pour l'input https://developer.mozilla.org/fr/docs/Web/HTML/Attributes/autocomplete 
 - title - string - optional - permet de définir l'attribut 'title' du champs
 - maxlength - number - optional - permet de saisir une limitation de taille de valeur saisie (par défaut: 0)
 - min - number - optional - permet de saisir une valeur minimale pour les champs de type number (par défaut: 0)
 - max - number - optional - permet de saisir une valeur maximale pour les champs de type number (par défaut: 0)
 - step - number - optional - permet de saisir une valeur de step pour les champs de type number (par défaut: 0) ou time (permet de gérer la saisie en millisecondes 300 = 5 minutes) https://developer.mozilla.org/en-US/docs/Web/HTML/Element/input/time#step )
 - title - string - optional - permet de définir l'attribut 'list' du champs
 - datalistId - string - optional - permet de définir l'attribut 'list' du champs
 - datalist - string - optional - permet de définir la balise 'datalist' du champs
 - helpMsg - string - optional - permet de définir le message d'aide du champs
 - errorMsg - string - optional - permet de définir le message d'erreur du champs
 - hideErrorMsg - boolean - optional - true permet de masquer le message d'erreur du champs si jamais il est afficher par exemple par le fieldset
 - params - string - optional - permet d'ajouter des parametres HTML au champs de formulaire

Showcase:
- desc: "Champ texte - @cInput"
- bs: "forms/form-control"
- newFeature: false

Snippet:

    Basic text input:

    <@cInput name='firstname' placeholder='Enter your first name' />

    Email input with validation:

    <@cInput name='email' type='email' required=true helpMsg='We will never share your email.' errorMsg='Please enter a valid email address.' />

    Phone number input with French formatting:

    <@cInput name='phone' type='tel' phoneCountry='FR' placeholder='06 12 34 56 78' />

    Number input with min/max:

    <@cInput name='quantity' type='number' min=1 max=100 value='1' />

-->
<#macro cInput name class='form-control' id='' type='text' size='' value='' placeholder='' phoneCountry='FR' required=false html5Required=true disabled=false readonly=false pattern='' ariaLabel='' autocomplete='' accept='' title='' maxlength=0 min=0 max=0 step=0 datalistId='' datalist='' helpMsg='' errorMsg='' hideErrorMsg=false params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#local isRequired = ( propagateRequired?? && propagateRequired) || required />
<#local hasError = (errorMsg != '')>
<#assign idMsg><#if id!=''>${id}<#else>${name!}</#if></#assign>
<#if helpMsg !=''><@cFormHelp idMsg helpMsg /></#if>
<#if errorMsg !='' && errorMsg !='_error'><@cFormError idMsg errorMsg errorClass /></#if>
<input type="${type!}" class="<#if class!=''> ${class!}</#if><#if size!=''> form-control-${size!}</#if><#if errorMsg!=''> is-invalid</#if><#if type == 'tel' && phoneCountry == 'FR'> fr-number</#if>" name="${name!}" id="<#if id!=''>${id}<#else>${name!}</#if>" value="${value!}"<#if placeholder!=''> placeholder="${placeholder!}"<#if ariaLabel!=''> aria-label="${ariaLabel!}"</#if></#if><#if autocomplete!=''> autocomplete="${autocomplete!}"</#if><#if title!=''> title="${title}"</#if><#if maxlength &gt; 0> maxlength="${maxlength}"</#if><#if disabled> disabled</#if><#if readonly> readonly</#if><#if params!=''> ${params}</#if><#if pattern!=''> pattern="${pattern}"</#if><#if accept !=''> accept="${accept}"</#if><#if min!=0> min="${min}"</#if><#if max!=0> max="${max}"</#if><#if step!=0> step="${step}"</#if><#if isRequired><#if html5Required> required</#if> aria-required="true"</#if><#if datalistId!=''> list="${datalistId!}"</#if><#if hasError> aria-invalid="true" aria-describedby="error_${idMsg!}"<#elseif helpMsg!=''> aria-describedby="help_${idMsg!}"</#if>>
<#local errorClass><#if hideErrorMsg>visually-hidden</#if></#local>
<#nested>
<#if datalistId !=''>
<datalist id="${datalistId}">
<#if datalist !=''><#list datalist as dl><option value="${dl.id}">${dl.label}</option></#list></#if>
</datalist>
</#if>
<#if type == 'tel' && phoneCountry == 'FR'>
<script>
document.addEventListener('DOMContentLoaded', function() {
  document.querySelectorAll('.fr-number').forEach(function(element) {
    element.addEventListener('blur', function() {
      var num = element.value;
      var numFormatte = num
        .replace(/^0(\d)(\s?)(\d{2})(\s?)(\d{2})(\s?)(\d{2})(\s?)(\d{2})$/, '+33 $1 $3 $5 $7 $9')
        .replace(/^\+33(\d{9})$/, function(match, p1) {
          return '+33 ' + p1.replace(/(\d)(\d{2})(\d{2})(\d{2})(\d{2})/, '$1 $2 $3 $4 $5');
        });
      element.value = numFormatte;
    });
  });
});
</script>
</#if>
<#if type == 'number' && ( min gt 0 || max gt 0 )>
<script>
function setInvalidMaxMinMessage( input ) {
  const value = parseFloat(input.value);
  const min = input.hasAttribute('min') ? parseFloat(input.getAttribute('min')) : -Infinity;
  const max = input.hasAttribute('max') ? parseFloat(input.getAttribute('max')) : Infinity;
  if ( value <= min || value >= max  ) {
    if ( !input.classList.contains('is-invalid') ) {
        input.classList.add('is-invalid');
        if( input.parentNode.classList.contains('input-group') ) {
            input.parentNode.insertAdjacentHTML('beforebegin', '<p class="invalid-feedback" >${i18n('portal.theme.labelQuantityInvalid',input.min,input.max)}</p>' );
        } else {
            input.insertAdjacentHTML('beforebegin', '<p class="invalid-feedback" >${i18n('portal.theme.labelQuantityInvalid',input.min,input.max)}</p>' );
        }
        input.setAttribute('aria-invalid', 'true');
        input.setAttribute('aria-describedby', 'error_' + input.id);
    }
    return false;
  } else {
    if ( input.classList.contains('is-invalid') ) {
        input.classList.remove('is-invalid');
        input.removeAttribute('aria-invalid');
        input.removeAttribute('aria-describedby');
        // Remove the error message if it exists
        const errorMsg = input.closest('.col').querySelector('.invalid-feedback'); 
        if (errorMsg) {
          errorMsg.remove();
        }
    }
    return true;
  }
}

document.addEventListener('DOMContentLoaded', function() {
  const input${idMsg} = document.getElementById('${idMsg}');
  if ( input${idMsg}  ) {

      input${idMsg}.addEventListener('blur', function() {
        setInvalidMaxMinMessage( input${idMsg} );
      });
    
      input${idMsg}.closest('form').addEventListener('submit', function(e) {
        const value = parseFloat(input${idMsg}.value);
        if ( input${idMsg} && input${idMsg}.closest('.col').style.display !== 'none' && input${idMsg}.value !== '' ) {
          if( !setInvalidMaxMinMessage( input${idMsg} ) ) {
            input${idMsg}.focus();
            e.preventDefault();
          }
        }
      });
   }
});
</script>
</#if>
</#macro>