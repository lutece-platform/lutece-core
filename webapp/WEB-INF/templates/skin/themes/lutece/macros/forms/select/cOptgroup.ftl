<#-- Macro: cOptgroup

Description: Defines a macro that show an option group - optgroup- tag for a select

Parameters:
@param - label - string - required - Label of the option group, default ''
@param - class - string - optional - the CSS class of the element, default '' 
@param - disabled - boolean - optional - Disable element, default false
@param - params - string - optional - additional HTML attributes to include in the parent block element default ''
-->
<#macro cOptgroup label class='' disabled=false params='' deprecated...>
<@deprecatedWarning args=deprecated />
<optgroup label="${label!}" <#if class!=''>class="${class}"</#if><#if disabled> disabled</#if><#if params!=''>${params}</#if>>
<#nested>
</optgroup>
</#macro>