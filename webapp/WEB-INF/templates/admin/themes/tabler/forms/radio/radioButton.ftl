<#--
Macro: radioButton

Description: Generates an HTML radio button element with a specified name, ID, label, orientation, value, tabindex, title, disabled, readonly, checked, parameters, and mandatory flag.

Parameters:
- name (string, required): the name for the radio button element.
- id (string, optional): the ID for the radio button element.
- labelKey (string, optional): the label for the radio button element.
- labelFor (string, optional): the "for" attribute for the radio button's label element.
- orientation (string, optional): the orientation for the radio button element, either "vertical" (default) or "inline".
- value (string, optional): the value for the radio button element.
- tabIndex (string, optional): the tabindex for the radio button element.
- title (string, optional): the title for the radio button element.
- disabled (boolean, optional): whether the radio button element is disabled.
- readonly (boolean, optional): whether the radio button element is readonly.
- checked (boolean, optional): whether the radio button element is checked.
- params (string, optional): additional parameters to add to the HTML code.
- attrs (hash, optional): additional HTML attributes as name/value pairs, e.g. {'aria-hidden': 'true'}. Prefer it to params: the macro writes the attribute and its value is escaped like any value of the page.
- mandatory (boolean, optional): whether the radio button element is mandatory.
- propagateMandatory (boolean, optional): whether to propagate the mandatory flag to the radio button element.

Snippet:

    Vertical radio button group:

    <@radioButton name='gender' id='gender_male' labelKey='Male' value='male' />
    <@radioButton name='gender' id='gender_female' labelKey='Female' value='female' />
    <@radioButton name='gender' id='gender_other' labelKey='Other' value='other' />

    Inline radio buttons with a pre-selected value:

    <@radioButton name='status' id='status_active' labelKey='Active' value='active' orientation='inline' checked=true />
    <@radioButton name='status' id='status_inactive' labelKey='Inactive' value='inactive' orientation='inline' />

-->
<#macro radioButton name id='' class='form-check-input' labelKey='' labelClass='' labelFor='' orientation='vertical' value='' tabIndex='' title='' disabled=false readonly=false checked=false params='' attrs={} mandatory=false  deprecated...>
<@deprecatedWarning args=deprecated />
<#local isRequired = mandatory || ( propagateMandatory?? && propagateMandatory ) />
<#-- params and attrs go on the wrapper when there is one, else on the input -->
<#if orientation='vertical'><div class="radio"<@commonAttrs params=params attrs=attrs />>	</#if>
<label  class="form-check<#if orientation!='vertical'> form-check-inline</#if><#if labelClass!=''> ${labelClass!}</#if>"<#if labelFor!=''> for="${labelFor}"</#if>>
<input class="<#if class!=''> ${class}</#if>" type="radio" id="${id}" name="${name}" value="<#if value!=''>${value}</#if>"<#if tabIndex!=''> tabindex="${tabIndex}"</#if><#if checked> checked</#if><#if disabled> disabled</#if><#if readonly> readonly</#if><#if orientation !='vertical'><@commonAttrs params=params attrs=attrs /></#if><#if isRequired> required</#if>>
<#if labelKey!=''>
<span class="form-check-label<#if labelClass!=''> ${labelClass!}</#if>"><#if labelKey!=''>${labelKey}<#else><#nested></#if></span>
</#if>
<#nested>
</label>
<#if orientation='vertical'></div></#if>
</#macro>