<#--
Macro: cFieldset

Description: Generates a fieldset container with an optional legend, help message, required indicator, and ARIA attributes for accessibility.

Parameters:
- legend (string, optional): the legend text. Default: ''.
- legendClass (string, optional): CSS class for the legend. Default: ''.
- role (string, optional): ARIA role attribute for the fieldset. Default: 'group'.
- class (string, optional): CSS class for the fieldset. Default: ''.
- id (string, optional): the ID of the fieldset. Default: ''.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.
- for (string, optional): links an ID with the fieldset. Default: ''.
- helpMsg (string, optional): help message for the fieldset. Default: ''.
- errorMsg (string, optional): error message of the group, written after the legend and linked to the fieldset by aria-describedby. Default: ''.
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
<#macro cFieldset legend='' legendClass='' role='group' class='' id='' params={} for='' helpMsg='' errorMsg='' helpPos='top' showLabel=true required=false deprecated...>
<@deprecatedWarning args=deprecated />
<#local cId><#if !id?has_content>id-${random()}<#else>${id}</#if></#local>
<#local describedBy = ( errorMsg?has_content?then( 'error_' + cId?markup_string, '' ) + ' ' + helpMsg?has_content?then( 'help_' + cId?markup_string, '' ) )?trim />
<fieldset id="fieldset-${cId}"<@commonAttrs class=class params=params /><#if role?has_content && role != 'group'> role="${role}"</#if><#if describedBy?has_content> aria-describedby="${describedBy}"</#if>>
<#if legend?has_content><legend <#if legendClass?has_content || !showLabel>class="${legendClass!}<#if !showLabel> visually-hidden</#if>"</#if> id="legend-${cId}">${legend!}<#if required> <span class="main-danger-color" title="#i18n{portal.theme.labelMandatory}" aria-hidden="true">*</span></#if></legend></#if>
<#if errorMsg?has_content><@cFormError cId errorMsg /></#if>
<#if helpPos == 'top' && helpMsg?has_content><@cFormHelp cId helpMsg /></#if>
<#nested>
<#if helpPos == 'after' && helpMsg?has_content><@cFormHelp cId helpMsg /></#if>
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
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

Snippet:

    Basic legend:

    <@cLegend label='Billing address' />

    Hidden legend for accessibility:

    <@cLegend label='Filter options' showLabel=false />

-->
<#macro cLegend label='' showLabel=true class='' id='' params={} deprecated...>
<@deprecatedWarning args=deprecated />
<legend <#if class?has_content>class="${class!}<#if !showLabel> visually-hidden</#if>"</#if> <@commonAttrs id=id />>
<#if label?has_content>${label!} </#if><#nested>
</legend>
</#macro>