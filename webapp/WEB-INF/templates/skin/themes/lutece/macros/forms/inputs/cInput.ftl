<#-- Macro: cInput

Description: permet de définir un champs de formulaire.

Parameters:

@param - id - string - optional - l'ID du champs de formulaire
@param - class - string - optional - ajoute une classe CSS au champs de formulaire (par défaut: 'form-control')
@param - name - string - required - permet de définir la valeur de l'attribut 'name' du champs du formulaire
@param - type - string - optional - permet de définir la valeur de l'attribut 'type' du champs du formulaire
@param - size - string - optional - permet d'ajouter un suffixe à la classe CSS 'form-control', valeur possible 'lg' ou 'sm'
@param - value - string - optional - permet de définir la valeur par défaut du champs du formulaire
@param - placeholder - string - optional - permet de définir la valeur de l'attribut 'placeholder' du champs du formulaire
@param - phoneCountry - string - optional - En complément de l'attribut type à la valeur 'tel', permet de formatter le numéro de téléphone entré au format du pays. Actuellement, seul le format français est pris en charge (+33 X XX XX XX XX). (par défaut: 'FR')
@param - required - boolean - optional - permet d'indiquer si le champs est obligatoire (par défaut: false)
@param - html5Required - boolean - optional - permet d'indiquer si le champs doit utliser l'attribut html5 required (par défaut: true)
@param - disabled - boolean - optional - permet d'indiquer si le champs est desactivé (par défaut: false)
@param - readonly - boolean - optional - permet d'indiquer si le champs est lecture seule (par défaut: false)
@param - pattern - string - optional - permet de saisir une expression réguliére pour contrôler le champs
@param - accept - string - optional - permet de définir l'attribut 'accept' pour les types de fichiers du champs
@param - autocomplete- string - Default '' , autocomplete pour l'input https://developer.mozilla.org/fr/docs/Web/HTML/Attributes/autocomplete 
@param - title - string - optional - permet de définir l'attribut 'title' du champs
@param - maxlength - number - optional - permet de saisir une limitation de taille de valeur saisie (par défaut: 0)
@param - min - number - optional - permet de saisir une valeur minimale pour les champs de type number (par défaut: 0)
@param - max - number - optional - permet de saisir une valeur maximale pour les champs de type number (par défaut: 0)
@param - title - string - optional - permet de définir l'attribut 'list' du champs
@param - datalistId - string - optional - permet de définir l'attribut 'list' du champs
@param - datalist - string - optional - permet de définir la balise 'datalist' du champs
@param - helpMsg - string - optional - permet de définir le message d'aide du champs
@param - errorMsg - string - optional - permet de définir le message d'erreur du champs
@param - params - string - optional - permet d'ajouter des parametres HTML au champs de formulaire
-->
<#macro cInput name class='form-control' id='' type='text' size='' value='' placeholder='' phoneCountry='FR' required=false html5Required=true disabled=false readonly=false pattern='' autocomplete='' accept='' title='' maxlength=0 min=0 max=0 datalistId='' datalist='' helpMsg='' errorMsg='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#local hasError = (errorMsg != '')>
<#assign idMsg><#if id!=''>${id}<#else>${name!}</#if></#assign>
<#if helpMsg !=''><@cFormHelp idMsg helpMsg /></#if>
<input type="${type!}" class="<#if class!=''> ${class!}</#if><#if size!=''> form-control-${size!}</#if><#if errorMsg!=''> is-invalid</#if><#if type == 'tel' && phoneCountry == 'FR'> fr-number</#if>" name="${name!}" id="<#if id!=''>${id}<#else>${name!}</#if>" value="${value!}"<#if placeholder!=''> placeholder="${placeholder!}" aria-label="${placeholder!}"</#if><#if autocomplete!=''> autocomplete="${autocomplete!}"</#if><#if title!=''> title="${title}"</#if><#if maxlength &gt; 0> maxlength="${maxlength}"</#if><#if disabled> disabled</#if><#if readonly> readonly</#if><#if params!=''> ${params}</#if><#if pattern!=''> pattern="${pattern}"</#if><#if accept !=''> accept="${accept}"</#if><#if min!=0> min="${min}"</#if><#if max!=0> max="${max}"</#if><#if required><#if html5Required> required</#if> aria-required="true"</#if><#if datalistId!=''> list="${datalistId!}"</#if><#if hasError> aria-invalid="true" aria-describedby="error_${idMsg!}"<#elseif helpMsg!=''> aria-describedby="help_${idMsg!}"</#if>>
<#if errorMsg !='' && errorMsg !='_error'><@cFormError idMsg errorMsg /></#if>
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
</#macro>