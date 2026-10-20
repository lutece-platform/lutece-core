<#--
Macro: fieldSet

Description: Generates a fieldset element with a specified class, ID, legend, and additional parameters.

Parameters:
- class (string, optional): additional classes to add to the fieldset.
- fieldsetId (string, optional): the ID for the fieldset.
- fieldsetParams (map, optional): additional HTML attributes of the fieldset as name/value pairs, e.g. {'data-x': 'y'}. Default: {}.
- legend (string, optional): the text to use as the legend for the fieldset.
- legendClass (string, optional): additional classes to add to the legend.
- legendId (string, optional): the ID for the legend.
- legendIcon (string, optional): the name of the icon to use in the legend.
- legendParams (map, optional): additional HTML attributes of the legend as name/value pairs, e.g. {'data-x': 'y'}. Default: {}.
- hideLegend (list, optional): list of strings that, if non-empty, hide the legend.
- disabled (boolean, optional): specifies whether the fieldset is disabled.

Snippet:

    Fieldset with a legend and form groups:

    <@fieldSet legend='Personal Information'>
        <@formGroup labelKey='#i18n{portal.users.label.name}' labelFor='name' formStyle='horizontal'>
            <@input type='text' name='name' id='name' />
        </@formGroup>
        <@formGroup labelKey='#i18n{portal.users.label.email}' labelFor='email' formStyle='horizontal'>
            <@input type='email' name='email' id='email' />
        </@formGroup>
    </@fieldSet>

    Disabled fieldset with legend icon:

    <@fieldSet legend='Locked Settings' legendIcon='lock' disabled=true>
        <@formGroup labelKey='#i18n{portal.settings.label.option}' labelFor='option' formStyle='horizontal'>
            <@input type='text' name='option' id='option' />
        </@formGroup>
    </@fieldSet>

-->
<#macro fieldSet class='' fieldsetId='' fieldsetParams={} legend='' legendClass='' legendId='' legendIcon='' legendParams={} hideLegend=[] disabled=false params={} deprecated...>
<@deprecatedWarning args=deprecated />
<fieldset<@commonAttrs id=fieldsetId classBase='mb-3' class=class params=fieldsetParams /><#if disabled> disabled</#if>>
<#if legend?has_content>
	<#local legendFullClass = legendClass + ' ' + hideLegend?seq_contains( 'all' )?then( 'visually-hidden', displaySettings( hideLegend, 'block' ) ) />
	<legend<#if legendFullClass?trim?has_content> class="${legendFullClass?trim}"</#if><#if legendId?has_content> id="${legendId}"</#if><@commonAttrs params=legendParams />><#if legendIcon?has_content><@icon style=legendIcon /> </#if>${legend}</legend>
</#if>
<#nested>
</fieldset>
</#macro>