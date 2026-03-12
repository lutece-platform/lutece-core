<#--
Macro: cSelect

Description: Generates a select dropdown element with support for validation, help/error messages, multiple selection, and accessibility attributes.

Parameters:
- name (string, required): the name attribute of the select.
- class (string, optional): CSS class for the select. Default: 'form-select'.
- size (string, optional): adds a size suffix to form-select class, 'lg' or 'sm'. Default: ''.
- id (string, optional): the ID of the element. Default: ''.
- params (string, optional): additional HTML attributes. Default: ''.
- multiple (boolean, optional): enables multiple selection. Default: false.
- disabled (boolean, optional): disables the select. Default: false.
- autocomplete (string, optional): the autocomplete attribute value. Default: ''.
- readonly (boolean, optional): sets the select as readonly. Default: false.
- required (boolean, optional): marks the field as required. Default: false.
- html5Required (boolean, optional): uses the HTML5 required attribute. Default: true.
- helpMsg (string, optional): help message displayed below the select. Default: ''.
- errorMsg (string, optional): error message displayed on validation failure. Default: ''.

Showcase:
- desc: "Liste déroulante - @cSelect"
- bs: "forms/select"
- newFeature: false

Snippet:

    Basic select:

    <@cSelect name='country' id='country'>
        <@cOption label='-- Select a country --' value='' />
        <@cOption label='France' value='FR' />
        <@cOption label='Germany' value='DE' />
        <@cOption label='Spain' value='ES' />
    </@cSelect>

    Required select with error message:

    <@cSelect name='category' id='category' required=true errorMsg='Please select a category.'>
        <@cOption label='-- Choose --' value='' />
        <@cOption label='Culture' value='culture' />
        <@cOption label='Sport' value='sport' />
    </@cSelect>

    Multiple select:

    <@cSelect name='languages' id='languages' multiple=true>
        <@cOption label='French' value='fr' />
        <@cOption label='English' value='en' />
        <@cOption label='Spanish' value='es' />
    </@cSelect>

-->
<#macro cSelect name class='form-select' size='' id='' params='' multiple=false disabled=false autocomplete='' readonly=false required=false html5Required=true helpMsg='' errorMsg='' deprecated...>
<@deprecatedWarning args=deprecated />
<#assign idMsg><#if id!=''>${id}<#else>${name!}</#if></#assign>
<#if helpMsg !=''><@cFormHelp idMsg helpMsg /></#if>
<select name="${name!}" class="${class!}<#if size!=''> form-select-${size!}</#if><#if errorMsg!=''> is-invalid</#if>"<#if id!=''> id="${id}"</#if><#if autocomplete!=''> autocomplete="${autocomplete}"</#if><#if params!=''> ${params}</#if><#if disabled> disabled</#if><#if required> <#if html5Required>required</#if> aria-required="true"</#if><#if readonly> readonly</#if><#if multiple> multiple</#if><#if errorMsg!=''> aria-invalid="true" aria-describedby="error_${idMsg!}"<#elseif helpMsg!=''> aria-describedby="help_${idMsg!}"</#if>>
<#nested>
</select>
<#if errorMsg!=''><#assign idMsg><#if id!=''>${id}<#else>${name!}</#if></#assign><@cFormError idMsg errorMsg /></#if>
</#macro>