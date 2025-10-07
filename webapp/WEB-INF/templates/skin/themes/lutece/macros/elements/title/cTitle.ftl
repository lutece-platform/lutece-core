<#-- Name :   cTitle            -->
<#-- Attributes List  :         -->
<#-- level                      -->
<#-- id                         -->
<#-- class                      -->
<#-- params                     -->
<#macro cTitle level=1 id='' class='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<h${level}<#if class!=''> class="${class!}"</#if><#if id!=''> id="${id!}"</#if><#if params!=''> ${params!}</#if>>
<#nested/>
</h${level}>
</#macro>