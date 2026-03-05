<#--
Macro: cContainer

Description: Generates a Bootstrap container element, with an optional fluid mode for full-width layout.

Parameters:
- type (string, optional): Container type; set to 'fluid' for a full-width container. Default: ''.
- class (string, optional): Additional CSS class(es) for the container. Default: ''.
- id (string, optional): Unique identifier for the container element. Default: ''.
- params (string, optional): Additional HTML attributes for the container element. Default: ''.

Snippet:

    Basic container:

    <@cContainer>
        <p>Page content inside a centered container</p>
    </@cContainer>

    Full-width fluid container:

    <@cContainer type='fluid' class='bg-light py-3'>
        <p>Full-width section content</p>
    </@cContainer>

-->
<#macro cContainer type='' class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#local tpl=.caller_template_name?keep_after("skin/") />
<@cTpl tpl=tpl>
<#if class!=''>
    <#local cClass>container<#if type!=''>-${type}</#if> ${class}</#local> 
<#else>
    <#local cClass>container<#if type!=''>-fluid</#if></#local>
</#if>
<@cSection type='div' class=cClass id=id params=params>
<#nested>
</@cSection>
</@cTpl>
</#macro>