<#--
Macro: cLabel

Description: Generates a form label element with optional required asterisk indicator and visibility control.

Parameters:
- label (string, required): the label text.
- class (string, optional): CSS class for the label. Default: ''.
- id (string, optional): the ID of the label. Default: ''.
- params (string, optional): additional HTML attributes. Default: ''.
- attrs (hash, optional): additional HTML attributes as name/value pairs, e.g. {'aria-hidden': 'true'}. Prefer it to params: the macro writes the attribute and its value is escaped like any value of the page.
- for (string, optional): the ID of the associated form element. Default: ''.
- showLabel (boolean, optional): displays or hides the label (uses visually-hidden class). Default: true.
- required (boolean, optional): displays a required asterisk (*) after the label. Default: false.

Showcase:
- desc: "Label - @cLabel"
- bs: "forms/form-control"
- newFeature: false

Snippet:

    Basic label:

    <@cLabel label='Email address' for='email' />
    <@cInput name='email' id='email' type='email' />

    Required label:

    <@cLabel label='Last name' for='lastname' required=true />
    <@cInput name='lastname' id='lastname' required=true />

    Hidden label for accessibility:

    <@cLabel label='Search' for='search' showLabel=false />
    <@cInput name='search' id='search' placeholder='Search...' />

-->
<#macro cLabel label class='' id='' params='' attrs={} for='' showLabel=true required=false showRequiredLabel=true deprecated...>
<@deprecatedWarning args=deprecated />
<#local isrequired=required />
<label class="form-label<#if class!=''> ${class}</#if><#if !showLabel> visually-hidden</#if>"<#if for!=''> for="${for!}"</#if><@commonAttrs id=id params=params attrs=attrs />>
<#nested> 
${label!}<#if isrequired && showRequiredLabel><span class="main-danger-color ms-xxs" title="#i18n{portal.theme.labelMandatory}">*</span></#if>
</label>
<#if propagateRequired?? && propagateRequired ><#local isrequired=true /></#if>
</#macro>