<#--
Macro: fieldSet

Description: Generates a fieldset element with a specified class, ID, legend, and additional parameters.

Parameters:
- class (string, optional): additional classes to add to the fieldset.
- fieldsetId (string, optional): the ID for the fieldset.
- fieldsetParams (string, optional): additional parameters to add to the fieldset.
- legend (string, optional): the text to use as the legend for the fieldset.
- legendClass (string, optional): additional classes to add to the legend.
- legendId (string, optional): the ID for the legend.
- legendIcon (string, optional): the name of the icon to use in the legend.
- legendParams (string, optional): additional parameters to add to the legend.
- hideLegend (list, optional): list of strings that, if non-empty, hide the legend.
- disabled (boolean, optional): specifies whether the fieldset is disabled.
-->
<#macro fieldSet class='' fieldsetId='' fieldsetParams='' legend='' legendClass='' legendId='' legendIcon='' legendParams='' hideLegend=[] disabled=false deprecated...>
<@deprecatedWarning args=deprecated />
<fieldset class="mb-3<#if class!=''> ${class}</#if>"<#if fieldsetId!=''> id="${fieldsetId}"</#if><#if fieldsetParams!=''> ${fieldsetParams}</#if><#if disabled> disabled</#if>>
<#if legend!=''>
	<#local legendClass += ' ' +  displaySettings(hideLegend,'block') />
	<legend<#if legendClass!=''> class="${legendClass?trim}"</#if><#if legendId!=''> id="${legendId}"</#if><#if legendParams!=''> ${legendParams}</#if>><#if legendIcon!=''><@icon style=legendIcon /> </#if>${legend}</legend>
</#if>
<#nested>
</fieldset>
</#macro>