<#-- INTERNAL FUNCTIONS -->
<#--
Macro: deprecatedWarning

Description: Generates an HTML comment warning when deprecated or unrecognized arguments are passed to a macro.

Parameters:
- args (map, optional): A map of deprecated argument names and their values. Default: [].

Snippet:

    Usage inside a macro that captures deprecated arguments:

    <@deprecatedWarning args=deprecated />

-->
<#macro deprecatedWarning args=[] >
<#if args?size != 0 >
<#-- an empty attrs is ignored: core macros pass it on to the macros they call, which a site theme may
     override with a version that does not declare attrs yet -->
<#local wrongArgs = args?keys?filter( key -> key != 'attrs' || args[key]?has_content ) />
<#if wrongArgs?size != 0 ><!-- Warning : wrong or deprecated argument(s) : <#list wrongArgs as key >${key}, </#list> ... --></#if>
</#if>
</#macro>