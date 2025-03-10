<#-- Macro: cMultiselectOption

Description: Defines a macro that show an option for multiselect tag

Parameters:
@param - class - string - optional - the CSS class of the option
@param - classCheckbox - string - optional - the CSS class of the checkbox in the option
@param - name - string  - required - the name of of the option
@param - label - string - required - the label associated to the option
@param - value - string - optional - the value associated to the checkbox of the option
@param - id - string - optional - the ID of the element, default ''
@param - params - optional - additional HTML attributes to include in the option element default ''
@param - disabled - boolean - optional - Disable element, default false
-->
<#macro cMultiselectOption name label class='' classCheckbox='' value='' id='' params='' disabled=false deprecated...>
<@deprecatedWarning args=deprecated />
<@chItem class=class!>
    <@cCheckbox class=classCheckbox name=name id=id label=label value=value type='button' disabled=disabled params=params/>
</@chItem>    
</#macro>