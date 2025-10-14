<#-- Name :   cBlock            -->
<#-- Attributes List  :         -->
<#-- type: section, div         -->
<#-- class (default) : ''       -->
<#-- id                         -->
<#-- params                     -->
<#macro cBlock type='div' class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<${type}<#if class!=''> class="${class}"</#if><#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>>
<#nested>
</${type}>
</#macro>