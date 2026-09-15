<#--
Macro: cContainer

Description: Generates a Bootstrap container element, with an optional fluid mode for full-width layout.

Parameters:
- type (string, optional): Container type; set to 'fluid' for a full-width container. Default: ''.
- class (string, optional): Additional CSS class(es) for the container. Default: ''.
- id (string, optional): Unique identifier for the container element. Default: ''.
- params (string, optional): Additional HTML attributes for the container element. Default: ''.

Showcase:
- desc: "Conteneur - @cContainer"
- bs: layout/containers
- newFeature: false

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
<#if class?has_content>
    <#local cClass>container<#if type?has_content>-${type}</#if> ${class}</#local>
<#local cClass = cClass?is_markup_output?then(cClass?markup_string, cClass) /> 
<#else>
    <#local cClass>container<#if type?has_content>-fluid</#if></#local>
<#local cClass = cClass?is_markup_output?then(cClass?markup_string, cClass) />
</#if>
<@cSection type='div' class=cClass id=id params=params>
<#nested>
</@cSection>
</@cTpl>
</#macro>