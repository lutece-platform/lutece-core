<#--
Macro: cLabel

Description: Generates a form label element with optional required asterisk indicator and visibility control.

Parameters:
- label (string, required): the label text.
- class (string, optional): CSS class for the label. Default: ''.
- id (string, optional): the ID of the label. Default: ''.
- params (string, optional): additional HTML attributes. Default: ''.
- for (string, optional): the ID of the associated form element. Default: ''.
- showLabel (boolean, optional): displays or hides the label (uses visually-hidden class). Default: true.
- required (boolean, optional): displays a required asterisk (*) after the label. Default: false.

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
<#macro cLabel label class='' id='' params='' for='' showLabel=true required=false deprecated...>
<@deprecatedWarning args=deprecated />
<#local isrequired=required />
<#if propagateRequired?? && propagateRequired ><#local isrequired=true /></#if>
<label class="<#if class!=''>${class}</#if><#if !showLabel> visually-hidden</#if>"<#if for!=''> for="${for!}"</#if><#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>>
${label!}<#if isrequired>&nbsp;<span class="main-danger-color" tabindex="0" title="#i18n{portal.theme.labelMandatory}">*</span></#if>
<#nested>
</label>
</#macro>