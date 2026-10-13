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
<#if args?size != 0 >
<#-- an empty attrs is ignored: core macros pass it on to the macros they call, which a site theme may
     override with a version that does not declare attrs yet -->
<#local wrongArgs = args?keys?filter( key -> key != 'attrs' || args[key]?has_content ) />
<#if wrongArgs?size != 0 ><!-- Warning : wrong or deprecated argument(s) : <#list wrongArgs as key >${key}, </#list> ... --></#if>
</#if>
</#macro>