<#-- cOption SELECT                          -->
<#-- --------------------------------------  -->
<#-- Attributes                              -->
<#-- --------------------------------------  -->
<#-- class                                   -->
<#-- id                                      -->
<#-- params                                  -->
<#-- Macro: cOption

Description: Defines a macro that show an option tag for a select

Parameters:
@param - label - string - required - Label of the option, default ''
@param - value - string - required - Value of the option, default ''
@param - selected - boolean - optional - Selected attribute on the option element, default false
@param - disabled - boolean - optional - Disable element, default false
@param - class - string - optional - the CSS class of the element, default '' 
@param - id - string - optional - the ID of the element, default ''
@param - params - string - optional - additional HTML attributes to include in the parent block element default ''
-->
<#macro cOption label value id='' class='' selected=false disabled=false params='' deprecated...>
<@deprecatedWarning args=deprecated />
<option <#if id!=''>id="${id}"</#if><#if class!=''> class="${class}"</#if> value="${value!}"<#if selected> selected</#if><#if disabled> disabled</#if><#if params!=''>${params}</#if>>${label!}</option>
</#macro>