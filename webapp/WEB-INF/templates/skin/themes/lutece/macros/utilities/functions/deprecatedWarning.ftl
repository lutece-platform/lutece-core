<#-- INTERNAL FUNCTIONS -->
<#-- Macro: deprecatedWarning
Description: Generates a warning message for deprecated or incorrect arguments.
Parameters:
- args (map, optional): a map of arguments and their values.
-->
<#macro deprecatedWarning args=[] >
<#if args?size != 0 ><!-- Warning : wrong or deprecated argument(s) : <#list  args?keys as key >${key}, </#list> ... --></#if>
</#macro>