<#-- Macro: deprecatedWarning

Description: Generates a warning message for deprecated or incorrect arguments.

Parameters:
- args (map, optional): a map of arguments and their values.

Snippet:

    Display a deprecation warning for unknown arguments:

    <@deprecatedWarning args={'oldParam': 'value', 'removedParam': 'test'} />
   Outputs an HTML comment: Warning : wrong or deprecated argument(s) : oldParam, removedParam, ... 

-->
<#macro deprecatedWarning args=[] >
<#if args?size != 0 ><!-- Warning : wrong or deprecated argument(s) : <#list  args?keys as key >${key}, </#list> ... --></#if>
</#macro>