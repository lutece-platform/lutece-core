<#-- Macro: cSkeleton

Description: affiche un "squelette" lors qu chargement de données.

Parameters:
@param - bones - json - optional - permet de définir la liste des items du skeleton, si non défini affiche un squelette par défaut
-->  
<#macro cSkeleton bones={} id='' class='' params='' deprecated...>
<@deprecatedWarning args=deprecated /> 
<div class="skeleton<#if class !='' > ${class!}</#if>"<#if id !='' > id="${id!}"</#if><#if params!=''> ${params}</#if>>
<#if bones?size gt 0>
    <#list bones as bone>
        <#switch bone>
            <#case 'text'>
                <div class="sk-bone sk-text"></div>
                <#break>
            <#case 'short'>
                <div class="sk-bone sk-text-short"></div>
                <#break>
            <#case 'image'>
                <div class="sk-bone sk-image-wrapper">
                    <div class="sk-bone sk-image"></div>
                </div>
                <#break>
            <#case 'button'>
                <div class="sk-bone sk-button"></div>
                <#break>
        </#switch>
    </#list>
<#else>
    <div class="sk-bone sk-text"></div>
</#if>
</div>
</#macro>