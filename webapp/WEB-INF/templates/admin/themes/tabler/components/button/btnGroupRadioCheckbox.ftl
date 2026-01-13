<#-- Macro: btnGroupRadioCheckbox

Description: Generates a radio button or checkbox within a button group.

Parameters:
- type (string, optional): the type of input, either 'checkbox' or 'radio'.
- color (string, optional): the color of the button, defaults to 'primary'.
- size (string, optional): the size of the button.
- name (string, optional): the name attribute of the input.
- id (string, optional): the ID attribute of the input.
- params (string, optional): additional parameters to add to the input.
- ariaLabel (string, optional): the ARIA label of the input.
- labelFor (string, optional): the ID of the label for the input.
- labelKey (string, optional): the i18n key of the label for the input.
- labelParams (string, optional): additional parameters to add to the label.
- tabIndex (string, optional): the tab index of the input.
- value (string, optional): the value attribute of the input.
- checked (boolean, optional): whether the input is checked by default.
-->
<#macro btnGroupRadioCheckbox type='checkbox' color='primary' size='' name='' id='' params='' ariaLabel='' labelFor='' labelKey='' labelParams='' tabIndex='' value='' checked=false deprecated...>
<@deprecatedWarning args=deprecated />
<label class="btn btn-${color}<#if size!=''> btn-${size}</#if>" for="${labelFor}"<#if labelParams!=''> ${labelParams}</#if>>
<input type="${type}" name="${name}" id="${id}" autocomplete="off"<#if value!=''> value="${value}"</#if><#if params!=''> ${params}</#if><#if tabIndex!=''> tabindex="${tabIndex}"</#if><#if checked> checked</#if> /><#if labelKey!=''><span class="ms-1">${labelKey}<span></#if>
</label>
</#macro>