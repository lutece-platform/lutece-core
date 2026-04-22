<#--
Macro: cFieldset

Description: Generates a fieldset container with an optional legend, help message, required indicator, and ARIA attributes for accessibility.

Parameters:
- legend (string, optional): the legend text. Default: ''.
- legendClass (string, optional): CSS class for the legend. Default: ''.
- role (string, optional): ARIA role attribute for the fieldset. Default: 'group'.
- class (string, optional): CSS class for the fieldset. Default: ''.
- id (string, optional): the ID of the fieldset. Default: ''.
- params (string, optional): additional HTML attributes. Default: ''.
- for (string, optional): links an ID with the fieldset. Default: ''.
- helpMsg (string, optional): help message for the fieldset. Default: ''.
- helpPos (string, optional): position of the help message, 'top' (after legend) or 'after' (after content). Default: 'top'.
- showLabel (boolean, optional): displays or hides the legend. Default: true.
- required (boolean, optional): displays a required asterisk (*) after the legend. Default: false.

Showcase:
- desc: "Fieldset - @cFieldset"
- bs: "forms/overview"
- newFeature: false

Snippet:

    Basic fieldset with legend:

    <@cFieldset legend='Personal information'>
        <@cField label='First name' for='firstname' required=true>
            <@cInput name='firstname' id='firstname' />
        </@cField>
        <@cField label='Last name' for='lastname' required=true>
            <@cInput name='lastname' id='lastname' />
        </@cField>
    </@cFieldset>

    Required fieldset with help message:

    <@cFieldset legend='Contact details' required=true helpMsg='Please fill in all required fields.'>
        <@cField label='Email' for='email'>
            <@cInput name='email' id='email' type='email' />
        </@cField>
    </@cFieldset>

    Fieldset with hidden legend:

    <@cFieldset legend='Address section' showLabel=false>
        <@cField label='Address' for='address'>
            <@cInput name='address' id='address' />
        </@cField>
    </@cFieldset>

-->
<#macro cFieldset legend='' legendClass='' role='group' class='' id='' params='' for='' helpMsg='' helpPos='top' showLabel=true required=false deprecated...>
<@deprecatedWarning args=deprecated />
<#local cId><#if id=''>id-${random()}<#else>${id}</#if></#local>
<fieldset<#if class!=''> class="${class}"</#if><#if for!=''> for="${for!}"</#if> id="fieldset-${cId}"<#if params!=''> ${params}</#if><#if role!=''> role="${role}"</#if> aria-labelledby="legend-${cId}"<#if helpMsg !=''> aria-describedby="help_${cId!}"</#if>>
<#if legend!=''><legend <#if legendClass!='' || !showLabel>class="${legendClass!}<#if !showLabel> visually-hidden</#if>"</#if> id="legend-${cId}" <#if required> aria-required="true"</#if>>${legend!}<#if required> <span class="main-danger-color" tabindex="-1" title="#i18n{portal.theme.labelMandatory}">*</span></#if></legend></#if>
<#if helpPos == 'top' && helpMsg !=''><@cFormHelp cId helpMsg /></#if>
<#nested>
<#if helpPos == 'after' && helpMsg !=''><@cFormHelp cId helpMsg /></#if>
</fieldset>
</#macro>
<#--
Macro: cLegend

Description: Generates a standalone legend element for a fieldset with optional visibility control.

Parameters:
- label (string, optional): the legend text. Default: ''.
- showLabel (boolean, optional): displays or hides the legend. Default: true.
- class (string, optional): CSS class for the legend. Default: ''.
- id (string, optional): the ID of the legend. Default: ''.
- params (string, optional): additional HTML attributes. Default: ''.

Snippet:

    Basic legend:

    <@cLegend label='Billing address' />

    Hidden legend for accessibility:

    <@cLegend label='Filter options' showLabel=false />

-->
<#macro cLegend label='' showLabel=true class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<legend <#if class!=''>class="${class!}<#if !showLabel> visually-hidden</#if>"</#if> <#if id!=''>id="${id}"</#if>>
<#if label!=''>${label!} </#if><#nested>
</legend>
</#macro>