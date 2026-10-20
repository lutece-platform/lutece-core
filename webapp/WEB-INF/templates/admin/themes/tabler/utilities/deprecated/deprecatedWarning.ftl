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
<#-- attrs is removed (LUT-33387): an attribute given through it would be lost without a word, so it fails -->
<#if args?keys?seq_contains( 'attrs' )><#stop 'The attrs argument of the macros is removed: pass the attributes as params={...}' /></#if>
<!-- Warning : wrong or deprecated argument(s) : <#list args?keys as key >${key}, </#list> ... -->
</#if>
</#macro>