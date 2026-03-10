<#--
Macro: cInput

Description: Generates a form input field with support for various types, validation, help/error messages, datalist, and phone number formatting.

Parameters:
- name (string, required): the name attribute of the input.
- class (string, optional): CSS class for the input. Default: 'form-control'.
- id (string, optional): the ID of the input. Default: ''.
- type (string, optional): the input type (text, email, tel, number, etc.). Default: 'text'.
- size (string, optional): adds a size suffix to form-control class, 'lg' or 'sm'. Default: ''.
- value (string, optional): the default value. Default: ''.
- placeholder (string, optional): the placeholder text. Default: ''.
- phoneCountry (string, optional): formats phone number for the given country when type is 'tel'. Default: 'FR'.
- required (boolean, optional): marks the field as required. Default: false.
- html5Required (boolean, optional): uses the HTML5 required attribute. Default: true.
- disabled (boolean, optional): disables the input. Default: false.
- readonly (boolean, optional): sets the input as readonly. Default: false.
- pattern (string, optional): a regular expression for input validation. Default: ''.
- autocomplete (string, optional): the autocomplete attribute value. Default: ''.
- accept (string, optional): accepted file types for file inputs. Default: ''.
- title (string, optional): the title attribute. Default: ''.
- maxlength (number, optional): maximum character length. Default: 0.
- min (number, optional): minimum value for number inputs. Default: 0.
- max (number, optional): maximum value for number inputs. Default: 0.
- datalistId (string, optional): ID for the associated datalist element. Default: ''.
- datalist (string, optional): list of objects with id and label for datalist options. Default: ''.
- helpMsg (string, optional): help message displayed below the input. Default: ''.
- errorMsg (string, optional): error message displayed on validation failure. Default: ''.
- params (string, optional): additional HTML attributes. Default: ''.

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