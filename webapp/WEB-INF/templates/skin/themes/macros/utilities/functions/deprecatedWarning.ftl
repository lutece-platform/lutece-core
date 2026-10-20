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
<#-- attrs is removed (LUT-33387): an attribute given through it would be lost without a word, so it fails -->
<#if args?keys?seq_contains( 'attrs' )><#stop 'The attrs argument of the macros is removed: pass the attributes as params={...}' /></#if>
<!-- Warning : wrong or deprecated argument(s) : <#list args?keys as key >${key}, </#list> ... -->
</#if>
</#macro>