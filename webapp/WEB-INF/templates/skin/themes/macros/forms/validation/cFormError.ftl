<#--
Macro: cFormError

Description: Generates an error message element for a form field, displayed with an alert icon and linked via aria-describedby for accessibility.

Parameters:
- id (string, required): the ID of the related field, used to generate the error element ID (error_{id}).
- label (string, required): the error message content.
- class (string, optional): adds a CSS class to the error message. Default: ''.
- params (string, optional): additional HTML attributes. Default: ''.
- attrs (hash, optional): additional HTML attributes as name/value pairs, e.g. {'aria-hidden': 'true'}. Prefer it to params: the macro writes the attribute and its value is escaped like any value of the page.

Showcase:
- desc: "Erreur de formulaire - @cFormError"
- bs: "forms/validation"
- newFeature: false

Snippet:

    Error message for an input field:

    <@cInput name='email' id='email' type='email' errorMsg='Please enter a valid email address.' />

    Standalone error message:

    <@cFormError id='email' label='Please enter a valid email address.' />

-->
<#macro cFormError id label class='' params='' attrs={} deprecated...>
<@deprecatedWarning args=deprecated />
<#local cClass>invalid-feedback<#if class!=''> ${class!}</#if></#local>
<#local cParam><#if params!=''> ${params!}  role="status"</#if></#local>
<@cText class=cClass id="error_${id!}" params=cParam attrs=attrs><@cIcon name='alert-error' class='main-danger-color' />${label!}</@cText>
</#macro>