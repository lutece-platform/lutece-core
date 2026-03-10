<#--
Macro: cSkeleton

Description: Generates a skeleton loading placeholder to display while content is being fetched, with configurable bone types.

Parameters:
- bones (sequence, optional): List of bone type strings defining the skeleton layout ('text', 'short', 'image', 'button'). Default: {} (displays a single text bone).
- id (string, optional): HTML id attribute. Default: ''.
- class (string, optional): Additional CSS classes. Default: ''.
- params (string, optional): Additional HTML attributes. Default: ''.

Showcase:
- desc: Skeleton loader - @cSkeleton
- newFeature: false

Snippet:

    Default skeleton (single text line):

    <@cSkeleton />

    Custom skeleton with multiple bone types:

    <@cSkeleton bones=['image', 'text', 'short', 'button'] />

    Skeleton with custom class:

    <@cSkeleton bones=['text', 'text', 'short'] class='my-4' id='loading-placeholder' />

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