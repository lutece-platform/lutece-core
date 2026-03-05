<#--
Macro: cFormCheck

Description: Generates a form check element (checkbox, radio, or toggle button) with label, validation, and optional selection box styling.

Parameters:
- name (string, required): the name of the element.
- label (string, required): the label associated to the input.
- type (string, required): the type of check, can be 'checkbox', 'radio' or 'button'.
- class (string, optional): the CSS class of the element. Default: 'form-check'.
- id (string, optional): the ID of the element. Default: ''.
- value (string, optional): the value of the element. Default: ''.
- btnClass (string, optional): CSS class for the button label, only used if type is 'button'. Default: ''.
- labelClass (string, optional): the CSS class of the label. Default: ''.
- selectionButton (boolean, optional): adds a selection box around the checkbox. Default: false.
- selectionLabel (string, optional): label for the selection box. Default: ''.
- nestedContent (string, optional): additional content to display inside the selection box. Default: ''.
- textCenter (boolean, optional): centers text on all selections. Default: false.
- errorMsg (string, optional): content of the error message. Default: ''.
- helpMsg (string, optional): content of the help message. Default: ''.
- inline (boolean, optional): sets inline display. Default: false.
- disabled (boolean, optional): disables the element. Default: false.
- readonly (boolean, optional): sets the element as readonly. Default: false.
- checked (boolean, optional): checks the element. Default: false.
- required (boolean, optional): sets element as required. Default: false.
- html5Required (boolean, optional): uses the HTML5 required attribute. Default: true.
- showRequiredLabel (boolean, optional): shows the required asterisk on the wrapping label (true) or on the input label (false). Default: true.
- params (string, optional): additional HTML attributes. Default: ''.

Snippet:

    Basic checkbox usage:

    <@cFormCheck name='accept_terms' label='I accept the terms and conditions' type='checkbox' />

    Radio button usage:

    <@cFormCheck name='gender' label='Male' type='radio' value='male' />
    <@cFormCheck name='gender' label='Female' type='radio' value='female' />

    Toggle button style:

    <@cFormCheck name='newsletter' label='Subscribe to newsletter' type='button' btnClass='btn btn-outline-primary' />

    With selection box:

    <@cFormCheck name='plan' label='Premium plan' type='checkbox' selectionButton=true selectionLabel='Recommended'>
        <p>Includes all features</p>
    </@cFormCheck>

-->
<#macro cFormCheck name label type class='form-check' id='' value='' btnClass='' labelClass='' selectionButton=false selectionLabel='' nestedContent='' textCenter=false errorMsg='' helpMsg='' inline=false disabled=false readonly=false checked=false required=false html5Required=true showRequiredLabel=true params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#local cType><#if type='button'>checkbox<#else>${type}</#if></#local>
<#local cId><#if id=''>${name}-${random()}<#else>${id}</#if></#local>
<#local idMsg><#if id!=''>${id}<#else>${name!}</#if></#local>
<#if helpMsg !=''><@cFormHelp idMsg helpMsg /></#if>
<#if type !='button'>
<div class="<#if class='form-check'>form-check<#else> ${class}</#if><#if selectionButton> btn-selection</#if><#if inline> form-check-inline</#if><#if errorMsg!=''> is-invalid</#if><#if labelClass?contains('visually-hidden')> hidden-label</#if>">
<#if selectionButton><div class="selection-check"></#if>
    <input type="${cType}" id="${cId!}" name="${name!}" class="form-check-input" <#if value!=''>value="${value}"</#if><#if errorMsg !=''> aria-invalid="true"</#if><#if disabled> disabled</#if><#if required><#if html5Required> required</#if> aria-required="true"</#if><#if readonly> readonly</#if><#if checked> checked</#if><#if params!=''> ${params!}</#if>>
    <#local cFCLabel>${label!} <#nested></#local>
    <#local cFCClass>form-check-label<#if labelClass!=''> ${labelClass}</#if><#if textCenter> w-100 justify-content-center</#if></#local>
    <#local cFCRequired=required />
    <#if !showRequiredLabel><#local cFCRequired=false /></#if>
    <@cLabel label=cFCLabel class=cFCClass for=cId required=cFCRequired/>
<#if selectionButton></div></#if>
<#if !selectionButton>${nestedContent}</#if>
<#if selectionLabel!=''><p class="selection-subtitle ms-m my-sm<#if textCenter> text-center</#if>">${selectionLabel}</p></#if>
<#if selectionButton && nestedContent?has_content><div class="selection-content">${nestedContent}</div></#if>
</div>
<#else>
    <input type="${cType!'checkbox'}" name="${name!}" class="btn-check<#if class!=''> ${class}</#if><#if errorMsg!=''> is-invalid</#if>" id="${cId!?replace(',','-')}" autocomplete="off"<#if value!=''> value="${value}"</#if><#if errorMsg !=''>= aria-invalid="true"</#if><#if disabled> disabled</#if><#if required> required</#if><#if readonly> readonly</#if><#if checked> checked</#if><#if params!=''> ${params!}</#if>>
    <#local cFCClass><#if btnClass!=''>${btnClass}<#else>btn btn-outline-primary</#if></#local>
    <#local cFCRequired=required />
    <#if !showRequiredLabel><#local cFCRequired=false /></#if>
    <@cLabel label=label! class=cFCClass for=cId required=cFCRequired  />
</#if>  
<#if errorMsg !=''><@cFormError idMsg errorMsg /></#if>
</#macro>